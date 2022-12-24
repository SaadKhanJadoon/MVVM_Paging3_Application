package com.saadkhan.paging3.api

import com.saadkhan.paging3.model.ResponseApi
import com.saadkhan.paging3.utils.Constants
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {

    @GET(Constants.END_POINT)
    suspend fun getAllCharacters(
        @Query("page") page: Int
    ): Response<ResponseApi>
}