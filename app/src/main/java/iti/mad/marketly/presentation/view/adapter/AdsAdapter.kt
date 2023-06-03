package iti.mad.marketly.presentation.view.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import iti.mad.marketly.data.model.HomeAdsModel
import iti.mad.marketly.databinding.RvHomeAdsBinding

class AdsAdapter ( var mClickListener: ListItemClickListener) :
    ListAdapter<HomeAdsModel, AdsAdapter.AdsViewHolder>(AdsDiffUtils()) {

    lateinit var binding: RvHomeAdsBinding
    interface ListItemClickListener {
        fun onClickAds(homeAdsModel: HomeAdsModel)
    }
    class AdsViewHolder(var binding: RvHomeAdsBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdsViewHolder {
        //    var inflate=(LayoutInflater.from(parent.context).inflate(R.layout.rv_item, parent, false))as LayoutInflater
        var inflate = parent.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        binding = RvHomeAdsBinding.inflate(inflate, parent, false)
        return AdsViewHolder(binding)
    }

    override fun onBindViewHolder(holder: AdsViewHolder, position: Int)  {
    binding.imgVAds.setImageResource(getItem(position).img)

    }

}
