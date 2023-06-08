package iti.workshop.admin.presentation.features.home.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import iti.workshop.admin.data.repository.Repository
import iti.workshop.admin.presentation.features.home.model.HomeModel
import iti.workshop.admin.presentation.utils.DataListResponseState
import iti.workshop.admin.presentation.utils.DataResponseState
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val _repo: Repository
) : ViewModel() {

    private var _model:HomeModel = HomeModel()
    private val _counts: MutableStateFlow<DataResponseState<HomeModel>> =
        MutableStateFlow(DataResponseState.OnLoading())
    val counts: StateFlow<DataResponseState<HomeModel>> get() = _counts

    init {
        updateCounts()
    }

    private fun updateCounts() {
        viewModelScope.launch {
            val productsCounts = async { _repo.productRepository.getCount() }
            val couponsCounts = async { _repo.couponRepository.getCount() }
            val inventoryLocationCount = async { _repo.inventoryRepository.retrieveCountLocations() }

            // Products Counts
           if (productsCounts.await().isSuccessful) {
               _model = _model.copy(
                   productCount = (productsCounts.await().body()?.count ?: 0).toString()
               )
               _counts.value = DataResponseState.OnSuccess(_model)
           } else {
               _counts.value =
                   DataResponseState.OnError(productsCounts.await().errorBody().toString())
           }

            // Coupons Counts
            if (couponsCounts.await().isSuccessful) {
                _model = _model.copy(
                    couponsCount = (couponsCounts.await().body()?.count ?: 0).toString()
                )
                _counts.value = DataResponseState.OnSuccess(_model)
            } else {
                _counts.value =
                    DataResponseState.OnError(couponsCounts.await().errorBody().toString())
            }

            // Inventory Counts
            if (inventoryLocationCount.await().isSuccessful) {
                _model = _model.copy(
                    inventoryLocationCount = (inventoryLocationCount.await().body()?.count
                        ?: 0).toString()
                )
                _counts.value = DataResponseState.OnSuccess(_model)
            } else {
                _counts.value =
                    DataResponseState.OnError(inventoryLocationCount.await().errorBody().toString())
            }
        }
    }
}