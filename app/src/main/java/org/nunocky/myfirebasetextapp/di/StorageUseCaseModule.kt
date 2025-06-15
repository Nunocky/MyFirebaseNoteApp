package org.nunocky.myfirebasetextapp.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import org.nunocky.myfirebasetextapp.domain.CloudStorageUseCase
import org.nunocky.myfirebasetextapp.domain.FireStoreUseCase

@Module
@InstallIn(SingletonComponent::class)
abstract class StorageUseCaseModule {
    @Binds
    abstract fun bindCloudStorageUseCase(impl: FireStoreUseCase): CloudStorageUseCase
}