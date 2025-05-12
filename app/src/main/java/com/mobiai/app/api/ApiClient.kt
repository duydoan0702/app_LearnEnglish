package com.mobiai.app.api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiClient {

    private const val BASE_URL = "https://api.mymemory.translated.net"  // Địa chỉ của dịch vụ LibreTranslate miễn phí

    private val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())  // Sử dụng Gson để chuyển đổi dữ liệu JSON
        .build()

    // Khởi tạo dịch vụ
    val translateService: TranslateService = retrofit.create(TranslateService::class.java)
}
