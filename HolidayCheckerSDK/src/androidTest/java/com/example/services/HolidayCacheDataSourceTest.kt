package com.example.services

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.services.datasource.HolidayCacheDataSourceImpl
import com.example.services.db.HolidayDao
import com.example.services.db.HolidayDatabase
import com.example.services.db.HolidayEntity
import com.example.services.utils.HolidayCheckMode
import junit.framework.TestCase
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class HolidayCacheDataSourceTest : TestCase() {

//    @get:Rule
//    val instantTaskExecutorRule = InstantTaskExecutorRule() // Allows LiveData execution

    private lateinit var database: HolidayDatabase
    private lateinit var dao: HolidayDao
    private lateinit var cacheDataSource: HolidayCacheDataSourceImpl

    @Before
    fun setup() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        database = Room.inMemoryDatabaseBuilder(context, HolidayDatabase::class.java)
            .allowMainThreadQueries() // Only for testing
            .build()
        dao = database.holidayDao()
        cacheDataSource = HolidayCacheDataSourceImpl(database)
    }

    @After
    fun closeDb() {
        database.close()
    }

    @Test
    fun saveHoliday() = runBlocking {
        val year = 2024
        val month = 12
        val day = 25
        val isHoliday = true
        cacheDataSource.saveHoliday(year, month, day, isHoliday, HolidayCheckMode.ANY)
        val result = cacheDataSource.getCachedHoliday(year, month, day, HolidayCheckMode.ANY)
        assert(result == true)
    }

    @Test
    fun getCachedHoliday() = runBlocking {
        val result = cacheDataSource.getCachedHoliday(2025, 1, 1, HolidayCheckMode.ANY)
        assert(result == null)
    }

    @Test
    fun getCachedHoliday2() = runBlocking {
        val holiday = HolidayEntity(
            year = 2024,
            month = 12,
            day = 25,
            isHoliday = true,
            mode = HolidayCheckMode.ANY
        )
        database.holidayDao().insertHoliday(holiday)
        val result = cacheDataSource.getCachedHoliday(2024, 12, 25, HolidayCheckMode.ANY)
        assert(result == true)
    }
}

