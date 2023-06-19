package iti.workshop.admin.domain.product

import androidx.lifecycle.viewModelScope
import iti.workshop.admin.data.dto.Product
import iti.workshop.admin.data.repository.IProductRepository
import iti.workshop.admin.presentation.features.product.models.ProductUIModel
import iti.workshop.admin.presentation.utils.DataListResponseState
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch

class GetProduct(private val _repo: IProductRepository) {
    suspend operator fun invoke():Flow<DataListResponseState<ProductUIModel>> = flow{

            val responseCount = _repo.getCount()
            val responseProductList = _repo.getProduct()

            if (responseCount.isSuccessful && responseProductList.isSuccessful) {

                val count = responseCount.body()
                val products = responseProductList.body()?.products
                _repo.insertAllTable(products)
                if (count?.count == 0) {
                   emit(DataListResponseState.OnNothingData())
                } else {
                    ProductUIModel(
                        count = count,
                        products = products
                    ).apply {
                        emit(DataListResponseState.OnSuccess(this))
                    }
                }

            }
            if (!responseCount.isSuccessful) emit( DataListResponseState.OnError(responseCount.errorBody().toString()))


            if (!responseProductList.isSuccessful) emit(DataListResponseState.OnError(responseProductList.errorBody()?.string() ?: "Error Happened during fetch data"))

    }
}