package com.obcompany.advancedandroid.database

@Suppress("unused")
data class DbResponse<out T>(
    val data: T?,
    val error: Throwable?
)