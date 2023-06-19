package iti.mad.marketly.data.repository.category

import iti.mad.marketly.data.model.category.CategoryResponse
import iti.mad.marketly.data.source.remote.IRemoteDataSource
import kotlinx.coroutines.flow.Flow

class CategoryRepoImpl(val remote: IRemoteDataSource): CategoryRepo {
    override suspend fun getCategory(): Flow<CategoryResponse> = remote.getCategory()
}