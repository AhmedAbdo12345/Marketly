package iti.workshop.admin.domain.product

import iti.workshop.admin.data.repository.IProductRepository
import retrofit2.Response

class DeleteVariantProduct(private val _repo: IProductRepository) {
    suspend operator fun invoke(product_id: Long, variant_id: Long): Response<Void>
     =  _repo.deleteProductVariant(product_id, variant_id)
}