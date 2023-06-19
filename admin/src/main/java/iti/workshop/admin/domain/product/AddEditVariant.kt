package iti.workshop.admin.domain.product

import iti.workshop.admin.data.dto.Variant
import iti.workshop.admin.data.dto.VariantSingleResponseAndRequest
import iti.workshop.admin.data.repository.IProductRepository
import retrofit2.Response

class AddEditVariant(private val _repo: IProductRepository) {
    suspend operator fun invoke(
        product_id: Long,
        data: Variant
    ): Response<VariantSingleResponseAndRequest> {
        return _repo.addProductVariant(
            product_id,
            VariantSingleResponseAndRequest(variant = data)
        )
    }
}