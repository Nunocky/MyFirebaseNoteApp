package org.nunocky.myfirebasetextapp.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import org.nunocky.myfirebasetextapp.domain.FirebaseGoogleSignInUseCaseImpl
import org.nunocky.myfirebasetextapp.domain.GoogleSignInUseCase

@Module
@InstallIn(SingletonComponent::class)
abstract class SignInUseCaseModule {
    @Binds
    abstract fun bindGoogleSignInUseCase(impl: FirebaseGoogleSignInUseCaseImpl): GoogleSignInUseCase
}

