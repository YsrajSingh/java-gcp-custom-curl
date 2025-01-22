import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Files;
import java.util.Scanner;

public class FileUpload {
    public static void main(String[] args) {
        try {
            // Replace with your actual access token
            String accessToken = ""; // Update with your generated access token
            String projectId = ""; // Replace with your actual project ID
            String listBucketsUrl = "https://storage.googleapis.com/storage/v1/b?project=" + projectId;

            // Send request to list buckets
            HttpURLConnection conn = (HttpURLConnection) new URL(listBucketsUrl).openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Authorization", "Bearer " + accessToken);

            int responseCode = conn.getResponseCode();
            if (responseCode == 200) {
                BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                String inputLine;
                StringBuilder response = new StringBuilder();
                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();

                // Parse the response (we assume it's a JSON response containing bucket names)
                // Here we'll print out the list of buckets. You can enhance the code to parse the response
                // and display just the bucket names.
                System.out.println("Buckets List: ");
                System.out.println(response.toString());  // Replace with proper JSON parsing if needed

                // Let the user choose a bucket (for simplicity, we assume you want to enter it manually)
                Scanner scanner = new Scanner(System.in);
                System.out.println("Enter the bucket name to upload the file to: ");
                String chosenBucket = scanner.nextLine();

                // Upload file to the chosen bucket
                uploadFileToBucket(chosenBucket, accessToken);
            } else {
                System.out.println("Error: Received HTTP " + responseCode);
                BufferedReader in = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
                StringBuilder errorResponse = new StringBuilder();
                String inputLine;
                while ((inputLine = in.readLine()) != null) {
                    errorResponse.append(inputLine);
                }
                in.close();
                System.out.println("Error Response: " + errorResponse.toString());
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void uploadFileToBucket(String bucketName, String accessToken) {
        try {
            // Replace with the actual file path
            String filePath = "./sample1.txt"; // Replace with the actual file path

            if (bucketName.isEmpty() || filePath.isEmpty()) {
                throw new IllegalArgumentException("Missing bucket name or file path");
            }

            // Read the file content
            File file = new File(filePath);
            byte[] fileContent = Files.readAllBytes(file.toPath());

            // Prepare the URL for the file upload to Google Cloud Storage
            String uploadUrl = "https://storage.googleapis.com/upload/storage/v1/b/" + bucketName + "/o?uploadType=media&name=" + file.getName();
            URL url = new URL(uploadUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Authorization", "Bearer " + accessToken);
            conn.setRequestProperty("Content-Type", "application/octet-stream");
            conn.setDoOutput(true);

            // Write the file content to the output stream
            try (OutputStream os = conn.getOutputStream()) {
                os.write(fileContent);
            }

            // Check the response code and handle accordingly
            int responseCode = conn.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                // Successfully uploaded the file, read the response
                BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                StringBuilder response = new StringBuilder();
                String inputLine;
                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();

                // Print the response
                System.out.println("Success! Response: " + response.toString());
            } else {
                // Handle error in case of failure
                BufferedReader in = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
                StringBuilder errorResponse = new StringBuilder();
                String inputLine;
                while ((inputLine = in.readLine()) != null) {
                    errorResponse.append(inputLine);
                }
                in.close();

                System.out.println("Error: Received HTTP " + responseCode);
                System.out.println("Error Response: " + errorResponse.toString());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
