package iti.workshop.admin.domain.product

import iti.workshop.admin.data.repository.IProductRepository
import retrofit2.Response

class DeleteImageProduct(private val _repo: IProductRepository) {
    suspend operator fun invoke(product_id: Long, image_id: Long): Response<Void>
     = _repo.deleteImageProduct(product_id, image_id)
}