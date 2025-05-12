package com.mobiai.app.model

data class TranslateRequest(
    val q: String,  // Text cần dịch
    val source: String,  // Ngôn ngữ nguồn
    val target: String   // Ngôn ngữ đích
)
