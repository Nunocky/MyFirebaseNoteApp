package org.nunocky.myfirebasenoteapp.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import org.nunocky.myfirebasenoteapp.network.firebase.FirebaseEmailSignInUseCase
import org.nunocky.myfirebasenoteapp.network.firebase.FirebaseGoogleSignInUseCase
import org.nunocky.myfirebasenoteapp.usecase.EmailSignInUseCase
import org.nunocky.myfirebasenoteapp.usecase.GoogleSignInUseCase

@Module
@InstallIn(SingletonComponent::class)
abstract class SignInUseCaseModule {
    @Binds
    abstract fun bindGoogleSignInUseCase(impl: FirebaseGoogleSignInUseCase): GoogleSignInUseCase

    @Binds
    abstract fun bindEmailSignInUseCase(impl: FirebaseEmailSignInUseCase): EmailSignInUseCase
}

