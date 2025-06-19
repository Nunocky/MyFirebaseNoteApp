package org.nunocky.myfirebasenoteapp.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import org.nunocky.myfirebasenoteapp.domain.EmailSignInUseCase
import org.nunocky.myfirebasenoteapp.domain.GoogleSignInUseCase
import org.nunocky.myfirebasenoteapp.domain.firebase.FirebaseEmailSignInUseCase
import org.nunocky.myfirebasenoteapp.domain.firebase.FirebaseGoogleSignInUseCase

@Module
@InstallIn(SingletonComponent::class)
abstract class SignInUseCaseModule {
    @Binds
    abstract fun bindGoogleSignInUseCase(impl: FirebaseGoogleSignInUseCase): GoogleSignInUseCase

    @Binds
    abstract fun bindEmailSignInUseCase(impl: FirebaseEmailSignInUseCase): EmailSignInUseCase
}

