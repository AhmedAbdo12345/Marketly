package iti.workshop.admin.presentation.features.coupon.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import iti.workshop.admin.R
import iti.workshop.admin.data.dto.PriceRule
import iti.workshop.admin.databinding.CouponItemPriceRuleBinding


class PriceRuleAdapter(
    private val clickListener: PriceRuleOnCLickListener,
) : ListAdapter<PriceRule, PriceRuleAdapter.MyViewHolder>(PriceRuleDiffCallback()){


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder
    = MyViewHolder(  LayoutInflater.from(parent.context).inflate(R.layout.coupon_item_price_rule, parent, false)  )

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.binder(getItem(position), clickListener)
    }

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val binder = CouponItemPriceRuleBinding.bind(itemView)
        fun binder(data:PriceRule,itemOnCLickListener: PriceRuleOnCLickListener){
                binder.model = data
                binder.clickListener = itemOnCLickListener
                binder.executePendingBindings()
        }
    }


}

class PriceRuleDiffCallback: DiffUtil.ItemCallback<PriceRule>() {
    override fun areItemsTheSame(oldItem: PriceRule, newItem: PriceRule): Boolean
            = oldItem.id == newItem.id

    override fun areContentsTheSame(oldItem: PriceRule, newItem: PriceRule): Boolean =
        oldItem.id == newItem.id &&
                oldItem.title == newItem.title

}

class PriceRuleOnCLickListener(
    val clickListener: (model: PriceRule) -> Unit,
    val deleteItemListener: (model: PriceRule) -> Unit
) {
    fun onClick(model: PriceRule) = clickListener(model)
    fun onDeleteItem(model: PriceRule) = deleteItemListener(model)
}

