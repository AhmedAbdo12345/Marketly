package iti.workshop.admin.presentation.features.home.viewModel

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import iti.workshop.admin.data.repository.Repository
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val _repo:Repository
):ViewModel() {
}