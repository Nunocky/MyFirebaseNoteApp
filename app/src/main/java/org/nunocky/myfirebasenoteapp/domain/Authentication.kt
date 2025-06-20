package org.nunocky.myfirebasenoteapp.domain

import org.nunocky.myfirebasenoteapp.data.User

interface Authentication {
    fun currentUser(): User?
    fun signOut()
}

