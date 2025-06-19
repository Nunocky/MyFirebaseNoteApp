package org.nunocky.myfirebasenoteapp.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import org.nunocky.myfirebasenoteapp.domain.Authentication
import org.nunocky.myfirebasenoteapp.domain.firebase.FirebaseAuthentication

@Module
@InstallIn(SingletonComponent::class)
abstract class AuthenticationModule {
    @Binds
    abstract fun bindFirebaseAuthentication(impl: FirebaseAuthentication): Authentication
}
