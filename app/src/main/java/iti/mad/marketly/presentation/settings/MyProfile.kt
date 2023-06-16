package iti.mad.marketly.presentation.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import iti.mad.marketly.R
import iti.mad.marketly.databinding.FragmentMyProfileBinding
import iti.mad.marketly.databinding.FragmentSettingsBinding
import iti.mad.marketly.presentation.home.HomeFragmentDirections


class MyProfile : Fragment() {
    lateinit var binding:FragmentMyProfileBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.settingCdProfileFrag.setOnClickListener(View.OnClickListener {
            var action= MyProfileDirections.actionMyProfile2ToSettings()
        findNavController().navigate(action)
        })


        binding.tvOrders.setOnClickListener {
            var action = MyProfileDirections.actionMyProfile2ToOrderFragment()
            findNavController().navigate(action)

        }

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentMyProfileBinding.inflate(layoutInflater,container,false)
        binding.lifecycleOwner=viewLifecycleOwner
        return binding.root
    }


}