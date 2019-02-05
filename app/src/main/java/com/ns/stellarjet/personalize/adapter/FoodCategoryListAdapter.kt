package com.ns.stellarjet.personalize.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.ns.stellarjet.R

class FoodCategoryListAdapter(private val mFoodsList: List<String>,
                              private val onSelectDishListenerParams : (String) -> Unit) :
    RecyclerView.Adapter<FoodCategoryListAdapter.FoodViewHolder>() {



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FoodViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.layout_row_food_types, parent, false)
        return FoodViewHolder(v)
    }

    override fun getItemCount(): Int {
        return mFoodsList.size
    }

    override fun onBindViewHolder(holder: FoodViewHolder, position: Int) {
        holder.bind(mFoodsList.get(position) , onSelectDishListenerParams)
    }


    class FoodViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){

        private val mFoodCategoryName : TextView = itemView.findViewById(R.id.textView_row_foodsList_name)
        private val mFoodCategoryLayout : LinearLayout = itemView.findViewById(R.id.layout_row_foodsList)

        fun bind(foodType: String, onSelectDishListenerParams: (String) -> Unit){
            mFoodCategoryName.text = foodType
            mFoodCategoryLayout.setOnClickListener {
                onSelectDishListenerParams(foodType)
            }
        }
    }
}