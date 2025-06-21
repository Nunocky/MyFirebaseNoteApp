package org.nunocky.usecase

import android.app.Application
import android.content.Context
import androidx.test.runner.AndroidJUnitRunner
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreSettings
import dagger.hilt.android.testing.HiltTestApplication

class HiltTestRunner : AndroidJUnitRunner() {
    override fun newApplication(
        cl: ClassLoader?,
        className: String?,
        context: Context?
    ): Application {
        return super.newApplication(cl, HiltTestApplication::class.java.name, context)
    }

    override fun onCreate(arguments: android.os.Bundle?) {
        super.onCreate(arguments)

        // Initialize Firebase for testing
        try {
            if (FirebaseApp.getApps(context).isEmpty()) {
                val options = com.google.firebase.FirebaseOptions.Builder()
                    .setApplicationId("1:123456789:android:abcdef")
                    .setApiKey("test-api-key")
                    .setProjectId("test-project")
                    .build()

                FirebaseApp.initializeApp(context, options)
            }
        } catch (e: Exception) {
            // If initialization fails, try with default configuration
            try {
                FirebaseApp.initializeApp(context)
            } catch (ex: Exception) {
                // Create a test Firebase app
                val options = com.google.firebase.FirebaseOptions.Builder()
                    .setApplicationId("1:123456789:android:abcdef")
                    .setApiKey("test-api-key")
                    .setProjectId("test-project")
                    .build()

                FirebaseApp.initializeApp(context, options, "testApp")
            }
        }

        val auth = FirebaseAuth.getInstance()
        val firestore = FirebaseFirestore.getInstance()

        // Use emulator if not already configured
//        try {
        auth.useEmulator("10.0.2.2", 9099)
//        } catch (e: IllegalStateException) {
//             Already configured, ignore
//        }

//        try {
        firestore.useEmulator("10.0.2.2", 8080)
//        } catch (e: IllegalStateException) {
//             Already configured, ignore
//        }

        // Configure Firestore settings for emulator
        val settings = FirebaseFirestoreSettings.Builder()
            .build()
        firestore.firestoreSettings = settings
    }
}