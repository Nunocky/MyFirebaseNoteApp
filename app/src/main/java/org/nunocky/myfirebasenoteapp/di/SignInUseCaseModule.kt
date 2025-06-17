package org.nunocky.myfirebasenoteapp.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import org.nunocky.myfirebasenoteapp.domain.firebase.FirebaseGoogleSignInUseCaseImpl
import org.nunocky.myfirebasenoteapp.domain.GoogleSignInUseCase

@Module
@InstallIn(SingletonComponent::class)
abstract class SignInUseCaseModule {
    @Binds
    abstract fun bindGoogleSignInUseCase(impl: FirebaseGoogleSignInUseCaseImpl): GoogleSignInUseCase
}

