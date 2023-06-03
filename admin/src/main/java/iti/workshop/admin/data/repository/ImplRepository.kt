package iti.workshop.admin.data.repository

import iti.workshop.admin.data.remote.IRemoteDataSource

class ImplRepository(private val _remote:IRemoteDataSource):IProductRepository,ICouponRepository,IInventoryRepository {

}