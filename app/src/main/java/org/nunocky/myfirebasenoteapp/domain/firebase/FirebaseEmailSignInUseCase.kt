package org.nunocky.myfirebasenoteapp.domain.firebase

import com.google.firebase.auth.FirebaseAuth
import org.nunocky.myfirebasenoteapp.data.ResetPasswordResult
import org.nunocky.myfirebasenoteapp.data.SignInResult
import org.nunocky.myfirebasenoteapp.data.SignUpResult
import org.nunocky.myfirebasenoteapp.data.User
import org.nunocky.myfirebasenoteapp.domain.EmailSignInUseCase
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class FirebaseEmailSignInUseCase @Inject constructor() : EmailSignInUseCase {
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

    override suspend fun createAccount(email: String, password: String): SignUpResult {
        return suspendCoroutine { continuation ->

            // TODO メールリンクによるサインアップにしたい
//            val actionCodeSettings = actionCodeSettings {
//                // URL you want to redirect back to. The domain (www.example.com) for this
//                // URL must be whitelisted in the Firebase Console.
//                url = "https://www.example.com/finishSignUp?cartId=1234"
//                // This must be true
//                handleCodeInApp = true
//                // setIOSBundleId("com.example.ios")
//                setAndroidPackageName(
//                    "org.nunocky.myfirebasenoteapp",
//                    true, // installIfNotAvailable
//                    "28", // minimumVersion
//                )
//            }

            val request =
//                FirebaseAuth.getInstance().sendSignInLinkToEmail(email, actionCodeSettings)
                FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)

            request.addOnSuccessListener {
                continuation.resume(SignUpResult.Success())
            }.addOnFailureListener { exception ->
                continuation.resume(SignUpResult.Failed(exception))
            }.addOnCanceledListener {
                continuation.resume(SignUpResult.Failed(Exception("Sign up cancelled")))
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