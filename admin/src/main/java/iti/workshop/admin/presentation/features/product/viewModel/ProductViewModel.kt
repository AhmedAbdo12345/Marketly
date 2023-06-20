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
import iti.workshop.admin.data.dto.VariantSingleResponseAndRequest
import iti.workshop.admin.data.repository.IProductRepository
import iti.workshop.admin.domain.product.ProductUseCases
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
    private val _useCases: ProductUseCases
) : ViewModel() {

    private val _productListResponses =
        MutableStateFlow<DataListResponseState<ProductUIModel>>(DataListResponseState.OnLoading())
    val productListResponses: StateFlow<DataListResponseState<ProductUIModel>> get() = _productListResponses

    private val _productImageListResponses =
        MutableStateFlow<DataListResponseState<List<Image?>>>(DataListResponseState.OnLoading())
    val productImageListResponses: StateFlow<DataListResponseState<List<Image?>>> get() = _productImageListResponses

    private val _productVariantsListResponses =
        MutableStateFlow<DataListResponseState<List<Variant>>>(DataListResponseState.OnLoading())
    val productVariantsListResponses: StateFlow<DataListResponseState<List<Variant>>> get() = _productVariantsListResponses


    private var products: MutableList<Product>? = mutableListOf()

    private val _actionResponse = MutableStateFlow<Pair<Boolean?, String?>>(Pair(null, null))
    val actionResponse: StateFlow<Pair<Boolean?, String?>> get() = _actionResponse

    private val _addOrEditResponses =
        MutableStateFlow<DataListResponseState<Product>>(DataListResponseState.OnLoading())
    val addOrEditResponses: StateFlow<DataListResponseState<Product>> get() = _addOrEditResponses





    fun getCountOfProducts(isConnected:Boolean) {
        viewModelScope.launch {

            val responseProductList = async { _useCases.getProduct() }
            responseProductList.await().collect{
                when(it){
                    is DataListResponseState.OnSuccess -> products = it.data.products as MutableList<Product>
                    else -> {}
                }
                _productListResponses.value = it
            }

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






    // region Retrieve Rank
    fun retrieveImagesProductFromServer(product_id: Long) {
        viewModelScope.launch {
            val response = async { _useCases.getImagesProduct(product_id) }

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
            val response = async { _useCases.getVariantsProduct(product_id) }

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
    // endregion Retrieve Rank

    // region Add/Edit Rank
    fun addOrEditProduct(action: Action, product: Product) {
        viewModelScope.launch {
            val response:Deferred<Response<Product>> = async { _useCases.addEditProduct(action,product) }

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

    fun addImage(product_id: Long,image:Image){
        viewModelScope.launch {
            val response = async { _useCases.addEditImage(product_id,image)}
            if (response.await().isSuccessful) {
                retrieveImagesProductFromServer(product_id)
                _actionResponse.value = Pair(true, "Product Image Added Successfully")
            } else {
                _actionResponse.value = Pair(false, response.await().errorBody()?.string())
            }

        }
    }
    fun addVariant(product_id: Long,variant:Variant){
        viewModelScope.launch {
            val response = async { _useCases.addEditVariant(product_id,variant)}
            if (response.await().isSuccessful) {
                retrieveVariantsProductFromServer(product_id)
                _actionResponse.value = Pair(true, "Product Variant Added Successfully")
            } else {
                _actionResponse.value = Pair(false, response.await().errorBody()?.string())
            }

        }
    }
    // endregion Add/Edit Rank

    // region Delete Rank
    fun deleteImageProductFromServer(product_id: Long, image_id: Long) {
        viewModelScope.launch {
            val response = async { _useCases.deleteImageProduct(product_id, image_id) }
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
            val response = async { _useCases.deleteVariantProduct(product_id, variant_id) }
            if (response.await().isSuccessful) {
                // TODO
                _actionResponse.value = Pair(true, "Variant Deleted Successfully")
            } else {
                _actionResponse.value = Pair(false, response.await().errorBody()?.string())
            }
        }
    }



    fun deleteProduct(product: Product,isConnected: Boolean) {
        viewModelScope.launch {
            val response = async { _useCases.deleteProduct(product) }
            if (response.await().isSuccessful) {

//                products?.remove(product)
//                _productListResponses.value = DataListResponseState.OnSuccess(
//                    ProductUIModel(
//                        products = products,
//                        count = Count(count = products?.size ?: 0)
//                    )
//                )
                getCountOfProducts(isConnected)
                _actionResponse.value = Pair(true, "Product Deleted Successfully")
            } else {
                _actionResponse.value = Pair(false, response.await().errorBody()?.string())
            }
        }
    }

    fun retrieveImagesProductFromLocal(product: Product) {
        viewModelScope.launch {
            product.images?.let {
                if (product.images?.isNotEmpty() == true) {
                    _productImageListResponses.value = DataListResponseState.OnSuccess(it)
                } else {
                    _productImageListResponses.value = DataListResponseState.OnNothingData()
                }
            }
        }
    }

    fun retrieveVariantsProductFromLocal(product: Product) {
        viewModelScope.launch {
            product.variants?.let {
                if (product.variants?.isNotEmpty() == true) {
                    _productVariantsListResponses.value = DataListResponseState.OnSuccess(it)
                } else {
                    _productVariantsListResponses.value = DataListResponseState.OnNothingData()
                }
            }
        }
    }

    // endregion Delete Rank

}