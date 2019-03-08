package com.ns.stellarjet.drawer.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.ns.networking.model.foods.FoodDaysSelection
import com.ns.stellarjet.R


class FoodDaysAdapter(
    private val mSecondaryUsersList: MutableList<out FoodDaysSelection>,
    private val onSelectUsersListener: (FoodDaysSelection , Int) -> Unit) : RecyclerView.Adapter<FoodDaysAdapter.FoodDaysListViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FoodDaysListViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.layout_row_food_days, parent, false)
        return FoodDaysListViewHolder(v)
    }

    override fun getItemCount(): Int {
        return mSecondaryUsersList.size
    }

    override fun onBindViewHolder(holder: FoodDaysListViewHolder, position: Int) {
        holder.bind(mSecondaryUsersList[position], onSelectUsersListener , position)
    }

    class FoodDaysListViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){

        val mManagersName : TextView = itemView.findViewById(R.id.textView_row_food_days)

        fun bind(
            mUsers: FoodDaysSelection,
            onSelectUsersListener: (FoodDaysSelection , Int) -> Unit,
            position: Int){

            mManagersName.text = mUsers.daysName.substring(0,3)
            if(mUsers.isSelected){
                mManagersName.alpha = 1.0f
            }else{
                mManagersName.alpha = 0.2f
            }
            mManagersName.setOnClickListener {
                if(mUsers.isSelected){
                    mManagersName.alpha = 0.2f
                    mUsers.isSelected = false
                }else{
                    mManagersName.alpha = 1.0f
                    mUsers.isSelected = true
                }
                onSelectUsersListener.invoke(mUsers , position)
            }
        }
    }
}
