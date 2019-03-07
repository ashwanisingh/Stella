package com.ns.stellarjet.drawer.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.ns.networking.model.CancelBookingUsers
import com.ns.stellarjet.R

class CancelBookingUsersAdapter(
    private val mSecondaryUsersList: MutableList<out CancelBookingUsers>,
    private val onSelectUsersListener: (CancelBookingUsers) -> Unit,
    private var isAllSelected: Boolean
) : RecyclerView.Adapter<CancelBookingUsersAdapter.SecondaryUserListViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SecondaryUserListViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.layout_row_user_cancel, parent, false)
        return SecondaryUserListViewHolder(v)
    }

    override fun getItemCount(): Int {
        return mSecondaryUsersList.size
    }

    override fun onBindViewHolder(holder: SecondaryUserListViewHolder, position: Int) {
        if(isAllSelected){
            holder.mOverFlow.setImageResource(R.mipmap.ic_food_select)
            mSecondaryUsersList[position].isSelected = true
        }else{
            holder.mOverFlow.setImageResource(R.mipmap.ic_food_unselect)
            mSecondaryUsersList[position].isSelected = false
        }
        holder.bind(mSecondaryUsersList[position], onSelectUsersListener , position)
    }

    class SecondaryUserListViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){

        val mManagersName : TextView = itemView.findViewById(R.id.textView_row_cancel_seat_name)
        val mManagersPhone : TextView = itemView.findViewById(R.id.textView_row_cancel_passenger_name)
        val mOverFlow : ImageView = itemView.findViewById(R.id.imageView_row_managers_overflow)

        fun bind(
            mUsers: CancelBookingUsers,
            onSelectUsersListener: (CancelBookingUsers) -> Unit,
            position: Int){

            mManagersName.text = mUsers.passengerName
            mManagersPhone.text = mUsers.seatName

            itemView.setOnClickListener {
                var isUsercencelled = mUsers.isSelected
                if(isUsercencelled){
                    mOverFlow.setImageResource(R.mipmap.ic_food_unselect)
                    isUsercencelled = false
                }else{
                    mOverFlow.setImageResource(R.mipmap.ic_food_select)
                    isUsercencelled = true
                }
                mUsers.isSelected = isUsercencelled
                onSelectUsersListener(mUsers)
            }
        }
    }

    public fun selectAll(isSelected : Boolean){
        isAllSelected = isSelected
        notifyDataSetChanged()
    }
}