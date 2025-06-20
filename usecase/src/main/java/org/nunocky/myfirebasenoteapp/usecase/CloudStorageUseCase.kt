package org.nunocky.myfirebasenoteapp.usecase

import org.nunocky.myfirebasenoteapp.data.User

interface CloudStorageUseCase {
    suspend fun registerUser(user: User)

    suspend fun getItemList(): List<Pair<String, String>>

    suspend fun createNewItem(title: String, content: String): String

    suspend fun getItem(itemId: String): Pair<String, String>

    suspend fun updateItem(itemId: String, title: String, content: String)

    suspend fun deleteItem(itemId: String)
}

