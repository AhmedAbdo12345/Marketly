package iti.workshop.admin.domain.product

import iti.workshop.admin.data.dto.VariantListResponse
import iti.workshop.admin.data.repository.IProductRepository
import retrofit2.Response

class GetVariantsProduct(private val _repo: IProductRepository) {
    suspend operator fun invoke(product_id: Long): Response<VariantListResponse>
     =  _repo.getProductVariants(product_id)
}