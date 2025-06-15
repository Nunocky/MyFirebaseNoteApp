package org.nunocky.myfirebasetextapp.domain

import com.google.firebase.auth.FirebaseUser

interface CloudStorageUseCase {
    fun registerUser(user: FirebaseUser)
}

