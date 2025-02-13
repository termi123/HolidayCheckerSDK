package com.example.services.di

import com.example.services.datasource.HolidayCacheDataSource
import com.example.services.datasource.HolidayCacheDataSourceImpl
import com.example.services.datasource.HolidayRemoteDataSource
import com.example.services.datasource.HolidayRemoteDataSourceImpl
import com.example.services.db.HolidayDatabase
import com.example.services.remote.AbstractHolidayService
import com.example.services.remote.CalendarificService
import com.example.services.remote.HolidayApiService
import com.example.services.repository.HolidayRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ServiceModule {

    private fun createRetrofit(baseUrl: String): Retrofit {
        val interceptor = HttpLoggingInterceptor()
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY)

        val okHttpClient = OkHttpClient.Builder().addInterceptor(interceptor).build()

        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun provideCalendarificService(): CalendarificService {
        return createRetrofit("https://calendarific.com/api/").create(CalendarificService::class.java)
    }

    @Provides
    @Singleton
    fun provideAbstractHolidayService(): AbstractHolidayService {
        return createRetrofit("https://holidays.abstractapi.com/").create(AbstractHolidayService::class.java)
    }

    @Provides
    @Singleton
    fun provideHolidayApiService(): HolidayApiService {
        return createRetrofit("https://holidayapi.com/").create(HolidayApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideHolidayRemoteDataSource(
        calendarificService: CalendarificService,
        abstractApiService: AbstractHolidayService,
        holidayApiService: HolidayApiService
    ): HolidayRemoteDataSource {
        return HolidayRemoteDataSourceImpl(
            calendarificService,
            abstractApiService,
            holidayApiService
        )
    }

    @Provides
    @Singleton
    fun provideHolidayCacheDataSource(database: HolidayDatabase): HolidayCacheDataSource {
        return HolidayCacheDataSourceImpl(database)
    }

    @Singleton
    @Provides
    fun provideHolidayRepository(
        remoteDataSource: HolidayRemoteDataSource,
        cacheDataSource: HolidayCacheDataSource,
    ) = HolidayRepository(remoteDataSource, cacheDataSource)
}
