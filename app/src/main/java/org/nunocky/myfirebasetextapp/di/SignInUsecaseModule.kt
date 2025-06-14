package org.nunocky.myfirebasetextapp.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import org.nunocky.myfirebasetextapp.domain.GoogleSignInUseCase
import org.nunocky.myfirebasetextapp.domain.GoogleSignInUseCaseImpl

@Module
@InstallIn(SingletonComponent::class)
abstract class SignInUsecaseModule {
    @Binds
    abstract fun bindGoogleSignInUseCase(impl: GoogleSignInUseCaseImpl): GoogleSignInUseCase
}