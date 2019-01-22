package com.ns.stellarjet.personalize.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.ns.networking.model.Food
import com.ns.stellarjet.R

class FoodListAdapter (
    private val mFoodList : List<Food>,
    private val onSelectDishListenerParams : (String,Boolean,Int) -> Unit): RecyclerView.Adapter<FoodListAdapter.FoodListViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FoodListViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.layout_row_food_list, parent, false)
        return FoodListViewHolder(v)
    }

    override fun getItemCount(): Int {
        return mFoodList.size
    }

    override fun onBindViewHolder(holder: FoodListViewHolder, position: Int) {
        holder.bind(mFoodList.get(position), onSelectDishListenerParams , position)
    }

    class FoodListViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){

        private val mFoodName : TextView = itemView.findViewById(R.id.textView_food_list_row_name)
//        private val mFoodDescription : TextView = itemView.findViewById(R.id.textView_food_list_row_content)
//        private val mFoodImage : ImageView = itemView.findViewById(R.id.imageView_food_list_image)

        fun bind(
            foodType: Food,
            onSelectDishListenerParams: (String, Boolean, Int) -> Unit,
            position: Int
        ){
            mFoodName.text = foodType.name
            if(foodType.pref!!){
                mFoodName.setCompoundDrawablesWithIntrinsicBounds( 0 , 0 , R.mipmap.ic_food_select,0)
//                onSelectDishListenerParams(foodType.id.toString() , true , position)
            }
            var isSelected:Boolean? = foodType.pref
            mFoodName.setOnClickListener {
                if(isSelected!!){
                    mFoodName.setCompoundDrawablesWithIntrinsicBounds(0 ,0 ,
                        R.mipmap.ic_food_unselect, 0)
                    isSelected= false
                    onSelectDishListenerParams(foodType.id.toString() , isSelected!!, position)
                }else{
                    mFoodName.setCompoundDrawablesWithIntrinsicBounds( 0 , 0 , R.mipmap.ic_food_select,0)
                    isSelected= true
                    onSelectDishListenerParams(foodType.id.toString() , isSelected!!, position)
                }
            }
        }
    }
}