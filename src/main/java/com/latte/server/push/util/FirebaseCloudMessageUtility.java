package com.latte.server.push.util;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import com.latte.server.common.exception.custom.FirebaseCloudMessageException;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Minky on 2021-09-03
 */

@Component
public class FirebaseCloudMessageUtility {
    private final String firebaseAdminServiceAccountPath = "src/main/resources/config/latte-13cbd-firebase-adminsdk-wvtmc-98d5440291.json";
    private FirebaseMessaging instance;


    @PostConstruct
    private void initFirebaseOptions() throws IOException {
        // TODO: new ClassPathResource("google-fcm-...-key.json").getInputStream() -> 배포시 해당 포맷으로 변경
        GoogleCredentials googleCredentials = GoogleCredentials
                .fromStream(new FileInputStream(firebaseAdminServiceAccountPath)) // Firebase Admin Key Path
                .createScoped(Arrays.asList("https://www.googleapis.com/auth/cloud-platform")); // Firebase 권한

        FirebaseApp firebaseApp = null;
        List<FirebaseApp> firebaseApps = FirebaseApp.getApps();
        if (firebaseApps != null && !firebaseApps.isEmpty()) {
            for (FirebaseApp app : firebaseApps) {
                if (app.getName().equals(FirebaseApp.DEFAULT_APP_NAME)) {
                    firebaseApp = app;
                }
            }
        } else {
            FirebaseOptions options = FirebaseOptions.builder()
                    .setCredentials(googleCredentials)
                    .build();

            firebaseApp = FirebaseApp.initializeApp(options);
        }

        this.instance = FirebaseMessaging.getInstance(firebaseApp);
    }

    public void sendTargetMessage(
            String targetToken,
            String title,
            String body,
            String target,
            String targetUrl
    ) {
        Message message = makeTargetMessage(targetToken, title, body, target, targetUrl);

        try {
            this.instance.send(message);
        } catch (FirebaseMessagingException e) {
            throw new FirebaseCloudMessageException();
        }
    }

    public void sendTopicMessage(
            String topic,
            String title,
            String body,
            String target,
            String targetUrl
    ) {
        Message message = makeTopicMessage(topic, title, body, target, targetUrl);
        try {
            this.instance.send(message);
        } catch (FirebaseMessagingException e) {
            throw new FirebaseCloudMessageException();
        }
    }

    private Message makeTargetMessage(
            String targetToken,
            String title,
            String body,
            String target,
            String targetUrl
    ) {
        Notification notification = new Notification(title, body);

        return Message.builder()
                .setToken(targetToken)
                .setNotification(notification)
                .putData("target", target)
                .putData("targetUrl", targetUrl)
                .build();

    }

    private Message makeTopicMessage(
            String topic,
            String title,
            String body,
            String target,
            String targetUrl
    ) {
        Notification notification = new Notification(title, body);

        return Message.builder()
                .setTopic(topic)
                .setNotification(notification)
                .putData("target", target)
                .putData("targetUrl", targetUrl)
                .build();
    }
}
