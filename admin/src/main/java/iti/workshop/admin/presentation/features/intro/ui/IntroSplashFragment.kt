package iti.workshop.admin.presentation.features.intro.ui

import android.app.ProgressDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import iti.workshop.admin.R
import iti.workshop.admin.data.local.shared.SharedManager
import iti.workshop.admin.databinding.IntroSplashFragmentBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class IntroSplashFragment : Fragment() {

    lateinit var binding:IntroSplashFragmentBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {

        binding = DataBindingUtil.inflate(inflater,R.layout.intro_splash_fragment,container,false)
        lifecycleScope.launch(Dispatchers.Main) {
            delay(3000)
            prepareProperties()
        }
        return binding.root
    }

    private fun prepareProperties() {
        val shared = SharedManager.getInstance(requireContext())
        // Log.d("seen",String.valueOf(seen));
        if (shared?.isFirstEntrance == true) {
            if (shared.isUser) {
                findNavController().navigate(R.id.action_splashFragment_to_homeFragment)
            } else {
                findNavController().navigate(R.id.action_splashFragment_to_authLoginFragment)
            }
        } else {
            findNavController().navigate(R.id.action_splashFragment_to_introOnBoardingFragment)
        }
    }
}