package org.nunocky.myfirebasenoteapp.domain.firebase

import com.google.firebase.auth.FirebaseAuth
import org.nunocky.myfirebasenoteapp.data.SignInResult
import org.nunocky.myfirebasenoteapp.domain.EmailSignInUseCase
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class FirebaseEmailSignInUseCase : EmailSignInUseCase {
    override suspend fun signIn(email: String, password: String): SignInResult {
        return suspendCoroutine { continuation ->
            FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
                .addOnSuccessListener {
                    continuation.resume(SignInResult.Success(it.user!!))
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