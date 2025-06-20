package org.nunocky.myfirebasenoteapp.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import org.nunocky.myfirebasenoteapp.network.firebase.FireStoreUseCase
import org.nunocky.myfirebasenoteapp.usecase.CloudStorageUseCase

@Module
@InstallIn(SingletonComponent::class)
abstract class StorageUseCaseModule {
    @Binds
    abstract fun bindCloudStorageUseCase(impl: FireStoreUseCase): CloudStorageUseCase
}