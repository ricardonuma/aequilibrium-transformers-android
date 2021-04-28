package com.aequilibrium.transformers.di.modules

import android.app.Application
import android.content.SharedPreferences
import androidx.preference.PreferenceManager
import com.aequilibrium.transformers.auth.ApplicationHeadersInterceptor
import com.aequilibrium.transformers.auth.SessionManager
import com.aequilibrium.transformers.utils.NetworkConnectionInterceptor
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import javax.inject.Singleton


@InstallIn(ApplicationComponent::class)
@Module
class AppModule {

    @Provides
    @Singleton
    fun provideSessionManager(
        appPreferences: SharedPreferences
    ): SessionManager = SessionManager(appPreferences)

    @Provides
    @Singleton
    fun provideSharedPreferences(app: Application): SharedPreferences =
        PreferenceManager.getDefaultSharedPreferences(app)

    @Provides
    @Singleton
    fun providesConnectivityInterceptor(
        app: Application
    ): NetworkConnectionInterceptor = NetworkConnectionInterceptor(app.applicationContext)

    @Provides
    @Singleton
    fun providesApplicationHeadersInterceptor(
        cryptoSessionManager: SessionManager
    ): ApplicationHeadersInterceptor = ApplicationHeadersInterceptor(cryptoSessionManager)
}