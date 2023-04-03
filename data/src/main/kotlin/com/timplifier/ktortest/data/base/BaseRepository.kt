package com.timplifier.ktortest.data.base

import androidx.paging.Pager
import androidx.paging.PagingConfig
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.timplifier.ktortest.domain.either.Either
import com.timplifier.ktortest.domain.either.NetworkError
import com.timplifier.ktortest.data.utils.DataMapper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import okhttp3.ResponseBody
import retrofit2.Response

internal fun <T : DataMapper<S>, S> makeNetworkRequest(
    request: suspend () -> Response<T>
) = flow<Either<NetworkError, S>> {
    request().let {
        if (it.isSuccessful && it.body() != null) {
            emit(Either.Right(it.body()!!.toDomain()))
        } else {
            emit(Either.Left(NetworkError.Api(it.errorBody().toApiError())))
        }
    }
}.flowOn(Dispatchers.IO).catch { exception ->
    emit(
        Either.Left(NetworkError.Unexpected(exception.localizedMessage ?: "Error Occurred!"))
    )
}

internal fun <T> makeNetworkRequestNoNetworkError(
    gatherIfSucceed: ((T) -> Unit)? = null,
    request: suspend () -> T
) =
    flow<Either<String, T>> {
        request().also {
            gatherIfSucceed?.invoke(it)
            emit(Either.Right(value = it))
        }
    }.flowOn(Dispatchers.IO).catch { exception ->
        emit(Either.Left(value = exception.localizedMessage ?: "Error Occurred!"))
    }

private fun ResponseBody?.toApiError(): MutableMap<String, List<String>> {
    return Gson().fromJson(
        this?.string(),
        object : TypeToken<MutableMap<String, List<String>>>() {}.type
    )
}

internal inline fun <T : Response<S>, S> T.onSuccess(block: (S) -> Unit): T {
    this.body()?.let(block)
    return this
}

/**
 * Do network paging request with default params
 */
internal fun <ValueDto : DataMapper<Value>, Value : Any> makePagingRequest(
    pagingSource: BasePagingSource<ValueDto, Value>,
    pageSize: Int = 10,
    prefetchDistance: Int = pageSize,
    enablePlaceholders: Boolean = true,
    initialLoadSize: Int = pageSize * 3,
    maxSize: Int = Int.MAX_VALUE,
    jumpThreshold: Int = Int.MIN_VALUE
) = Pager(
    config = PagingConfig(
        pageSize,
        prefetchDistance,
        enablePlaceholders,
        initialLoadSize,
        maxSize,
        jumpThreshold
    ),
    pagingSourceFactory = {
        pagingSource
    }
).flow