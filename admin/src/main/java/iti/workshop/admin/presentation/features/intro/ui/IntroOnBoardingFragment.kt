package iti.workshop.admin.presentation.features.intro.ui

import android.os.Bundle
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager
import iti.workshop.admin.R
import iti.workshop.admin.data.shared.SharedManager
import iti.workshop.admin.databinding.IntroOnboardingFragmentBinding
import iti.workshop.admin.presentation.features.intro.adapter.OnBoardingScreenAdapter
import iti.workshop.admin.presentation.features.intro.model.OnBoardingScreenModel
import iti.workshop.admin.presentation.features.intro.model.OnBoardingScreenModelResponse

class IntroOnBoardingFragment : Fragment() {
    private var sharedManager:SharedManager? = null

    lateinit var onBoardingModels: List<OnBoardingScreenModel>
    private lateinit var pagerAdapter: PagerAdapter
    lateinit var dots: Array<TextView?>
    lateinit var binding: IntroOnboardingFragmentBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        sharedManager = SharedManager.getInstance(requireContext())
        binding = DataBindingUtil.inflate(inflater, R.layout.intro_onboarding_fragment, container, false)
        onBoardingModels = OnBoardingScreenModelResponse.onBoardingObjects
        pagerAdapter = OnBoardingScreenAdapter(onBoardingModels, requireContext())
        binding.OnBoardingPageView.adapter = pagerAdapter

        addDotsSlider(0)
        binding.OnBoardingPageView.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
            }

            override fun onPageSelected(position: Int) {
                addDotsSlider(position)
            }

            override fun onPageScrollStateChanged(state: Int) {}
        })
        binding.btnGetStart.setOnClickListener {
            sharedManager?.saveEntrance()
            if (sharedManager?.isUser == true) {
                findNavController().navigate(R.id.action_introOnBoardingFragment_to_homeFragment)
            } else {
                findNavController().navigate(R.id.action_introOnBoardingFragment_to_authLoginFragment)
            }
        }
        return binding.root
    }

    private fun addDotsSlider(position: Int) {
        binding.dotsSlider.removeAllViews()
        dots = arrayOfNulls(onBoardingModels.size)
        for (i in dots.indices) {
            dots[i] = TextView(requireContext())
            dots[i]!!.text = Html.fromHtml("&#8226;")
            dots[i]?.textSize = 50f
            dots[i]?.setTextColor(resources.getColor(R.color.textAd))
            binding.dotsSlider.addView(dots[i])
        }
        dots[position]?.setTextColor(resources.getColor(R.color.primary))
    }


}