package org.nunocky.myfirebasetextapp.domain

import com.google.firebase.auth.FirebaseUser

interface CloudStorageUseCase {
    fun registerUser(user: FirebaseUser)

    fun getItemList(
        onSuccess: (List<Pair<String, String>>) -> Unit,
        onError: (Throwable) -> Unit = {}
    )

    fun createNewItem(
        title: String,
        content: String,
        onSuccess: (String) -> Unit = {},
        onError: (Throwable) -> Unit = {}
    )

    fun getItem(
        itemId: String,
        onSuccess: (title: String, content: String) -> Unit,
        onError: (Throwable) -> Unit = {}
    )

    fun updateItem(
        itemId: String,
        title: String,
        content: String,
        onSuccess: () -> Unit = {},
        onError: (Throwable) -> Unit = {}
    )

    fun deleteItem(
        itemId: String,
        onSuccess: () -> Unit = {},
        onError: (Throwable) -> Unit = {}
    )
}

