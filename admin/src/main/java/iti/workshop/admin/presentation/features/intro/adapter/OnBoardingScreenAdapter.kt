package iti.workshop.admin.presentation.features.intro.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.viewpager.widget.PagerAdapter
import iti.workshop.admin.R
import iti.workshop.admin.presentation.features.intro.model.OnBoardingScreenModel

class OnBoardingScreenAdapter(var PagesModel: List<OnBoardingScreenModel>, var context: Context) :
    PagerAdapter() {
    var layoutInflater: LayoutInflater? = null
    override fun getCount(): Int {
        return PagesModel.size
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view === `object`
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        layoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view: View = layoutInflater!!.inflate(R.layout.onboardingscreen, container, false)
        val imageView = view.findViewById<ImageView>(R.id.Page_Icon)
        val title: TextView = view.findViewById<TextView>(R.id.Page_Title)
        val description: TextView = view.findViewById<TextView>(R.id.Page_description)
        val onBoardingModel = PagesModel[position]
        imageView.setImageResource(onBoardingModel.image)
        title.text = onBoardingModel.title
        description.text = onBoardingModel.description
        container.addView(view)
        return view
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as View)
    }
}