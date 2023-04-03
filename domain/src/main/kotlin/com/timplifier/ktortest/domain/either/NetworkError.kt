package com.timplifier.ktortest.domain.either

sealed class NetworkError {
    class Api(val error: MutableMap<String, List<String>>) : NetworkError()
    class Unexpected(val error: String) : NetworkError()
}