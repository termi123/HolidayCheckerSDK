package com.example.services.remote

import com.example.services.entities.Holiday
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface HolidayApiService {
    @GET("v1/holidays")
    suspend fun getHolidays(
        @Query("key") apiKey: String,
        @Query("country") country: String,
        @Query("year") year: Int,
        @Query("month") month: Int,
        @Query("day") day: Int
    ): Response<HolidayApiResponse>
}

data class HolidayApiResponse(val holidays: List<HolidayApiHoliday>)
data class HolidayApiHoliday(val name: String, val date: String)
//data class HolidayApiDate(val iso: String)

fun HolidayApiResponse?.toStandardHolidays(): List<Holiday>? {
    return this?.holidays?.map { Holiday(it.name, it.date) }
}