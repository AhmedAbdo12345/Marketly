package iti.workshop.admin.domain.product

import iti.workshop.admin.data.dto.ImagesListResponse
import iti.workshop.admin.data.repository.IProductRepository
import retrofit2.Response

class GetImagesProduct(private val _repo: IProductRepository) {
    suspend operator fun invoke(product_id: Long
    ): Response<ImagesListResponse> = _repo.getImageProducts(product_id)
}