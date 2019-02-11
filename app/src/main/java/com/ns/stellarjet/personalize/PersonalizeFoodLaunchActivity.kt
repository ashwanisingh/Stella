package com.ns.stellarjet.personalize

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ns.networking.model.Food
import com.ns.networking.model.FoodItems
import com.ns.networking.model.schedulefood.ScheduleFoodListResponse
import com.ns.networking.retrofit.RetrofitAPICaller
import com.ns.stellarjet.R
import com.ns.stellarjet.personalize.adapter.FoodCategoryListAdapter
import com.ns.stellarjet.utils.SharedPreferencesHelper
import com.ns.stellarjet.utils.UIConstants
import com.ns.stellarjet.utils.UiUtils
import kotlinx.android.synthetic.main.activity_food_preferences_launch.*
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException
import java.util.HashSet
import kotlin.collections.ArrayList
import kotlin.collections.List
import kotlin.collections.MutableList
import kotlin.collections.forEach
import kotlin.collections.sortedDescending

class PersonalizeFoodLaunchActivity : AppCompatActivity(), (String) -> Unit {

    private var mJourneyDate : String = ""
    private var scheduleId : String= ""
    private var isFromPostBooking : Boolean = false
    private var foodList: ArrayList<FoodItems>? = null
    companion object {
        @JvmField var mCommonFoodsList : MutableList<Food> = ArrayList()
        @JvmField var mPersonalizationFoodId : MutableList<String> = ArrayList()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_food_preferences_launch)
        scheduleId = intent?.extras?.getString("ScheduleId" , "")!!
        mJourneyDate = intent?.extras?.getString("JourneyDate")!!
        isFromPostBooking = intent?.extras?.getBoolean("isPostBooking" , false)!!
        foodList = intent?.extras?.getParcelableArrayList<FoodItems>("selectedFoods")
        mCommonFoodsList.clear()
        mPersonalizationFoodId.clear()

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

        foodList?.forEach {
            if(!it.name.equals("standard" , true)){
                mPersonalizationFoodId.add(it.id.toString())
            }
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
                        showResponseDialog(response.body()!!.message)
                    }else{
                        mCommonFoodsList.addAll(response.body()!!.data)
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
                        UiUtils.showToast(this@PersonalizeFoodLaunchActivity ,
                            errorMessage)
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
                UiUtils.showServerErrorDialog(this@PersonalizeFoodLaunchActivity)
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

    private fun showResponseDialog(message : String) {
        val alertDialogBuilder = AlertDialog.Builder(this@PersonalizeFoodLaunchActivity)
        alertDialogBuilder.setMessage(message)
        alertDialogBuilder.setCancelable(false)

        alertDialogBuilder.setPositiveButton(
            "Ok"
        ) { arg0, arg1 -> finish() }

        val alertDialog = alertDialogBuilder.create()

        alertDialog.setOnShowListener { dialog ->
            alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(
                resources.getColor(R.color.colorButtonNew)
            )
        }

        alertDialog.show()
    }


}
