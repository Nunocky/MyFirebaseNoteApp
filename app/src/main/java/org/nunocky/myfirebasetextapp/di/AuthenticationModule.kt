package org.nunocky.myfirebasetextapp.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import org.nunocky.myfirebasetextapp.domain.Authentication
import org.nunocky.myfirebasetextapp.domain.FirebaseAuthenticationImpl

@Module
@InstallIn(SingletonComponent::class)
abstract class AuthenticationModule {
    @Binds
    abstract fun bindFirebaseAuthentication(impl: FirebaseAuthenticationImpl): Authentication
}
