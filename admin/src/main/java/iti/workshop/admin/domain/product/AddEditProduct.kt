package iti.workshop.admin.domain.product

import iti.workshop.admin.data.dto.PostProduct
import iti.workshop.admin.data.dto.Product
import iti.workshop.admin.data.dto.UpdateProduct
import iti.workshop.admin.data.repository.IProductRepository
import iti.workshop.admin.presentation.comon.Action
import retrofit2.Response

class AddEditProduct(private val _repo: IProductRepository) {
    suspend operator fun invoke(action: Action, product: Product): Response<Product> {
        return when (action) {
            Action.Add -> _repo.addProduct(PostProduct(product))
            Action.Edit -> _repo.updateProduct(product.id, UpdateProduct(product))
        }
    }
}