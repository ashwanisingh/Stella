package com.ns.stellarjet.drawer

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ns.networking.model.foods.FoodDaysSelection
import com.ns.stellarjet.R
import com.ns.stellarjet.drawer.adapter.FoodDaysAdapter
import com.ns.stellarjet.home.HomeActivity
import kotlinx.android.synthetic.main.activity_global_food_selection.*

class GlobalFoodSelectionActivity : AppCompatActivity(){

    val foodDaysList = mutableListOf<FoodDaysSelection>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(com.ns.stellarjet.R.layout.activity_global_food_selection)

        makeFoodDaysList()
        makeFoodListLayout()

        val foodCount = layout_global_food.childCount
        for (i in 0 until foodCount){
            val textViewFoodName = layout_global_food.getChildAt(i)
                .findViewById<TextView>(com.ns.stellarjet.R.id.textView_row_global_food_name)
            val daysLayout = layout_global_food.getChildAt(i)
                .findViewById<LinearLayout>(com.ns.stellarjet.R.id.layout_row_global_days)
            val daysImageView = layout_global_food.getChildAt(i)
                .findViewById<ImageView>(com.ns.stellarjet.R.id.imageView_row_global_days)
            val daysRecyclerView = layout_global_food.getChildAt(i)
                .findViewById<RecyclerView>(com.ns.stellarjet.R.id.recyclerView_food_days)
            textViewFoodName.text = HomeActivity.sUserData.customer_prefs.food_categories[i].cat_text
            val foodCode = HomeActivity.sUserData.customer_prefs.food_categories[i].cat_key
            val foodPrefSize = HomeActivity.sUserData.customer_prefs.food_prefs.size
            var mFoodPrefList = ArrayList<String>()
            for(i in 0 until foodPrefSize){
                mFoodPrefList.add(mFoodPrefList[i])
            }
            if(mFoodPrefList.contains(foodCode)){
                textViewFoodName.setCompoundDrawablesWithIntrinsicBounds(0 ,0 ,
                    R.mipmap.ic_food_select, 0)
            }else{
                textViewFoodName.setCompoundDrawablesWithIntrinsicBounds(0 ,0 ,
                    R.mipmap.ic_food_unselect, 0)
            }
            textViewFoodName.setOnClickListener {
                textViewFoodName.setCompoundDrawablesWithIntrinsicBounds(0 ,0 ,
                    R.mipmap.ic_food_select, 0)
            }
            daysLayout.setOnClickListener{
                if(daysRecyclerView.visibility == View.GONE){
                    daysImageView.setImageResource(R.drawable.ic_up)
                    daysRecyclerView.visibility = View.VISIBLE
                }else {
                    daysImageView.setImageResource(R.drawable.ic_done)
                    daysRecyclerView.visibility = View.GONE
                }
            }

            val foodDaysAdapter = FoodDaysAdapter(foodDaysList , onSelectUsersListener = {

            })
            val layoutManager = LinearLayoutManager(
                this@GlobalFoodSelectionActivity ,
                RecyclerView.HORIZONTAL ,
                false)

            daysRecyclerView.layoutManager = layoutManager
            daysRecyclerView.adapter= foodDaysAdapter


        }
    }

    private fun makeFoodListLayout(){
        val foodCount = HomeActivity.sUserData.customer_prefs.food_categories.size
        val layoutInflater: LayoutInflater = LayoutInflater.from(this@GlobalFoodSelectionActivity)

        for (i in 0 until foodCount) {
            val v = layoutInflater.inflate(R.layout.layout_row_global_food, null)
            v.tag = "FoodItems $i"
            if(!HomeActivity.sUserData.customer_prefs.food_categories[i].show_days){
                val daysLayout = v.findViewById<LinearLayout>(com.ns.stellarjet.R.id.layout_row_global_days)
                daysLayout.visibility = View.GONE
            }
            layout_global_food.addView(v)

        }
    }

    private fun makeFoodDaysList(){
        foodDaysList.add(FoodDaysSelection("Sun" , false))
        foodDaysList.add(FoodDaysSelection("Mon" , false))
        foodDaysList.add(FoodDaysSelection("Tue" , false))
        foodDaysList.add(FoodDaysSelection("Wed" , false))
        foodDaysList.add(FoodDaysSelection("Thu" , false))
        foodDaysList.add(FoodDaysSelection("Fri" , false))
        foodDaysList.add(FoodDaysSelection("Sat" , false))
    }
}
