package org.nunocky.myfirebasenoteapp.domain.firebase

import android.content.Context
import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.actionCodeSettings
import dagger.hilt.android.qualifiers.ApplicationContext
import org.nunocky.myfirebasenoteapp.data.ResetPasswordResult
import org.nunocky.myfirebasenoteapp.data.SignInResult
import org.nunocky.myfirebasenoteapp.data.SignUpResult
import org.nunocky.myfirebasenoteapp.data.User
import org.nunocky.myfirebasenoteapp.domain.EmailSignInUseCase
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

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
            // メールリンクによるサインアップ
            // メールには以下のようなリンクが送信される
            // https://myfirebasestudy-b74f9.firebaseapp.com/__/auth/links?link=https://myfirebasestudy-b74f9.firebaseapp.com/__/auth/action?apiKey%3DAIzaSyCrQ5YPqwuA2o1JLkzXbTY80flM_ov32l0%26mode%3DsignIn%26oobCode%3Dptub9UIvDizGdCO9s3pWTd8mZlGhw8FDpTCl_SZyEZUAAAGXh3A4Uw%26continueUrl%3Dhttps://myfirebasestudy-b74f9.firebaseapp.com/finishSignUp%26lang%3Dja
            val actionCodeSettings = actionCodeSettings {
                // HTTPS URLが必要（Firebase要件）
                url = "https://myfirebasestudy-b74f9.firebaseapp.com/finishSignUp"
                handleCodeInApp = true // This must be true
                setAndroidPackageName(
                    "org.nunocky.myfirebasenoteapp",
                    true, // installIfNotAvailable
                    "28", // minimumVersion
                )
            }

            // メールアドレスを保存してからメールリンクを送信
            context.getSharedPreferences("auth", Context.MODE_PRIVATE)
                .edit()
                .putString("email_for_sign_in", email)
                .apply()

            val request =
                FirebaseAuth.getInstance().sendSignInLinkToEmail(email, actionCodeSettings)
            // FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)

            request.addOnSuccessListener {
                Log.d("FirebaseEmailSignInUseCase", "Sign up email sent successfully for: $email")
                continuation.resume(SignUpResult.Success())
            }.addOnFailureListener { exception ->
                // 失敗時は保存したメールを削除
                context.getSharedPreferences("auth", Context.MODE_PRIVATE)
                    .edit()
                    .remove("email_for_sign_in")
                    .apply()
                continuation.resume(SignUpResult.Failed(exception))
            }.addOnCanceledListener {
                // キャンセル時は保存したメールを削除
                context.getSharedPreferences("auth", Context.MODE_PRIVATE)
                    .edit()
                    .remove("email_for_sign_in")
                    .apply()
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