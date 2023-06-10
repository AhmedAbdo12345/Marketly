package iti.workshop.admin.presentation.features.coupon.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import iti.workshop.admin.R
import iti.workshop.admin.data.dto.DiscountCode
import iti.workshop.admin.databinding.CouponItemDiscountCodeBinding


class DiscountCodeAdapter(
    private val clickListener: DiscountCodeOnCLickListener,
) : ListAdapter<DiscountCode, DiscountCodeAdapter.MyViewHolder>(DiscountCodeDiffCallback()){


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder
    = MyViewHolder(  LayoutInflater.from(parent.context).inflate(R.layout.coupon_item_discount_code, parent, false)  )

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.binder(getItem(position), clickListener)
    }

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val binder = CouponItemDiscountCodeBinding.bind(itemView)
        fun binder(data:DiscountCode,itemOnCLickListener: DiscountCodeOnCLickListener){
                binder.model = data
                binder.clickListener = itemOnCLickListener
                binder.executePendingBindings()
        }
    }


}

class DiscountCodeDiffCallback: DiffUtil.ItemCallback<DiscountCode>() {
    override fun areItemsTheSame(oldItem: DiscountCode, newItem: DiscountCode): Boolean
            = oldItem.id == newItem.id

    override fun areContentsTheSame(oldItem: DiscountCode, newItem: DiscountCode): Boolean =
        oldItem.id == newItem.id &&
        oldItem.code == newItem.code &&
        oldItem.created_at == newItem.created_at &&
        oldItem.updated_at == newItem.updated_at &&
        oldItem.price_rule_id == newItem.price_rule_id &&
        oldItem.usage_count == newItem.usage_count

}

class DiscountCodeOnCLickListener(
    val clickListener: (model: DiscountCode) -> Unit,
    val deleteItemListener: (model: DiscountCode) -> Unit
) {
    fun onClick(model: DiscountCode) = clickListener(model)
    fun onDeleteItem(model: DiscountCode) = deleteItemListener(model)
}

