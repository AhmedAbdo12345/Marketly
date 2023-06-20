package iti.mad.marketly.presentation.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import iti.mad.marketly.data.source.local.sharedpreference.SharedPreferenceManager
import iti.mad.marketly.databinding.FragmentMyProfileBinding
import iti.mad.marketly.utils.AlertManager


class MyProfile : Fragment() {
    lateinit var binding: FragmentMyProfileBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (SharedPreferenceManager.isUserLogin(requireContext())) {
            binding.loginConstraint.visibility = View.VISIBLE
            binding.notLoginConstraint.visibility = View.GONE
            binding.settingCdProfileFrag.setOnClickListener(View.OnClickListener {
                var action = MyProfileDirections.actionMyProfileToSettings()
                findNavController().navigate(action)
            })


            binding.layoutOrders.setOnClickListener {
                var action = MyProfileDirections.actionMyProfileToOrderFragment()
                findNavController().navigate(action)
            }

            binding.logoutCard.setOnClickListener {
                AlertManager.functionalDialog(
                    "Log Out",
                    requireContext(),
                    "Are You sure,Do You want to Leave"
                ) {
                    FirebaseAuth.getInstance().signOut()
                    SharedPreferenceManager.deleteUserData(requireContext())
                    var action = MyProfileDirections.actionMyProfileToHomeFragment()
                    findNavController().navigate(action)



                }.show()
            }

        } else {
            binding.loginConstraint.visibility = View.GONE
            binding.notLoginConstraint.visibility = View.VISIBLE
            binding.loginBtn.setOnClickListener {
                val action = MyProfileDirections.actionMyProfileToLoginFragment()
                findNavController().navigate(action)

            }
            binding.registerBtn.setOnClickListener {
                val action = MyProfileDirections.actionMyProfileToRegisterFragment()
                findNavController().navigate(action)

            }

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentMyProfileBinding.inflate(layoutInflater, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }


}