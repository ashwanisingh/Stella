package com.ns.stellarjet.personalize

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ns.networking.model.Food
import com.ns.networking.model.FoodPersonalizeResponse
import com.ns.networking.retrofit.RetrofitAPICaller
import com.ns.stellarjet.R
import com.ns.stellarjet.databinding.ActivityFoodPreferenceListBinding
import com.ns.stellarjet.home.HomeActivity
import com.ns.stellarjet.personalize.adapter.FoodListAdapter
import com.ns.stellarjet.utils.SharedPreferencesHelper
import com.ns.stellarjet.utils.StellarJetUtils
import com.ns.stellarjet.utils.UIConstants
import kotlinx.android.synthetic.main.activity_food_preference_list.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FoodPreferenceListActivity : AppCompatActivity(), (String) -> Unit {

    //    private var mSelectedFoodIds = ""
    private val mSelectedFoodIds : MutableList<String> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val activityBinding: ActivityFoodPreferenceListBinding =  DataBindingUtil.setContentView(
            this ,
            R.layout.activity_food_preference_list
        )

        val foodType : String = intent.extras?.getString(UIConstants.BUNDLE_FOOD_TYPE)!!
        activityBinding.textViewFoodListName.text = foodType


        val foodListAdapter = FoodListAdapter(
            makeFoodListByCategory(foodType) ,
            this
        )

        val layoutManager = LinearLayoutManager(
            this ,
            RecyclerView.VERTICAL ,
            false
        )
        activityBinding.recyclerViewFoodList.adapter = foodListAdapter
        activityBinding.recyclerViewFoodList.layoutManager= layoutManager

        button_food_list_back.setOnClickListener {
            onBackPressed()
        }

        button_food_list_confirm.setOnClickListener {

            Log.d("Booking", "onResponse:")
            if(StellarJetUtils.isConnectingToInternet(applicationContext)){
                personalizeFood()
            }else{
                Toast.makeText(applicationContext, "Not Connected to Internet", Toast.LENGTH_SHORT).show()
            }

        }
    }

    private fun personalizeFood(){
        val personalizeFood : Call<FoodPersonalizeResponse> = RetrofitAPICaller.getInstance(this)
            .stellarJetAPIs.personalizeFood(
            SharedPreferencesHelper.getUserToken(this) ,
            SharedPreferencesHelper.getBookingId(this) ,
            mSelectedFoodIds
        )

        personalizeFood.enqueue(object : Callback<FoodPersonalizeResponse> {
            override fun onResponse(
                call: Call<FoodPersonalizeResponse>,
                response: Response<FoodPersonalizeResponse>){
                Log.d("Booking", "onResponse: $response")
                SharedPreferencesHelper.saveFoodPersonalize(
                    this@FoodPreferenceListActivity ,
                    true
                )
                finish()/*
                    if (response.body() != null && response.body()!!.resultcode == 1) {

                    }*/
            }

            override fun onFailure(call: Call<FoodPersonalizeResponse>, t: Throwable) {
                Log.d("Booking", "onResponse: $t")
            }
        })
    }

    private fun makeFoodListByCategory(foodType : String) :  MutableList<Food>{
        val mFoodsList =  HomeActivity.sUserData.customer_prefs.foods

        val mFoodsDisplayList : MutableList<Food> = ArrayList()

        mFoodsList.forEach {
            if(it.food_type_text.equals(foodType , false)){
                mFoodsDisplayList.add(it)
            }
        }

        return mFoodsDisplayList
    }

    override fun invoke(selectedID: String) {
        mSelectedFoodIds.add(selectedID.toInt().toString())
    }
}



