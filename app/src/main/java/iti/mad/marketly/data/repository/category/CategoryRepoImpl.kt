package iti.mad.marketly.data.repository.category

import iti.mad.marketly.data.model.category.CategoryResponse
import iti.mad.marketly.data.source.remote.retrofit.CallApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class CategoryRepoImpl(val callApi: CallApi): CategoryRepo {
    override suspend fun getCategory(): Flow<CategoryResponse> = flow{
        emit(callApi.getCategoryFromAPI())
    }
}