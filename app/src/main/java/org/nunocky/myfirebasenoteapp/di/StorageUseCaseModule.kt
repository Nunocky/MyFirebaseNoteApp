package org.nunocky.myfirebasenoteapp.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import org.nunocky.myfirebasenoteapp.domain.CloudStorageUseCase
import org.nunocky.myfirebasenoteapp.domain.firebase.FireStoreUseCase

@Module
@InstallIn(SingletonComponent::class)
abstract class StorageUseCaseModule {
    @Binds
    abstract fun bindCloudStorageUseCase(impl: FireStoreUseCase): CloudStorageUseCase
}