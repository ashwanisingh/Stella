package com.ns.stellarjet.personalize

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ns.stellarjet.R
import com.ns.stellarjet.databinding.ActivityFoodPreferencesLaunchBinding
import com.ns.stellarjet.home.HomeActivity
import com.ns.stellarjet.personalize.adapter.FoodCategoryListAdapter
import com.ns.stellarjet.utils.UIConstants
import java.util.*

class FoodPreferencesLaunchActivity : AppCompatActivity(), (String) -> Unit {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_food_preferences_launch)

        val activityFoodPreferencesBinding: ActivityFoodPreferencesLaunchBinding =
        DataBindingUtil.setContentView(
                    this ,
                    R.layout.activity_food_preferences_launch
                )

        activityFoodPreferencesBinding.buttonFoodPrefBack.setOnClickListener {
            onBackPressed()
        }

        val foodListAdapter = FoodCategoryListAdapter(
            makeFoodTypeList(),
            this
        )

        val layoutManager = LinearLayoutManager(
            this ,
            RecyclerView.VERTICAL ,
            false
        )

        activityFoodPreferencesBinding.recyclerViewFoodTypes.adapter = foodListAdapter
        activityFoodPreferencesBinding.recyclerViewFoodTypes.layoutManager = layoutManager
    }

    /**
     * this methods makes the food types in a list from the global list
     * */
    private fun makeFoodTypeList() : List<String>{
        val mFoodsList = HomeActivity.sUserData.customer_prefs.foods
        val foodType : HashSet<String> = HashSet()
        val mFoodsTypeList : MutableList<String> = ArrayList()

        mFoodsList.forEach {
            foodType.add(it.food_type_text)
        }

        foodType.forEach {
            mFoodsTypeList.add(it.capitalize())
            Log.d("FoodLaunchActivity" , it)
        }

        return mFoodsTypeList.sortedDescending()
    }


    override fun invoke(foodTypeSelected: String) {
        val mFoodListIntent =  Intent(
            this ,
            FoodPreferenceListActivity::class.java
        )
        mFoodListIntent.putExtra(UIConstants.BUNDLE_FOOD_TYPE , foodTypeSelected)
        startActivity(mFoodListIntent)
    }

}
