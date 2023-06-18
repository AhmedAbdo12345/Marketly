package iti.mad.marketly.onboarding

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.text.Html
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager
import iti.mad.marketly.R
import iti.mad.marketly.presentation.MainActivity


class OnboardingScreenActivity : AppCompatActivity() {
    lateinit var viewPager: ViewPager
    lateinit var onBoardingModels: List<OnBoardingScreenModel>
    lateinit var pagerAdapter: PagerAdapter
    lateinit var linearLayout: LinearLayout
    lateinit var dots: Array<TextView?>
    lateinit var GetStarted: Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_onboarding_screen)
        viewPager = findViewById<ViewPager>(R.id.OnBoardingPageView)
        onBoardingModels = OnBoardingScreenModelResponse.onBoardingObjects
        pagerAdapter = OnBoardingScreenAdapter(onBoardingModels!!, this)
        viewPager.setAdapter(pagerAdapter)
        linearLayout = findViewById<LinearLayout>(R.id.dots_Slider)
        add_dotsSlider(0)
        viewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrolled(
                position: Int, positionOffset: Float, positionOffsetPixels: Int
            ) {
            }

            override fun onPageSelected(position: Int) {
                add_dotsSlider(position)
            }

            override fun onPageScrollStateChanged(state: Int) {}
        })
        startedButton
    }

    private val startedButton: Unit
        private get() {
            GetStarted = findViewById<Button>(R.id.btn_Get_Start)
            val intent = Intent(this, MainActivity::class.java)
            GetStarted!!.setOnClickListener {
                updateScreen()
                startActivity(intent)
                this.finish()
            }
        }

    private fun add_dotsSlider(position: Int) {
        linearLayout.removeAllViews()
        dots = arrayOfNulls<TextView>(onBoardingModels!!.size)
        for (i in dots.indices) {
            dots[i] = TextView(this)
            dots[i]!!.setText(Html.fromHtml("&#8226;"))
            dots[i]?.setTextSize(50f)
            dots[i]?.setTextColor(getResources().getColor(R.color.grey))
            linearLayout.addView(dots[i])
        }
        dots[position]?.setTextColor(getResources().getColor(R.color.primary))
    }

    private fun updateScreen() {
        val Shared: SharedPreferences =
            getSharedPreferences(DeciderActivity.MYPREFERENCES, Context.MODE_PRIVATE)
        val editor: SharedPreferences.Editor = Shared.edit()
        editor.putBoolean("seen", true)
        editor.commit()
    }
}