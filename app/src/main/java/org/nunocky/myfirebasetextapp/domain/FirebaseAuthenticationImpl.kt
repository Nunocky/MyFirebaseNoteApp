package org.nunocky.myfirebasetextapp.domain

import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import org.nunocky.myfirebasetextapp.data.User
import javax.inject.Inject

class FirebaseAuthenticationImpl @Inject constructor() : Authentication {
    override fun currentUser() = if (Firebase.auth.currentUser == null) null else User(
        uid = Firebase.auth.currentUser?.uid ?: "",
        displayName = Firebase.auth.currentUser?.displayName,
        email = Firebase.auth.currentUser?.email,
        photoUrl = Firebase.auth.currentUser?.photoUrl?.toString()
    )
}