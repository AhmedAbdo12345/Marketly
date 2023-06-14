package iti.workshop.admin.presentation.features.auth.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import iti.workshop.admin.R
import iti.workshop.admin.databinding.AuthFragmentEmpolyersListBinding
import iti.workshop.admin.presentation.comon.Action
import iti.workshop.admin.presentation.comon.ConstantsKeys
import iti.workshop.admin.presentation.features.auth.adapters.AuthEmployersAdapter
import iti.workshop.admin.presentation.features.auth.adapters.AuthEmployersOnCLickListener
import iti.workshop.admin.presentation.features.auth.model.User
import iti.workshop.admin.presentation.features.auth.viewModel.AuthViewModel
import iti.workshop.admin.presentation.utils.DataListResponseState
import iti.workshop.admin.presentation.utils.DataStates
import iti.workshop.admin.presentation.utils.Message
import kotlinx.coroutines.launch

@AndroidEntryPoint
class AuthEmployersListFragment : Fragment() {

    val viewModel: AuthViewModel by viewModels()
    lateinit var adapter: AuthEmployersAdapter
    lateinit var binding: AuthFragmentEmpolyersListBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding =
            DataBindingUtil.inflate(inflater, R.layout.auth_fragment_empolyers_list, container, false)
        binding.lifecycleOwner = this
        adapter = AuthEmployersAdapter(AuthEmployersOnCLickListener(::selectItem,::deleteItem))
        binding.mAdapter = adapter

        viewModel.getUsersList()
        updateUISate()
        addUserAction()
        return binding.root
    }

    private fun addUserAction() {
        val bundle = Bundle()
        bundle.putSerializable(ConstantsKeys.ACTION_KEY,Action.Add)
        findNavController().navigate(R.id.action_authEmployersListFragment_to_authProfileAddEditFragment,bundle)
    }

    private fun deleteItem(model: User) {

    }

    private fun selectItem(model: User) {
        val bundle = Bundle()
        bundle.putSerializable(ConstantsKeys.USER_KEY, model)
        findNavController().navigate(R.id.action_authEmployersListFragment_to_authProfilePreviewFragment,bundle)
    }

    private fun updateUISate() {
        lifecycleScope.launch {
            viewModel.actionResponse.collect{ state ->
                state.first?.let {
                    Message.snakeMessage(
                        requireContext(),
                        binding.root,
                        state.second,
                        it
                    )?.show()
                }
            }
        }

        lifecycleScope.launch {
            viewModel.usersResponse.collect{state->
                when(state){
                    is DataListResponseState.OnError -> {
                        dataViewStates(DataStates.Error)
                        state.message.let { message ->
                            Message.snakeMessage(
                                requireContext(),
                                binding.root,
                                message,
                                false
                            )?.show()
                        }
                    }
                    is DataListResponseState.OnLoading -> {
                        dataViewStates(DataStates.Loading)
                    }
                    is DataListResponseState.OnNothingData -> {
                        dataViewStates(DataStates.Nothing)
                    }
                    is DataListResponseState.OnSuccess -> {
                        dataViewStates(DataStates.Data)
                        adapter.submitList(state.data)

                    }
                }
            }
        }

    }



    private fun dataViewStates(dataStates: DataStates) {
        when(dataStates){
            DataStates.Data -> {
                binding.shimmerResults.visibility = View.GONE
                binding.recyclerViewHolder.visibility = View.VISIBLE
                binding.nothingDataResponse.visibility = View.GONE
                binding.errorDataResponse.visibility = View.GONE
            }
            DataStates.Nothing -> {
                binding.shimmerResults.visibility = View.GONE
                binding.recyclerViewHolder.visibility = View.GONE
                binding.nothingDataResponse.visibility = View.VISIBLE
                binding.errorDataResponse.visibility = View.GONE
            }
            DataStates.Error -> {
                binding.shimmerResults.visibility = View.GONE
                binding.recyclerViewHolder.visibility = View.GONE
                binding.nothingDataResponse.visibility = View.GONE
                binding.errorDataResponse.visibility = View.VISIBLE
            }
            DataStates.Loading ->{
                binding.shimmerResults.visibility = View.VISIBLE
                binding.recyclerViewHolder.visibility = View.GONE
                binding.nothingDataResponse.visibility = View.GONE
                binding.errorDataResponse.visibility = View.GONE
            }
        }

    }
}

