package iti.mad.marketly.presentation.brandProduct

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import iti.mad.marketly.AppDependencies
import iti.mad.marketly.data.model.brandproduct.BrandProductResponse
import iti.mad.marketly.data.model.brandproduct.Product
import iti.mad.marketly.data.repository.brandproduct.BrandProductRepo
import iti.mad.marketly.data.repository.favourite_repo.IFavouriteRepo
import iti.mad.marketly.utils.ResponseState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.zip
import kotlinx.coroutines.launch

class BrandProductViewModel(
    private val brandsRepo: BrandProductRepo,
    private val favouriteRep: IFavouriteRepo
) : ViewModel() {
    private var brandProduct: MutableStateFlow<ResponseState<List<Product>>> =
        MutableStateFlow(
            ResponseState.OnLoading()
        )
    val _brandProduct: StateFlow<ResponseState<List<Product>>> = brandProduct
    private val _addedSuccessfully =
        MutableStateFlow<ResponseState<String>>(ResponseState.OnLoading())
    val addedSuccessfully: StateFlow<ResponseState<String>> = _addedSuccessfully
    private val _deletedSuccessfully =
        MutableStateFlow<ResponseState<String>>(ResponseState.OnLoading())
    val deletedSuccessfully: StateFlow<ResponseState<String>> = _deletedSuccessfully

   /* private var _allFavouriteIDS =
        MutableStateFlow<ResponseState<List<String>>>(ResponseState.OnLoading())
    val allFavouriteIDS = _allFavouriteIDS*/
    fun getAllBrandProduct(brandID: String,userID: String) {
        viewModelScope.launch {
            brandsRepo.getBrandProduct(brandID).combine(favouriteRep.getAllFavouriteIDS(userID)){
                    r1,r2->r1.products.map {
                        product ->  if(r2.contains(product.id.toString())){
                            product.isFavourite=true
                        }
                product
            }
            }.flowOn(Dispatchers.IO).catch {
                brandProduct.value = ResponseState.OnError(it.localizedMessage ?: "")

            }.collect{
                brandProduct.value = ResponseState.OnSuccess(it)
            }
        }
    }
    fun addProductToFavourite(userID: String, product: iti.mad.marketly.data.model.productDetails.Product) {
        viewModelScope.launch {
            favouriteRep.addProductToFavourite(userID, product).flowOn(Dispatchers.IO).catch {
                _addedSuccessfully.value = ResponseState.OnError(it.localizedMessage ?: "")
                print(it.printStackTrace())
            }.collect {
                print("essss")
                _addedSuccessfully.value = ResponseState.OnSuccess("added Successfully")
            }
        }
    }

    fun deleteProductFromFavourite(userID: String, product: iti.mad.marketly.data.model.productDetails.Product) {
        viewModelScope.launch {
            favouriteRep.deleteFromFavourite(userID, product).flowOn(Dispatchers.IO).catch {
                _deletedSuccessfully.value = ResponseState.OnError(it.localizedMessage ?: "")
                print(it.printStackTrace())
            }.collect {
                print("essss")
                _deletedSuccessfully.value = ResponseState.OnSuccess("deleted Successfully")
            }
        }
    }

  /*  fun getAllFavouriteIDs(userID: String) {
        viewModelScope.launch {
            favouriteRep.getAllFavouriteIDS(userID).flowOn(Dispatchers.IO).catch {
                _allFavouriteIDS.value = ResponseState.OnError(it.localizedMessage ?: "")

            }
                .collect {
                    _allFavouriteIDS.value = ResponseState.OnSuccess(it)
                }
        }

    }*/

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                BrandProductViewModel(
                    AppDependencies.brandProductRepo,
                    AppDependencies.favouriteRep
                )
            }
        }
    }
}