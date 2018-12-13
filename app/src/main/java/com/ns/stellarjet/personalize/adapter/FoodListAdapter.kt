package com.ns.stellarjet.personalize.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.ns.networking.model.Food
import com.ns.stellarjet.R
import com.squareup.picasso.Picasso

class FoodListAdapter (
    private val mFoodList : List<Food>,
    private val onSelectDishListenerParams : (String) -> Unit): RecyclerView.Adapter<FoodListAdapter.FoodListViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FoodListViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.layout_row_food_list, parent, false)
        return FoodListViewHolder(v)
    }

    override fun getItemCount(): Int {
        return mFoodList.size
    }

    override fun onBindViewHolder(holder: FoodListViewHolder, position: Int) {
        holder.bind(mFoodList.get(position), onSelectDishListenerParams)
    }

    class FoodListViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){

        private val mFoodName : TextView = itemView.findViewById(R.id.textView_food_list_row_name)
        private val mFoodDescription : TextView = itemView.findViewById(R.id.textView_food_list_row_content)
        private val mFoodImage : ImageView = itemView.findViewById(R.id.imageView_food_list_image)
        private val mFoodSelect : ImageView = itemView.findViewById(R.id.imageView_food_list_select)

        fun bind(foodType: Food, onSelectDishListenerParams: (String) -> Unit){
            mFoodName.text = foodType.name
            mFoodDescription.text = foodType.description
            Picasso.with(itemView.context)
                .load(foodType.img_url)
                .into(mFoodImage)
            if(foodType.pref){
                mFoodSelect.setImageDrawable(itemView.context
                    .resources.getDrawable(R.drawable.ic_food_selected))
            }

            mFoodSelect.setOnClickListener {
                var isSelected = false
                if(mFoodSelect.drawable.constantState == itemView.context
                        .resources.getDrawable(R.drawable.ic_food_selected).constantState){
                    isSelected = false
                    mFoodSelect.setImageDrawable(itemView.context
                        .resources.getDrawable(R.drawable.ic_food_unselected))
                }else{
                    onSelectDishListenerParams(foodType.id.toString())
                    isSelected = true
                    mFoodSelect.setImageDrawable(itemView.context
                        .resources.getDrawable(R.drawable.ic_food_selected))
                }

            }

            /*itemView.setOnClickListener {
                onSelectDishListenerParams(foodType)
            }*/
        }
    }
}