package com.example.services.di

import android.content.Context
import androidx.room.Room
import com.example.services.db.HolidayDao
import com.example.services.db.HolidayDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): HolidayDatabase {
        return Room.databaseBuilder(context, HolidayDatabase::class.java, "holiday_database")
            .build()
    }

    @Provides
    fun provideHolidayDao(database: HolidayDatabase): HolidayDao {
        return database.holidayDao()
    }
}