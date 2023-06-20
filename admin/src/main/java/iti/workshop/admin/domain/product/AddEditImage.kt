package iti.workshop.admin.domain.product

import iti.workshop.admin.data.dto.Image
import iti.workshop.admin.data.dto.ImagesSingleResponse
import iti.workshop.admin.data.dto.PostImage
import iti.workshop.admin.data.dto.Variant
import iti.workshop.admin.data.dto.VariantSingleResponseAndRequest
import iti.workshop.admin.data.repository.IProductRepository
import retrofit2.Response

class AddEditImage(private val _repo: IProductRepository) {
    suspend operator fun invoke(
        product_id: Long,
        data: Image
    ): Response<ImagesSingleResponse> {
        return _repo.addImageProduct(
            product_id,
            PostImage(image = data)
        )
    }
}