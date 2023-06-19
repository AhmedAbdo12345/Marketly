package iti.workshop.admin.data.local

import iti.workshop.admin.data.dto.Product
import iti.workshop.admin.data.local.room.ProductDao
import kotlinx.coroutines.flow.Flow

class LocalDataSource(private val productDao:ProductDao):ILocalDataSource {
    override fun getAllProducts(): Flow<MutableList<Product>>  = productDao.getAllProducts()

    override suspend fun insertAllTable(products: List<Product?>?) {
        productDao.insertAllTable(products)
    }
}