package org.nunocky.myfirebasenoteapp.network.firebase

import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import org.nunocky.myfirebasenoteapp.data.User
import org.nunocky.myfirebasenoteapp.usecase.Authentication
import javax.inject.Inject

class FirebaseAuthentication @Inject constructor(
    private val googleSignInUseCase: FirebaseGoogleAuthUseCase,
    private val emailSignInUseCase: FirebaseEmailAuthUseCase
) : Authentication {
    override fun currentUser() = if (Firebase.auth.currentUser == null) null else User(
        uid = Firebase.auth.currentUser?.uid ?: "",
        displayName = Firebase.auth.currentUser?.displayName,
        email = Firebase.auth.currentUser?.email,
        photoUrl = Firebase.auth.currentUser?.photoUrl?.toString(),
        emailVerified = Firebase.auth.currentUser?.isEmailVerified ?: false
    )

    override fun signOut() {
        Firebase.auth.signOut()
    }

    override suspend fun googleSignIn(googleClientId: String) =
        googleSignInUseCase.signIn(googleClientId)

    override suspend fun emailSignIn(email: String, password: String) =
        emailSignInUseCase.signIn(email, password)

    override suspend fun emailSignUp(email: String, password: String) =
        emailSignInUseCase.signUp(email, password)

    override suspend fun resetPassword(email: String) =
        emailSignInUseCase.resetPassword(email)
}