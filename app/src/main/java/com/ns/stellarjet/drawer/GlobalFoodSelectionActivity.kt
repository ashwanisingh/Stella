package com.ns.stellarjet.drawer

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.TextView
import com.ns.stellarjet.R
import com.ns.stellarjet.home.HomeActivity
import kotlinx.android.synthetic.main.activity_global_food_selection.*
import android.widget.LinearLayout



class GlobalFoodSelectionActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(com.ns.stellarjet.R.layout.activity_global_food_selection)

        makeFoodListLayout()

        val foodCount = layout_global_food.childCount
        for (i in 0 until foodCount){
            val textViewFoodName = layout_global_food.getChildAt(i)
                .findViewById<TextView>(com.ns.stellarjet.R.id.textView_row_global_food_name)
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
        }
    }

    private fun makeFoodListLayout(){
        val foodCount = HomeActivity.sUserData.customer_prefs.food_categories.size
        val layoutInflater: LayoutInflater = LayoutInflater.from(this@GlobalFoodSelectionActivity)

        for (i in 0 until foodCount) {
            val v = layoutInflater.inflate(R.layout.layout_row_global_food, null)
            v.tag = "FoodItems $i"
            val params = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT
            )
            params.setMargins(0 , 24 , 32 , 0)
            layout_global_food.addView(v)
        }
    }
}
