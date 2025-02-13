package com.example.services.remote

import com.example.services.entities.Holiday
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface AbstractHolidayService {
    @GET("v1/")
    suspend fun getHolidays(
        @Query("api_key") apiKey: String,
        @Query("country") country: String,
        @Query("year") year: Int,
        @Query("month") month: Int,
        @Query("day") day: Int
    ): Response<AbstractApiResponse>
}

typealias AbstractApiResponse = List<AbstractHoliday>

data class AbstractHoliday(
    val name: String,
    val date: String
)

fun AbstractApiResponse?.toStandardHolidays(): List<Holiday>? {
    return this?.map { Holiday(it.name, it.date) }
}