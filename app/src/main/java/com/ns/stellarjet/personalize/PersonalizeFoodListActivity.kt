package com.ns.stellarjet.personalize

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ns.networking.model.Food
import com.ns.stellarjet.R
import com.ns.stellarjet.personalize.PersonalizeFoodLaunchActivity.Companion.mCommonFoodsList
import com.ns.stellarjet.personalize.adapter.FoodListAdapter
import com.ns.stellarjet.utils.UIConstants
import kotlinx.android.synthetic.main.activity_food_preference_list.*

class PersonalizeFoodListActivity : AppCompatActivity(), (String, Boolean, Int) -> Unit {

    private var mSelectedFoodIds : MutableList<String> = ArrayList()
    private var mFoodsList: List<Food>? = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_food_preference_list)

        val foodType : String = intent.extras?.getString(UIConstants.BUNDLE_FOOD_TYPE)!!

        var foodListAdapter: FoodListAdapter= FoodListAdapter(
            makeCommonFoodListByCategory(foodType) ,
            this@PersonalizeFoodListActivity )

        val layoutManager = LinearLayoutManager(
            this ,
            RecyclerView.VERTICAL ,
            false
        )
        recyclerView_food_list.adapter = foodListAdapter
        recyclerView_food_list.layoutManager= layoutManager

        button_food_list_back.setOnClickListener {
            onBackPressed()
        }
    }

    private fun makeCommonFoodListByCategory(foodType : String) :  MutableList<Food>{
        mFoodsList = mCommonFoodsList
        val mFoodsDisplayList : MutableList<Food> = ArrayList()
        mFoodsList!!.forEach {
            if(it.food_type_text.equals(foodType , false)){
                mFoodsDisplayList.add(it)
            }
        }
        mFoodsDisplayList.forEach {
            it.pref = FoodPreferencesLaunchActivity.mPersonalizationFoodId.contains(it.id.toString())
        }
        FoodPreferencesLaunchActivity.mPersonalizationFoodId.forEach {
            mSelectedFoodIds.add(it)
        }
        return mFoodsDisplayList
    }

    override fun invoke(p1: String, p2: Boolean, p3: Int) {

    }
}
