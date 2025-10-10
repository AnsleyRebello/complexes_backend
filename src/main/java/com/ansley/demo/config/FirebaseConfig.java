package com.ansley.demo.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.charset.StandardCharsets;

@Configuration
public class FirebaseConfig {

    private static final String TEMP_PATH = "/tmp/firebase.json";
    private static final String RESOURCE_PATH = "src/main/resources/firebase.json";
    private static final String ENV_VAR = "GCP_SA_JSON";

    @PostConstruct
    public void init() {
        try {
            String envJson = System.getenv(ENV_VAR);
            FileInputStream serviceAccountStream;

            if (envJson != null && !envJson.isBlank()) {
                // Write env var to temp file (safe for containers)
                File tmp = new File(TEMP_PATH);
                try (FileOutputStream fos = new FileOutputStream(tmp)) {
                    fos.write(envJson.getBytes(StandardCharsets.UTF_8));
                    fos.flush();
                }
                serviceAccountStream = new FileInputStream(tmp);
                System.out.println("✅ Firebase: loaded service account from GCP_SA_JSON env var -> " + TEMP_PATH);
            } else {
                // Fall back to local file if present
                File f = new File(RESOURCE_PATH);
                if (!f.exists()) {
                    throw new IllegalStateException("Firebase service account not found. " +
                            "Set env var GCP_SA_JSON or place firebase.json at " + RESOURCE_PATH);
                }
                serviceAccountStream = new FileInputStream(f);
                System.out.println("✅ Firebase: loaded service account from " + RESOURCE_PATH);
            }

            FirebaseOptions options = new FirebaseOptions.Builder()
                    .setCredentials(GoogleCredentials.fromStream(serviceAccountStream))
                    .build();

            if (FirebaseApp.getApps().isEmpty()) {
                FirebaseApp.initializeApp(options);
                System.out.println("✅ Firebase connected successfully!");
            }
        } catch (Exception e) {
            // Fatal for app: print and rethrow so you can see it in logs
            e.printStackTrace();
            throw new RuntimeException("Failed to initialize Firebase: " + e.getMessage(), e);
        }
    }
}
