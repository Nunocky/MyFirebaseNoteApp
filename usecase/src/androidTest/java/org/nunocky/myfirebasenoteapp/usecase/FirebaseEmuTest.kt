package org.nunocky.myfirebasenoteapp.usecase

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.android.gms.tasks.Tasks
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class FirebaseEmuTest {

    private lateinit var firestore: FirebaseFirestore
    private lateinit var auth: FirebaseAuth

    @Before
    fun setUp() {
        auth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()
    }

    @After
    fun tearDown() {
        // Clear auth state
        if (::auth.isInitialized) {
            auth.signOut()
        }
    }

    @Test
    fun testFirebaseEmulatorConnection() = runTest {
        // Test that Firebase emulators are properly connected
        assert(auth.currentUser == null)

        // Test Firestore connection by attempting to read from a test collection
        val testCollection = firestore.collection("test")
        val snapshot = Tasks.await(testCollection.get())

        // If we get here without exception, emulator connection is working
        assert(snapshot != null)
    }

    @Test
    fun testAuthEmulator() = runTest {
        // Test Firebase Auth emulator functionality
        val testEmail = "test@example.com"
        val testPassword = "testPassword123"

        try {
            // Create test user
            val result = Tasks.await(auth.createUserWithEmailAndPassword(testEmail, testPassword))
            assert(result.user != null)

            // Sign out
            auth.signOut()
            assert(auth.currentUser == null)

            // Sign in
            val signInResult = Tasks.await(auth.signInWithEmailAndPassword(testEmail, testPassword))
            assert(signInResult.user != null)
            assert(auth.currentUser != null)

        } catch (e: Exception) {
            // In case of any error, fail the test
            throw AssertionError("Auth emulator test failed: ${e.message}")
        }
    }

    @Test
    fun testFirestoreEmulator() = runTest {
        // Test Firestore emulator functionality
        val testCollection = firestore.collection("testCollection")
        val testDocument = testCollection.document("testDoc")

        val testData = mapOf(
            "name" to "Test Name",
            "value" to 123,
            "timestamp" to System.currentTimeMillis()
        )

        try {
            // Write test data
            Tasks.await(testDocument.set(testData))

            // Read test data
            val snapshot = Tasks.await(testDocument.get())
            val retrievedData = snapshot.data

            assert(retrievedData != null)
            assert(retrievedData!!["name"] == "Test Name")
            assert(retrievedData["value"] == 123L)

        } catch (e: Exception) {
            throw AssertionError("Firestore emulator test failed: ${e.message}")
        }
    }
}