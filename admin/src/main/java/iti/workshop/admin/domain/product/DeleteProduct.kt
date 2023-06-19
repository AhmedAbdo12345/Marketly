package iti.workshop.admin.domain.product

import iti.workshop.admin.data.dto.Product
import iti.workshop.admin.data.repository.IProductRepository
import retrofit2.Response

class DeleteProduct(private val _repo: IProductRepository) {
    suspend operator fun invoke(product: Product):Response<Void>
     = _repo.deleteProduct(product.id)
}