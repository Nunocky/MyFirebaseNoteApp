package org.nunocky.myfirebasenoteapp.network.firebase

import android.app.Application
import android.util.Log
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import com.google.android.libraries.identity.googleid.GetSignInWithGoogleOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.coroutines.tasks.await
import org.nunocky.myfirebasenoteapp.data.SignInResult
import org.nunocky.myfirebasenoteapp.data.User
import org.nunocky.myfirebasenoteapp.usecase.GoogleAuthUseCase
import javax.inject.Inject

/**
 * Google Sign In
 * very thanks to https://qiita.com/kisayama/items/5dc7618b76f6d86a6d55
 */
class FirebaseGoogleAuthUseCase @Inject constructor(
    private val application: Application
) : GoogleAuthUseCase {

    private fun getSignInRequest(googleClientId: String): GetCredentialRequest {
        val googleIdOption: GetSignInWithGoogleOption = GetSignInWithGoogleOption.Builder(
            googleClientId
        ).build()

        return GetCredentialRequest.Builder()
            .addCredentialOption(googleIdOption)
            .build()
    }

    override suspend fun signIn(googleClientId: String): SignInResult {
        return try {
            val auth: FirebaseAuth = FirebaseAuth.getInstance()
            val credentialManager = CredentialManager.create(application)

            val request = getSignInRequest(googleClientId)

            val credential = try {
                val result =
                    credentialManager.getCredential(context = application, request = request)
                result.credential
            } catch (e: Exception) {
                throw e
            }

            if (credential is CustomCredential && credential.type == GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL) {
                val googleIdTokenCredential = GoogleIdTokenCredential.createFrom(credential.data)
                val googleIdToken = googleIdTokenCredential.idToken
                val firebaseCredential = GoogleAuthProvider.getCredential(googleIdToken, null)
                val authResult = auth.signInWithCredential(firebaseCredential).await()
                val firebaseUser = authResult.user

                if (firebaseUser != null) {
                    SignInResult.Success(
                        User(
                            uid = firebaseUser.uid,
                            displayName = firebaseUser.displayName,
                            email = firebaseUser.email,
                            photoUrl = firebaseUser.photoUrl?.toString(),
                            emailVerified = firebaseUser.isEmailVerified
                        )
                    )
                } else {
                    SignInResult.Failed(Exception("Firebase user is null"))
                }
            } else {
                SignInResult.Failed(Exception("account not found"))
            }
        } catch (e: Exception) {
            Log.d("Firebase", "signIn failed", e)
            SignInResult.Failed(e)
        }
    }
}