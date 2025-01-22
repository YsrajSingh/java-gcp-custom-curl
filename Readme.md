# Token & Upload Solution

A custom solution that securely generates access tokens from a `credentials.json` file and enables image uploads using cURL commands, all without relying on SDKs. This tool streamlines authentication and file transfer processes, making it easy to integrate with various platforms.

## Features

- **Access Token Generation**: Securely generate access tokens from a `credentials.json` file, enabling authenticated access to cloud storage or APIs.
- **Image Upload**: Upload images or other files using custom cURL commands, bypassing the need for any SDKs.
- **No SDK Dependency**: The solution is entirely independent of any specific SDK or cloud service, using simple cURL requests for the file upload and access token handling.

## Requirements

- **Java Runtime** (for compiling and running the code)
- **cURL** (for making HTTP requests to upload the file)
- A valid `credentials.json` file for generating access tokens.
- A destination URL for the file upload.

## How It Works

1. **Access Token Generation**:
   - The tool reads a `credentials.json` file, extracts the necessary information, and generates an access token for secure communication with APIs or cloud storage services.

2. **Image Upload**:
   - The tool allows you to upload images by sending a `POST` request to a provided URL using the cURL command, with the generated access token for authentication.

## Setup

1. Clone or download the project.
2. Place your `credentials.json` file in the project directory.
3. Compile and run the solution.

## Usage

1. **Generate Access Token:**

    The solution will generate an access token by processing the credentials.json file.

2. **Upload File:**

    After the token is generated, you can use the provided cURL command to upload files to your cloud storage or server.

## Open Source
This project is open source and available under the MIT License. Feel free to fork it, contribute, or open issues for any bugs or enhancements you would like to see. To contribute:

1. Fork the repository.
1. Create a new branch for your feature or bug fix.
1. Commit your changes.
1. Push to the branch.
1. Open a pull request.

We welcome all contributions, big and small, to make this tool better!

## License
This project is licensed under the MIT License - see the [LICENSE](https://github.com/YsrajSingh/java-gcp-custom-curl/blob/main/LICENSE) file for details.