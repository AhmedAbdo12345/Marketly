package iti.mad.marketly.data.repository.brands

import iti.mad.marketly.data.model.brands.BrandsResponse
import iti.mad.marketly.data.source.remote.FakeRemoteDataSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FakeBrandsRepo(val remoteDataSource: FakeRemoteDataSource): BrandsRepo {
    override suspend fun getBrands(): Flow<BrandsResponse> {
        return remoteDataSource.getBrands()

    }
}