package iti.workshop.admin.domain.product

import iti.workshop.admin.data.dto.Count
import iti.workshop.admin.data.repository.IProductRepository
import retrofit2.Response

class GetCountProduct(private val _repo: IProductRepository) {
    suspend operator fun invoke(): Response<Count> = _repo.getCount()
}