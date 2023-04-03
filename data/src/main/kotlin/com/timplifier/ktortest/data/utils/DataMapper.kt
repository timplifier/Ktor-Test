package com.timplifier.ktortest.data.utils

interface DataMapper<T> {
    fun toDomain(): T
}