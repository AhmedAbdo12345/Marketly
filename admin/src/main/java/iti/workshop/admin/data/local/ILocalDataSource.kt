package iti.workshop.admin.data.local

import iti.workshop.admin.data.dto.Product
import kotlinx.coroutines.flow.Flow

interface ILocalDataSource {
    fun getAllProducts(): Flow<MutableList<Product>>

    suspend fun insertAllTable(products: List<Product?>?)

}