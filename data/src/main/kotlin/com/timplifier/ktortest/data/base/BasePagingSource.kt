package com.timplifier.ktortest.data.base

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.timplifier.ktortest.data.utils.DataMapper
import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException

private const val BASE_STARTING_PAGE_INDEX = 1

abstract class BasePagingSource<ValueDto : DataMapper<Value>, Value : Any>(
    private val request: suspend (position: Int) -> Response<BasePagingResponse<ValueDto>>,
) : PagingSource<Int, Value>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Value> {
        val position = params.key ?: BASE_STARTING_PAGE_INDEX

        return try {
            val response = request(position)
            val data = response.body()!!

            LoadResult.Page(
                data = data.data.map { it.toDomain() },
                prevKey = null,
                nextKey = data.next
            )
        } catch (ioException: IOException) {
            LoadResult.Error(ioException)
        } catch (httpException: HttpException) {
            LoadResult.Error(httpException)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, Value>) =
        state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
}