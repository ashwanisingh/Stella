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
                onSelectDishListenerParams(foodType.id.toString() , true , position)
            }
            mFoodName.setOnClickListener {
                var isSelected = foodType.pref
                if(isSelected!!){
                    mFoodName.setCompoundDrawablesWithIntrinsicBounds(0 ,0 ,
                        R.mipmap.ic_food_unselect, 0)
                    isSelected= false
                    onSelectDishListenerParams(foodType.id.toString() , isSelected, position)
                }else{
                    mFoodName.setCompoundDrawablesWithIntrinsicBounds( 0 , 0 , R.mipmap.ic_food_select,0)
                    isSelected= true
                    onSelectDishListenerParams(foodType.id.toString() , isSelected, position)
                }
            }
            /*mFoodDescription.text = foodType.description
            Picasso.with(itemView.context)
                .load(foodType.img_url)
                .into(mFoodImage)
            if(foodType.pref!!){
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
*/
            /*  mFoodSelect.setOnClickListener {
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

              }*/

            /*itemView.setOnClickListener {
                onSelectDishListenerParams(foodType)
            }*/
        }
    }
}