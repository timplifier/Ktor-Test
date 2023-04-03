package com.timplifier.ktortest.data.base

import com.google.gson.annotations.SerializedName

class BasePagingResponse<T>(
    @SerializedName("prev")
    val prev: Int?,
    @SerializedName("next")
    val next: Int?,
    @SerializedName("data")
    val data: MutableList<T>
)