package iti.mad.marketly.presentation

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import iti.mad.marketly.R
import iti.mad.marketly.databinding.FragmentSplashBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class SplashFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {

        return inflater.inflate(R.layout.fragment_splash, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // TODO val share = SharedManager.getInstance(requireContext())
        lifecycleScope.launch(Dispatchers.Main) {
            delay(3000)
            var action = SplashFragmentDirections.actionSplashFragmentToHomeFragment()
            findNavController().navigate(action)

          //  findNavController().navigate(R.id.action_splashFragment_to_homeFragment)

        }
    }

}