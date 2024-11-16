package com.esoft.emobile.di


import com.esoft.emobile.BuildConfig
import com.esoft.emobile.data.remote.TagEndpoint
import com.esoft.emobile.data.remote.UnitEndpoint
import com.esoft.emobile.data.remote.VehicleEndpoint
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    private const val BASE_URL: String = BuildConfig.BASE_URL

    @Singleton
    @Provides
    fun provideOkHttpClient() = if (BuildConfig.DEBUG) {
        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
        OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .build()
    } else {
        OkHttpClient
            .Builder()
            .build()
    }

    @Singleton
    @Provides
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit = Retrofit.Builder()
        .addConverterFactory(MoshiConverterFactory.create())
        .baseUrl(BASE_URL)
        .client(okHttpClient)
        .build()

    @Provides
    @Singleton
    fun provideApiVehicleService(retrofit: Retrofit): VehicleEndpoint =
        retrofit.create(VehicleEndpoint::class.java)

    @Provides
    @Singleton
    fun provideUnitService(retrofit: Retrofit): UnitEndpoint =
        retrofit.create(UnitEndpoint::class.java)

    @Provides
    @Singleton
    fun provideTagService(retrofit: Retrofit): TagEndpoint =
        retrofit.create(TagEndpoint::class.java)

}