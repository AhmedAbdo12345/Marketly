package iti.workshop.admin.presentation.features.product.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import iti.workshop.admin.data.dto.Count
import iti.workshop.admin.data.dto.Image
import iti.workshop.admin.data.dto.PostProduct
import iti.workshop.admin.data.dto.Product
import iti.workshop.admin.data.dto.UpdateProduct
import iti.workshop.admin.data.dto.Variant
import iti.workshop.admin.data.repository.IProductRepository
import iti.workshop.admin.presentation.comon.Action
import iti.workshop.admin.presentation.features.product.models.ProductUIModel
import iti.workshop.admin.presentation.utils.DataListResponseState
import iti.workshop.admin.presentation.utils.handleDate
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class ProductViewModel @Inject constructor(
    private val _repo: IProductRepository
) : ViewModel() {

    private val _productListResponses =
        MutableStateFlow<DataListResponseState<ProductUIModel>>(DataListResponseState.OnLoading())
    val productListResponses: StateFlow<DataListResponseState<ProductUIModel>> get() = _productListResponses

    private val _productImageListResponses =
        MutableStateFlow<DataListResponseState<List<Image>>>(DataListResponseState.OnLoading())
    val productImageListResponses: StateFlow<DataListResponseState<List<Image>>> get() = _productImageListResponses

    private val _productVariantsListResponses =
        MutableStateFlow<DataListResponseState<List<Variant>>>(DataListResponseState.OnLoading())
    val productVariantsListResponses: StateFlow<DataListResponseState<List<Variant>>> get() = _productVariantsListResponses


    private var products: MutableList<Product>? = mutableListOf()

    private val _actionResponse = MutableStateFlow<Pair<Boolean?, String?>>(Pair(null, null))
    val actionResponse: StateFlow<Pair<Boolean?, String?>> get() = _actionResponse

    private val _addOrEditResponses =
        MutableStateFlow<DataListResponseState<Product>>(DataListResponseState.OnLoading())
    val addOrEditResponses: StateFlow<DataListResponseState<Product>> get() = _addOrEditResponses


    fun deleteProduct(product: Product) {
        viewModelScope.launch {
            val response = async { _repo.deleteProduct(product.id) }
            if (response.await().isSuccessful) {

                products?.remove(product)
                _productListResponses.value = DataListResponseState.OnSuccess(
                    ProductUIModel(
                        products = products,
                        count = Count(count = products?.size ?: 0)
                    )
                )
                _actionResponse.value = Pair(true, "Product Deleted Successfully")
            } else {
                _actionResponse.value = Pair(false, response.await().errorBody()?.string())
            }
        }
    }

    fun addOrEditProduct(action: Action, product: Product) {
        viewModelScope.launch {
           val response:Deferred<Response<Product>> = when(action){
                Action.Add -> async { _repo.addProduct(PostProduct(product)) }
                Action.Edit -> async { _repo.updateProduct(product.id, UpdateProduct(product)) }
            }

            if (response.await().isSuccessful)
                response.await().body()?.let {
                    _actionResponse.value = Pair(true, "Product Added Successfully")

                    _addOrEditResponses.value = DataListResponseState.OnSuccess(it)
                }
            else {

                _actionResponse.value = Pair(false, response.await().errorBody()?.string())

                _addOrEditResponses.value =
                    DataListResponseState.OnError(
                        response.await().errorBody()?.string() ?: "Error Happened during fetch data"
                    )
            } }
    }

    fun getCountOfProducts() {
        viewModelScope.launch {

            val responseCount = async { _repo.getCount() }
            val responseProductList = async { _repo.getProduct() }

            if (responseCount.await().isSuccessful && responseProductList.await().isSuccessful) {

                val count = responseCount.await().body()
                products = responseProductList.await().body()?.products as MutableList<Product>

                if (count?.count == 0) {
                    _productListResponses.value = DataListResponseState.OnNothingData()

                } else {
                    ProductUIModel(
                        count = count,
                        products = products
                    ).apply {
                        _productListResponses.value = DataListResponseState.OnSuccess(this)
                    }
                }

            }
            if (!responseCount.await().isSuccessful)
                _productListResponses.value =
                    DataListResponseState.OnError(responseCount.await().errorBody().toString())

            if (!responseProductList.await().isSuccessful)
                _productListResponses.value = DataListResponseState.OnError(
                    responseProductList.await().errorBody()?.string() ?: "Error Happened during fetch data"
                )
        }
    }

    fun queryProductByTitle(searchQuery: CharSequence? = null) {
        viewModelScope.launch {
            delay(500)
            if (searchQuery != null) {
                val productResult =
                    products?.filter { product -> product.title?.startsWith(searchQuery) ?: false }
                if (productResult?.isNotEmpty() == true) {
                    _productListResponses.value = DataListResponseState.OnSuccess(
                        ProductUIModel(
                            products = productResult,
                            count = Count(count = products?.size ?: 0)
                        )
                    )
                } else {
                    _productListResponses.value = DataListResponseState.OnNothingData()
                }
            }
        }
    }


    fun retrieveImagesProductFromServer(product_id: Long) {
        viewModelScope.launch {
            val response = async { _repo.getImageProducts(product_id) }

            if (response.await().isSuccessful) {
                val data = response.await().body()?.images
                if (data?.isNotEmpty() == true) {
                    _productImageListResponses.value = DataListResponseState.OnSuccess(data)
                } else {
                    _productImageListResponses.value = DataListResponseState.OnNothingData()
                }
            } else {
                _productImageListResponses.value = DataListResponseState.OnError(
                    response.await().errorBody()?.string() ?: "Error Happened during fetch data"
                )
            }
        }
    }

    fun retrieveVariantsProductFromServer(product_id: Long) {
        viewModelScope.launch {
            val response = async { _repo.getProductVariants(product_id) }

            if (response.await().isSuccessful) {
                val data = response.await().body()?.variants?.map { item-> item.copy(
                    created_at = handleDate(item.created_at),
                    updated_at = handleDate(item.updated_at)
                ) }
                if (data?.isNotEmpty() == true) {
                    _productVariantsListResponses.value = DataListResponseState.OnSuccess(data)
                } else {
                    _productVariantsListResponses.value = DataListResponseState.OnNothingData()
                }
            } else {
                _productVariantsListResponses.value = DataListResponseState.OnError(
                    response.await().errorBody()?.string() ?: "Error Happened during fetch data"
                )
            }
        }
    }


    fun deleteImageProductFromServer(product_id: Long, image_id: Long) {
        viewModelScope.launch {
            val response = async { _repo.deleteImageProduct(product_id, image_id) }
            if (response.await().isSuccessful) {
                // TODO
                _actionResponse.value = Pair(true, "Image Deleted Successfully")
            } else {
                _actionResponse.value = Pair(false, response.await().errorBody()?.string())
            }
        }
    }

    fun deleteVariantProductFromServer(product_id: Long, variant_id: Long) {
        viewModelScope.launch {
            val response = async { _repo.deleteProductVariant(product_id, variant_id) }
            if (response.await().isSuccessful) {
                // TODO
                _actionResponse.value = Pair(true, "Variant Deleted Successfully")
            } else {
                _actionResponse.value = Pair(false, response.await().errorBody()?.string())
            }
        }
    }


}