package com.ns.stellarjet.personalize

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ns.networking.model.Food
import com.ns.networking.model.schedulefood.ScheduleFoodListResponse
import com.ns.networking.retrofit.RetrofitAPICaller
import com.ns.stellarjet.R
import com.ns.stellarjet.personalize.adapter.FoodCategoryListAdapter
import com.ns.stellarjet.utils.SharedPreferencesHelper
import com.ns.stellarjet.utils.UIConstants
import kotlinx.android.synthetic.main.activity_food_preferences_launch.*
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException
import java.util.HashSet

class PersonalizeFoodLaunchActivity : AppCompatActivity(), (String) -> Unit {

    private var mJourneyDate : String = ""
    private var scheduleId : String= ""
    private var isFromPostBooking : Boolean = false
    companion object {
        @JvmField var mCommonFoodsList : MutableList<Food> = ArrayList()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_food_preferences_launch)
        scheduleId = intent?.extras?.getString("ScheduleId" , "")!!
        mJourneyDate = intent?.extras?.getString("JourneyDate")!!
        isFromPostBooking = intent?.extras?.getBoolean("isPostBooking" , false)!!


        button_food_pref_back.setOnClickListener {
            onBackPressed()
        }

        getScheduleFoodPreferences()
    }

    /**
     * this methods makes the food types in a list from the global list
     * */
    private fun  makeCommonFoodTypeList(mFoodsList : List<Food>) : List<String>{
        val foodType : HashSet<String> = HashSet()
        val mFoodsTypeList : MutableList<String> = ArrayList()

        mFoodsList.forEach {
            foodType.add(it.food_type_text!!)
        }

        foodType.forEach {
            mFoodsTypeList.add(it.capitalize())
            Log.d("FoodLaunchActivity" , it)
        }

        return mFoodsTypeList.sortedDescending()
    }

    private fun getScheduleFoodPreferences(){
        val scheduleFoodId : Call<ScheduleFoodListResponse> = RetrofitAPICaller.getInstance(this)
            .stellarJetAPIs.getFoodListBySchedule(
            SharedPreferencesHelper.getUserToken(this),
            scheduleId.toInt() ,
            mJourneyDate)

        scheduleFoodId.enqueue(object : Callback<ScheduleFoodListResponse> {
            override fun onResponse(
                call: Call<ScheduleFoodListResponse>,
                response: Response<ScheduleFoodListResponse>){
                if(response.body()!=null){
                    Log.d("Booking", "onResponse: $response")
                    if(response.body()!!.data.isEmpty()){
                        Toast.makeText(
                            this@PersonalizeFoodLaunchActivity ,
                            "No Foods are available",
                            Toast.LENGTH_SHORT
                        ).show()
                        finish()
                    }else{
                        mCommonFoodsList.addAll(response.body()!!.data)
                        makeCommonFoodTypeList(response.body()!!.data)
                        val foodListAdapter  = FoodCategoryListAdapter(
                            makeCommonFoodTypeList(response.body()!!.data),
                            this@PersonalizeFoodLaunchActivity
                        )
                        val layoutManager = LinearLayoutManager(
                            this@PersonalizeFoodLaunchActivity,
                            RecyclerView.VERTICAL ,
                            false
                        )

                        recyclerView_food_types.adapter = foodListAdapter
                        recyclerView_food_types.layoutManager = layoutManager
                    }
                }else if(response.code() == 400){
                    val mJsonObject: JSONObject
                    try {
                        mJsonObject = JSONObject(response.errorBody()!!.string())
                        val errorMessage = mJsonObject.getString("message")
                        Toast.makeText(
                            this@PersonalizeFoodLaunchActivity ,
                            errorMessage,
                            Toast.LENGTH_LONG
                        ).show()
                        finish()
                    } catch (e: JSONException) {
                        e.printStackTrace()
                    } catch (e: IOException) {
                        e.printStackTrace()
                    }
                }
            }

            override fun onFailure(call: Call<ScheduleFoodListResponse>, t: Throwable) {
                Log.d("Booking", "onResponse: $t")
                Toast.makeText(
                    this@PersonalizeFoodLaunchActivity ,
                    "Server Error" ,
                    Toast.LENGTH_SHORT).show()
            }
        })
    }

    override fun invoke(foodTypeSelected: String) {
        val mFoodListIntent =  Intent(
            this ,
            PersonalizeFoodListActivity::class.java
        )
        mFoodListIntent.putExtra(UIConstants.BUNDLE_FOOD_TYPE , foodTypeSelected)
        mFoodListIntent.putExtra("isPostBooking" , isFromPostBooking)
        startActivity(mFoodListIntent)
    }
}
