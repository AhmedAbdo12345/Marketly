package iti.workshop.admin.presentation.features.auth.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import iti.workshop.admin.R
import iti.workshop.admin.databinding.AuthEmployersItemBinding
import iti.workshop.admin.presentation.features.auth.model.User


class AuthEmployersAdapter(
    private val clickListener: AuthEmployersOnCLickListener,
) : ListAdapter<User, AuthEmployersAdapter.MyViewHolder>(AuthEmployersDiffCallback()){


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder
    = MyViewHolder(  LayoutInflater.from(parent.context).inflate(R.layout.auth_employers_item, parent, false)  )

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.binder(getItem(position), clickListener)
    }

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val binder = AuthEmployersItemBinding.bind(itemView)
        fun binder(data:User,itemOnCLickListener: AuthEmployersOnCLickListener){
                binder.model = data
                binder.clickListener = itemOnCLickListener
                binder.executePendingBindings()
        }
    }


}

class AuthEmployersDiffCallback: DiffUtil.ItemCallback<User>() {
    override fun areItemsTheSame(oldItem: User, newItem: User): Boolean
            = oldItem.email == newItem.email

    override fun areContentsTheSame(oldItem: User, newItem: User): Boolean =
        oldItem.email == newItem.email &&
                oldItem.name == newItem.name

}

class AuthEmployersOnCLickListener(
    val clickListener: (model: User) -> Unit,
    val deleteItemListener: (model: User) -> Unit
) {
    fun onClick(model: User) = clickListener(model)
    fun onDeleteItem(model: User) = deleteItemListener(model)
}

