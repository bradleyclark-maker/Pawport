package compawportweb;

import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import compawportweb.awsconfig.AwsConfig;
import software.amazon.awssdk.services.s3.model.ListObjectsRequest;
import software.amazon.awssdk.services.s3.model.ListObjectsResponse;
import software.amazon.awssdk.services.s3.model.S3Object;
import java.util.List;
import software.amazon.awssdk.http.apache.ApacheHttpClient;

/**
 * This service handles all interactions with the PawPort S3 Vault.
 */
public class S3Service {

    // Helper method to get a client for other parts of your app (like Servlets)
	public static S3Client getS3Client() {
	    return S3Client.builder()
	            .region(Region.of(AwsConfig.REGION))
	            .httpClientBuilder(ApacheHttpClient.builder()) // Forces Apache instead of Netty
	            .credentialsProvider(() -> software.amazon.awssdk.auth.credentials.AwsBasicCredentials.create("temp", "temp"))
	            .overrideConfiguration(conf -> conf.apiCallAttemptTimeout(java.time.Duration.ofSeconds(5)))
	            .build();
	}

    // The handshake test method
    public void testConnection() {
        S3Client s3 = getS3Client();
        
        try {
            ListObjectsRequest listObjectsRequest = ListObjectsRequest.builder()
                    .bucket(AwsConfig.BUCKET_NAME)
                    .build();

            ListObjectsResponse res = s3.listObjects(listObjectsRequest);
            System.out.println("--- PawPort Connection Success! ---");
            System.out.println("Handshake complete with bucket: " + AwsConfig.BUCKET_NAME);
            
            // Just in case there are files already, let's list them
            for (S3Object myValue : res.contents()) {
                System.out.println("Found File: " + myValue.key());
            }
            
        } catch (Exception e) {
            System.err.println("Handshake Failed: " + e.getMessage());
            // This will tell us if it's a Credential issue or a Region issue
            e.printStackTrace(); 
        }
    }

    // The single "Bypass" Test to run without Tomcat
    public static void main(String[] args) {
        try {
            System.out.println("Step 1: Testing basic internet reachability...");
            java.net.URL url = new java.net.URL("https://www.google.com");
            java.net.HttpURLConnection urlConn = (java.net.HttpURLConnection) url.openConnection();
            urlConn.connect();
            System.out.println("Step 2: Internet is reachable! Response Code: " + urlConn.getResponseCode());
            
            System.out.println("Step 3: Testing AWS Endpoint resolution...");
            java.net.InetAddress address = java.net.InetAddress.getByName("s3.us-east-2.amazonaws.com");
            System.out.println("Step 4: AWS S3 found at: " + address.getHostAddress());

        } catch (Exception e) {
            System.err.println("CRITICAL: Network is unreachable from inside Eclipse!");
            e.printStackTrace();
        }
    }
}