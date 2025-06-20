package org.nunocky.myfirebasenoteapp.domain.firebase

import android.content.Context
import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.qualifiers.ApplicationContext
import org.nunocky.myfirebasenoteapp.data.ResetPasswordResult
import org.nunocky.myfirebasenoteapp.data.SignInResult
import org.nunocky.myfirebasenoteapp.data.SignUpResult
import org.nunocky.myfirebasenoteapp.data.User
import org.nunocky.myfirebasenoteapp.domain.EmailSignInUseCase
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

private const val TAG = "FirebaseEmailSignInUseCase"

class FirebaseEmailSignInUseCase @Inject constructor(
    @ApplicationContext private val context: Context
) : EmailSignInUseCase {
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
                                    it.photoUrl.toString(),
                                    it.isEmailVerified
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

    override suspend fun signUp(email: String, password: String): SignUpResult {
        return suspendCoroutine { continuation ->
            val auth = FirebaseAuth.getInstance()

            Log.d(TAG, "signUp: email=$email, password=[REDACTED]")
            auth.createUserWithEmailAndPassword(email, password)
                .addOnSuccessListener {
                    Log.d(TAG, "signUp: success")
                    val user = auth.currentUser
                    if (user == null) {
                        Log.e(TAG, "signUp: user is null")
                        continuation.resume(SignUpResult.Failed(Exception("User is null")))
                        return@addOnSuccessListener
                    }

                    user.sendEmailVerification()
                        .addOnSuccessListener {
                            Log.d(TAG, "signUp: email verification sent")
                            continuation.resume(
                                SignUpResult.Success(
                                    User(
                                        user.uid,
                                        user.displayName ?: "",
                                        user.email ?: "",
                                        user.photoUrl?.toString() ?: "",
                                        user.isEmailVerified
                                    )
                                )
                            )
                        }
                        .addOnFailureListener { e ->
                            Log.e(TAG, "signUp: failed to send email verification", e)
                            continuation.resume(SignUpResult.Failed(e))
                        }
                        .addOnCanceledListener {
                            Log.w(TAG, "signUp: email verification cancelled")
                            continuation.resume(SignUpResult.Failed(Exception("Email verification cancelled")))
                        }
                }
                .addOnFailureListener { e ->
                    Log.e(TAG, "signUp: failed to create user", e)
                    continuation.resume(SignUpResult.Failed(e))
                }
        }
    }

    override suspend fun resetPassword(email: String): ResetPasswordResult {
        return suspendCoroutine { continuation ->
            val request = FirebaseAuth.getInstance().sendPasswordResetEmail(email)

            request.addOnSuccessListener {
                continuation.resume(ResetPasswordResult.Success())
            }.addOnFailureListener { exception ->
                continuation.resume(ResetPasswordResult.Failed(exception))
            }.addOnCanceledListener {
                continuation.resume(ResetPasswordResult.Failed(Exception("Reset password cancelled")))
            }
        }
    }
}