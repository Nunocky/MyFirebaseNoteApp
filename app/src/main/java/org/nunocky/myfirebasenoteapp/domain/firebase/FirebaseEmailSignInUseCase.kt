package org.nunocky.myfirebasenoteapp.domain.firebase

import com.google.firebase.auth.FirebaseAuth
import org.nunocky.myfirebasenoteapp.data.SignInResult
import org.nunocky.myfirebasenoteapp.data.User
import org.nunocky.myfirebasenoteapp.domain.EmailSignInUseCase
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class FirebaseEmailSignInUseCase : EmailSignInUseCase {
    override suspend fun signIn(email: String, password: String): SignInResult {
        return suspendCoroutine { continuation ->
            FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
                .addOnSuccessListener {
                    it.user?.let {
                        continuation.resume(
                            SignInResult.Success(
                                User(
                                    it.uid,
                                    it.displayName ?: "",
                                    it.email ?: "",
                                    it.photoUrl.toString()
                                )
                            )
                        )
                    } ?: continuation.resume(
                        SignInResult.Failed(Exception("User is null"))
                    )
                }
                .addOnFailureListener {
                    continuation.resume(SignInResult.Failed(it))
                }
                .addOnCanceledListener {
                    continuation.resume(SignInResult.Cancelled)
                }
        }
    }
}