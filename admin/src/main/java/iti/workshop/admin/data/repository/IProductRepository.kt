package iti.workshop.admin.data.repository

import iti.workshop.admin.data.dto.*
import retrofit2.Response

interface IProductRepository {

    suspend fun getProduct(): Response<SuccessProductResponse>

    suspend fun addProduct(data: PostProduct): Response<Product>

    suspend fun updateProduct(id:Long, data: UpdateProduct): Response<Product>

    suspend fun deleteProduct(id:Long): Response<Void>

    suspend fun getCount(): Response<Count>
}