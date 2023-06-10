package iti.mad.marketly.data.repository.categoryProduct

import iti.mad.marketly.data.model.category.CategoryResponse
import iti.mad.marketly.data.model.categoryProduct.CategoryProductResponse
import iti.mad.marketly.data.source.remote.retrofit.ApiService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class CategoryProductRepoImpl(val apiService: ApiService): CategoryProductRepo {

    override suspend fun getCategoryProduct(categoryID:Long): Flow<CategoryProductResponse> = flow{
        emit(apiService.getCategoryProducts(categoryID))
    }
}