package org.nunocky.myfirebasetextapp.domain

import org.nunocky.myfirebasetextapp.data.User

interface Authentication {
    fun currentUser(): User?
}

