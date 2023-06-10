package iti.mad.marketly.data.repository.categoryProduct

import iti.mad.marketly.data.model.category.CategoryResponse
import iti.mad.marketly.data.model.categoryProduct.CategoryProductResponse
import kotlinx.coroutines.flow.Flow

interface CategoryProductRepo {
    suspend fun getCategoryProduct( categoryID:Long): Flow<CategoryProductResponse>
}