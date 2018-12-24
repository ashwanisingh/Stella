package com.ns.stellarjet.personalize.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.ns.networking.model.Food
import com.ns.stellarjet.R
import com.ns.stellarjet.utils.StellarJetUtils
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
                mFoodSelect.setImageDrawable(ContextCompat.getDrawable(itemView.context , R.drawable.ic_food_selected))
            }
            var isExpanded = false
            mFoodDescription.setOnClickListener {
                if(!isExpanded){
                    val params = LinearLayout.LayoutParams(
                        StellarJetUtils.pxFromDp(itemView.context , 250.0f),
                        LinearLayout.LayoutParams.WRAP_CONTENT
                    )
                    mFoodDescription.layoutParams = params
                    mFoodDescription.setCompoundDrawablesWithIntrinsicBounds(0, 0,R.drawable.ic_up_arrow ,0)
                    isExpanded = true
                }else if(isExpanded){
                    val params = LinearLayout.LayoutParams(
                        StellarJetUtils.pxFromDp(itemView.context , 250.0f),
                        StellarJetUtils.pxFromDp(itemView.context , 50.0f)
                    )
                    mFoodDescription.layoutParams = params
                    mFoodDescription.setCompoundDrawablesWithIntrinsicBounds(0 , 0,R.drawable.ic_down_arrow,0)
                    isExpanded = false
                }

            }

            mFoodSelect.setOnClickListener {
                //                var isSelected = false
//                if(mFoodSelect.drawable.constantState == itemView.context.resources.getDrawable(R.drawable.ic_food_selected).constantState){
                if(mFoodSelect.drawable.constantState == ContextCompat.getDrawable(itemView.context , R.drawable.ic_food_selected)?.constantState){
//                    isSelected = false
                    mFoodSelect.setImageDrawable(ContextCompat.getDrawable(itemView.context , R.drawable.ic_food_unselected))
                }else{
                    onSelectDishListenerParams(foodType.id.toString())
//                    isSelected = true
                    mFoodSelect.setImageDrawable(ContextCompat.getDrawable(itemView.context , R.drawable.ic_food_selected))
                }

            }

            /*itemView.setOnClickListener {
                onSelectDishListenerParams(foodType)
            }*/
        }
    }
}