package iti.mad.marketly.data.repository.category

import iti.mad.marketly.data.model.category.CategoryResponse
import iti.mad.marketly.data.source.remote.FakeRemoteDataSource
import kotlinx.coroutines.flow.Flow

class FakeCategoryRepo(val remoteDataSource: FakeRemoteDataSource):CategoryRepo {
    override suspend fun getCategory(): Flow<CategoryResponse> {
return  remoteDataSource.getCategory()
    }
}