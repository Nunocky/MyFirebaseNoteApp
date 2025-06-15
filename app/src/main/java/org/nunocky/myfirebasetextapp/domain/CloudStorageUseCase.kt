package org.nunocky.myfirebasetextapp.domain

import com.google.firebase.auth.FirebaseUser

interface CloudStorageUseCase {
    suspend fun registerUser(user: FirebaseUser)

    suspend fun getItemList(): List<Pair<String, String>>

    suspend fun createNewItem(title: String, content: String): String

    suspend fun getItem(itemId: String): Pair<String, String>

    suspend fun updateItem(itemId: String, title: String, content: String)

    suspend fun deleteItem(itemId: String)
}

