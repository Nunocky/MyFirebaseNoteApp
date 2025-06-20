package org.nunocky.myfirebasenoteapp.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import org.nunocky.myfirebasenoteapp.network.firebase.FirebaseAuthentication
import org.nunocky.myfirebasenoteapp.usecase.Authentication

@Module
@InstallIn(SingletonComponent::class)
abstract class AuthenticationModule {
    @Binds
    abstract fun bindFirebaseAuthentication(impl: FirebaseAuthentication): Authentication
}
