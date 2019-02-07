package com.ns.stellarjet.personalize

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ns.networking.model.Food
import com.ns.networking.model.FoodPersonalizeResponse
import com.ns.networking.retrofit.RetrofitAPICaller
import com.ns.stellarjet.R
import com.ns.stellarjet.personalize.PersonalizeFoodLaunchActivity.Companion.mCommonFoodsList
import com.ns.stellarjet.personalize.PersonalizeFoodLaunchActivity.Companion.mPersonalizationFoodId
import com.ns.stellarjet.personalize.PersonalizeLaunchActivity.Companion.mPersonalizationSelectedFoodIds
import com.ns.stellarjet.personalize.adapter.FoodListAdapter
import com.ns.stellarjet.utils.Progress
import com.ns.stellarjet.utils.SharedPreferencesHelper
import com.ns.stellarjet.utils.UIConstants
import kotlinx.android.synthetic.main.activity_food_preference_list.*
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException

class PersonalizeFoodListActivity : AppCompatActivity(), (String, Boolean, Int) -> Unit {

    private var mSelectedFoodIds : MutableList<String> = ArrayList()
    private var mFoodsList: List<Food>? = ArrayList()
    private var isFromPostBooking : Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_food_preference_list)

        val foodType : String = intent.extras?.getString(UIConstants.BUNDLE_FOOD_TYPE)!!
        isFromPostBooking = intent?.extras?.getBoolean("isPostBooking" , false)!!

        textView_food_list_name.text = foodType
        val foodListAdapter = FoodListAdapter(
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

        button_food_list_confirm.setOnClickListener {
           /* if(mSelectedFoodIds.size==0){
                Toast.makeText(
                    this@PersonalizeFoodListActivity ,
                    "please select at least one food",
                    Toast.LENGTH_SHORT).show()
            }*/
            if(isFromPostBooking){
                if(mPersonalizationSelectedFoodIds == mSelectedFoodIds){
                    finish()
                }else{
                    personalizeFood()
                }
            }else{
                if(mPersonalizationFoodId == mSelectedFoodIds){
                    finish()
                }else{
                    personalizeFood()
                }
            }
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

        if(isFromPostBooking){
            mFoodsDisplayList.forEach {
                it.pref = mPersonalizationSelectedFoodIds.contains(it.id.toString())
            }
            mPersonalizationSelectedFoodIds.forEach {
                mSelectedFoodIds.add(it)
            }
        }else{
            mFoodsDisplayList.forEach {
                it.pref = mPersonalizationFoodId.contains(it.id.toString())
            }
            mPersonalizationFoodId.forEach {
                mSelectedFoodIds.add(it)
            }
        }


        return mFoodsDisplayList
    }

    private fun personalizeFood(){
        val progress = Progress.getInstance()
        progress.showProgress(this)
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
                progress.hideProgress()
                if(response.code() == 200){
                    Log.d("Booking", "onResponse: $response")
                    if(mSelectedFoodIds.size == 0){
                        SharedPreferencesHelper.saveFoodPersonalize(
                            this@PersonalizeFoodListActivity ,
                            false
                        )
                    }else{
                        SharedPreferencesHelper.saveFoodPersonalize(
                            this@PersonalizeFoodListActivity ,
                            true
                        )
                    }
                    if(isFromPostBooking){
                        PersonalizeLaunchActivity.mPersonalizationSelectedFoodIds = mSelectedFoodIds
                    }else{
                        mPersonalizationFoodId = mSelectedFoodIds
                    }
                    Toast.makeText(
                        this@PersonalizeFoodListActivity,
                        response.body()?.message,
                        Toast.LENGTH_SHORT
                    ).show()
                    finish()
                    if(isFromPostBooking){
                        if(SharedPreferencesHelper.getCabPersonalize(this@PersonalizeFoodListActivity)){
                            val mPersonalizeSuccessIntent  =  Intent(
                                this@PersonalizeFoodListActivity ,
                                PersonalizeSuccessActivity::class.java
                            )
                            mPersonalizeSuccessIntent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                            startActivity(mPersonalizeSuccessIntent)
                            finish()
                            clearPersonalizedPreferences()
                        }else{
                            finish()
                        }
                    }
                }else if(response.code() == 400){
                    try {
                        val mJsonObject = JSONObject(response.errorBody()!!.string())
                        val errorMessage = mJsonObject.getString("error_message")
                        Toast.makeText(this@PersonalizeFoodListActivity, errorMessage, Toast.LENGTH_LONG).show()
                    } catch (e: JSONException) {
                        e.printStackTrace()
                    } catch (e: IOException) {
                        e.printStackTrace()
                    }
                }else if(response.code() == 500){
                    Toast.makeText(this@PersonalizeFoodListActivity, "Please try again later", Toast.LENGTH_LONG).show()
                }
            }

            override fun onFailure(call: Call<FoodPersonalizeResponse>, t: Throwable) {
                progress.hideProgress()
                Log.d("Booking", "onResponse: $t")
                Toast.makeText(this@PersonalizeFoodListActivity , "Server Error" , Toast.LENGTH_SHORT).show()
            }
        })
    }

    override fun invoke(selectedID: String, isSelected: Boolean, position: Int) {
        if(mSelectedFoodIds.contains(selectedID)){
            mSelectedFoodIds.remove(selectedID)
        }else{
            mSelectedFoodIds.add(selectedID)
        }
    }

    private fun clearPersonalizedPreferences(){
        SharedPreferencesHelper.saveCabDropPersonalize(this , "")
        SharedPreferencesHelper.saveCabDropPersonalizeID(this , "")
        SharedPreferencesHelper.saveCabPickupPersoalizeID(this , "")
        SharedPreferencesHelper.saveCabPickupPersoalize(this , "")
        SharedPreferencesHelper.saveBookingId(this , "")
        SharedPreferencesHelper.saveFoodPersonalize(this ,false)
        SharedPreferencesHelper.saveCabPersonalize(this ,false)
    }
}
