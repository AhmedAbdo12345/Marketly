package iti.workshop.admin.presentation.features.inventory.viewModel

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import iti.workshop.admin.data.repository.IInventoryRepository
import iti.workshop.admin.data.repository.ImplInventoryRepository
import javax.inject.Inject

@HiltViewModel
class InventoryViewModel @Inject constructor(
    private val _repo: IInventoryRepository
):ViewModel() {
}