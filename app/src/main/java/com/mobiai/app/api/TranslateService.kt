package com.mobiai.app.api  // Đảm bảo package là com.mobiai.app.api

import com.mobiai.app.model.TranslateRequest
import com.mobiai.app.model.TranslateResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface TranslateService {
    @POST("/translate")
    fun translateText(@Body request: TranslateRequest): Call<TranslateResponse>
}
