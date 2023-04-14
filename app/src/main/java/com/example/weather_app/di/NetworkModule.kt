package com.example.weather_app.di

import com.example.weather_app.data.data_source.RemoteDataSource
import com.example.weather_app.network.APIService
import com.example.weather_app.network.CitiesAPIService
import com.example.weather_app.network.HttpRequestInterceptor
import com.example.weather_app.utils.Constants.API_CITIES
import com.example.weather_app.utils.Constants.API_FORECAST
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Named

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    fun provideOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(HttpRequestInterceptor())
            .connectTimeout(10, TimeUnit.SECONDS)
            .readTimeout(10, TimeUnit.SECONDS)
            .writeTimeout(10, TimeUnit.SECONDS)
            .build()
    }

    @Provides
    fun provideMoshi(): Moshi {
        return Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .build()
    }

    @Provides
    @Named("Forecast")
    fun provideForecastRetrofit(okHttpClient: OkHttpClient, moshi: Moshi): Retrofit {

        return Retrofit.Builder()
            .client(okHttpClient)
            .baseUrl(API_FORECAST)
            .addConverterFactory(
                MoshiConverterFactory.create(
                    moshi
                )
            )
            .addCallAdapterFactory(CoroutineCallAdapterFactory())
            .build()
    }

    @Provides
    @Named("Cities")
    fun provideRetrofitCities(moshi: Moshi): Retrofit {

        return Retrofit.Builder()
            .baseUrl(API_CITIES)
            .addConverterFactory(
                MoshiConverterFactory.create(
                    moshi
                )
            )
            .addCallAdapterFactory(CoroutineCallAdapterFactory())
            .build()
    }

    @Provides
    fun provideForecastService(@Named("Forecast") retrofit: Retrofit): APIService {
        return retrofit.create(APIService::class.java)
    }

    @Provides
    fun provideCitiesService(@Named("Cities") retrofit: Retrofit): CitiesAPIService {
        return retrofit.create(CitiesAPIService::class.java)
    }

    @Provides
    fun provideApiClient(
        apiService: APIService,
        citiesAPIService: CitiesAPIService
    ): RemoteDataSource {
        return RemoteDataSource(apiService, citiesAPIService)
    }
}