import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.Signature;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Base64;

public class Main {
    public static void main(String[] args) {
        try {
            String clientEmail = "";
            String privateKeyPem = "";
            String tokenUri = "https://oauth2.googleapis.com/token";

            if (clientEmail.isEmpty() || privateKeyPem.isEmpty() || tokenUri.isEmpty()) {
                throw new IllegalArgumentException("Missing required credentials in the decrypted payload");
            }


            // Decode private key
            privateKeyPem = privateKeyPem.replace("-----BEGIN PRIVATE KEY-----", "")
                                         .replace("-----END PRIVATE KEY-----", "")
                                         .replaceAll("\\s+", "");

            // Decode the private key
            byte[] privateKeyBytes = Base64.getDecoder().decode(privateKeyPem);
            PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(privateKeyBytes);
            PrivateKey privateKey = java.security.KeyFactory.getInstance("RSA").generatePrivate(keySpec);

            // Create a JWT
            long now = System.currentTimeMillis();
            long oneHourInMillis = 3600 * 1000;
            String jwtHeader = Base64.getUrlEncoder().withoutPadding().encodeToString("{\"alg\":\"RS256\",\"typ\":\"JWT\"}".getBytes());
            String jwtClaimSet = Base64.getUrlEncoder().withoutPadding().encodeToString((
                "{" +
                "\"iss\":\"" + clientEmail + "\"," +
                "\"scope\":\"https://www.googleapis.com/auth/cloud-platform\"," +
                "\"aud\":\"" + tokenUri + "\"," +
                "\"exp\":" + ((now + oneHourInMillis) / 1000) + "," +
                "\"iat\":" + (now / 1000) +
                "}"
            ).getBytes());

            String jwtSignatureInput = jwtHeader + "." + jwtClaimSet;

            // Sign the JWT
            Signature signature = Signature.getInstance("SHA256withRSA");
            signature.initSign(privateKey);
            signature.update(jwtSignatureInput.getBytes());
            String jwtSignature = Base64.getUrlEncoder().withoutPadding().encodeToString(signature.sign());

            String jwt = jwtSignatureInput + "." + jwtSignature;

            // Request an access token
            URL url = new URL(tokenUri);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            conn.setDoOutput(true);

            String postData = "grant_type=urn:ietf:params:oauth:grant-type:jwt-bearer&assertion=" + jwt;
            try (OutputStream os = conn.getOutputStream()) {
                os.write(postData.getBytes());
            }

            // Read the response
            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String inputLine;
            StringBuilder response = new StringBuilder();
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            // Print the access token
            System.out.println("Access Token: " + response.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
