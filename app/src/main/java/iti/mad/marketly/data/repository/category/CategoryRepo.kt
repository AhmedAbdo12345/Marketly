package iti.mad.marketly.data.repository.category

import iti.mad.marketly.data.model.category.CategoryResponse
import kotlinx.coroutines.flow.Flow

interface CategoryRepo {
    suspend fun getCategory(): Flow<CategoryResponse>
}