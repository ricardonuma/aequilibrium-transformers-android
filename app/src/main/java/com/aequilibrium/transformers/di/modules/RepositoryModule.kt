package com.aequilibrium.transformers.di.modules

import com.aequilibrium.transformers.data.source.ITokenRepository
import com.aequilibrium.transformers.data.source.ITransformerRepository
import com.aequilibrium.transformers.data.source.TokenRepository
import com.aequilibrium.transformers.data.source.TransformerRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent


@Module
@InstallIn(ApplicationComponent::class)
abstract class RepositoryModule {

    @Binds
    abstract fun bindTokenRepository(
        tokenRepository: TokenRepository
    ): ITokenRepository

    @Binds
    abstract fun bindTransformerRepository(
        transformerRepository: TransformerRepository
    ): ITransformerRepository
}