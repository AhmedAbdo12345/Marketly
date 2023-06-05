package iti.mad.marketly.data.repository.category

import iti.mad.marketly.data.model.category.CategoryResponse
import iti.mad.marketly.data.source.remote.retrofit.ApiService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class CategoryRepoImpl(val apiService: ApiService): CategoryRepo {
    override suspend fun getCategory(): Flow<CategoryResponse> = flow{
        emit(apiService.getCategoryFromAPI())
    }
}