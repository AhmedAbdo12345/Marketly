package iti.workshop.admin.domain.product

import androidx.lifecycle.viewModelScope
import iti.workshop.admin.data.dto.Count
import iti.workshop.admin.data.dto.Product
import iti.workshop.admin.data.remote.retrofit.RetrofitInstance
import iti.workshop.admin.data.repository.IProductRepository
import iti.workshop.admin.presentation.features.product.models.ProductUIModel
import iti.workshop.admin.presentation.utils.DataListResponseState
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class GetProduct(private val _repo: IProductRepository) {
    operator fun invoke():Flow<DataListResponseState<ProductUIModel>>
    = try {
        getFromServer()
    }catch (exception: RetrofitInstance.NoConnectivityException){
        fromRoom()
    }

    private fun fromRoom():Flow<DataListResponseState<ProductUIModel>>
    = _repo.getAllProducts().map {
            val result =ProductUIModel(
                count = Count(it.size),
                products = it
            )
            DataListResponseState.OnSuccess(result)
        }


    private fun getFromServer():Flow<DataListResponseState<ProductUIModel>> = flow {
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