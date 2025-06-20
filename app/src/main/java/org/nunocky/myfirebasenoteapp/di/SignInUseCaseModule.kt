package org.nunocky.myfirebasenoteapp.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import org.nunocky.myfirebasenoteapp.network.firebase.FirebaseGoogleAuthUseCase
import org.nunocky.myfirebasenoteapp.usecase.GoogleAuthUseCase

@Module
@InstallIn(SingletonComponent::class)
abstract class SignInUseCaseModule {
    @Binds
    abstract fun bindAuthenticationUseCase(impl: FirebaseGoogleAuthUseCase): GoogleAuthUseCase

//    @Binds
//    abstract fun bindGoogleSignInUseCase(impl: FirebaseGoogleAuthUseCase): GoogleAuthUseCase
//
//    @Binds
//    abstract fun bindEmailSignInUseCase(impl: FirebaseEmailAuthUseCase): EmailAuthUseCase
}

