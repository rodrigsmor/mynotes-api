package com.rm.mynotes.utils.config;

import com.google.auth.Credentials;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.*;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.cloud.StorageClient;
import com.rm.mynotes.utils.constants.FileTypes;
import com.rm.mynotes.utils.functions.CommonFunctions;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.HashMap;

@Slf4j
@Component
@RequiredArgsConstructor
public class FirebaseConfig {
    @Value("${FIREBASE_BUCKET_NAME}")
    private String FIREBASE_BUCKET_NAME;

    @Value("${FIREBASE_SERVICE_ACCOUNT_KEY}")
    private String FIREBASE_SERVICE_ACCOUNT_KEY;

    @Value("${FIREBASE_DOWNLOAD_URL}")
    private String DOWNLOAD_URL;

    public void firebaseConfig() throws IOException {
        FileInputStream refreshToken = new FileInputStream(FIREBASE_SERVICE_ACCOUNT_KEY);

        FirebaseOptions options = FirebaseOptions
                .builder()
                .setCredentials(GoogleCredentials.fromStream(refreshToken))
                .setDatabaseUrl(FIREBASE_BUCKET_NAME)
                .build();

        FirebaseApp.initializeApp();

        Bucket bucket = StorageClient.getInstance().bucket();
    }

    public HashMap<String, Object> uploadImage(MultipartFile multipartFile, FileTypes type) throws IOException {
        HashMap<String, Object> response = new HashMap<>();

        try {
            String filename = CommonFunctions.formatFilename(multipartFile, type);
            File file = this.convertToFile(multipartFile, filename);

            String uploadFileUrl = this.uploadFile(file, filename);
            file.delete();

            response.put("success", true);
            response.put("imageUrl", uploadFileUrl);

            return response;
        } catch (Exception e) {
            e.printStackTrace();

            response.put("success", false);
            response.put("Exception", e);
            return response;
        }
    }

    private String uploadFile(File file, String filename) throws IOException {
        BlobId blobId = BlobId.of(FIREBASE_BUCKET_NAME, filename);
        BlobInfo blobInfo = BlobInfo.newBuilder(blobId).setContentType("media").build();

        Credentials credentials = GoogleCredentials.fromStream(new FileInputStream(FIREBASE_SERVICE_ACCOUNT_KEY));
        Storage storage = StorageOptions.newBuilder().setCredentials(credentials).setProjectId(FIREBASE_BUCKET_NAME).build().getService();

        storage.create(blobInfo, Files.readAllBytes(file.toPath()));

        return String.format(
                DOWNLOAD_URL,
                FIREBASE_BUCKET_NAME,
                URLEncoder.encode(filename, StandardCharsets.UTF_8)
        );
    }

    private File convertToFile(MultipartFile file, String filename) throws IOException {
        File tempFile = new File(filename);

        try (FileOutputStream fos = new FileOutputStream(tempFile)) {
            fos.write(file.getBytes());
            fos.close();
        }

        return tempFile;
    }
}
