package com.ns.stellarjet.drawer

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.ns.networking.model.FoodPref
import com.ns.networking.model.LoginResponse
import com.ns.networking.model.foods.FoodCategorySelection
import com.ns.networking.model.foods.FoodDaysSelection
import com.ns.networking.model.foods.GlobalFoodPrefResponse
import com.ns.networking.retrofit.RetrofitAPICaller
import com.ns.stellarjet.R
import com.ns.stellarjet.TouchIdActivity
import com.ns.stellarjet.drawer.adapter.FoodDaysAdapter
import com.ns.stellarjet.home.HomeActivity
import com.ns.stellarjet.utils.Progress
import com.ns.stellarjet.utils.SharedPreferencesHelper
import com.ns.stellarjet.utils.UIConstants
import com.ns.stellarjet.utils.UiUtils
import kotlinx.android.synthetic.main.activity_global_food_selection.*
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException

class GlobalFoodSelectionActivity : AppCompatActivity() {


    private val mSelectedFoodList = mutableListOf<FoodCategorySelection>()
    private var jsonArray: JSONArray? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(com.ns.stellarjet.R.layout.activity_global_food_selection)

        makeFoodListLayout()
        fillFoodListLayout()

        button_global_food_back.setOnClickListener {
            onBackPressed()
        }

        button_global_food_confirm.setOnClickListener {
            jsonArray = JSONArray()
            for (it in mSelectedFoodList) {
                val mIndividualJsonObject = JSONObject()
                mIndividualJsonObject.put("food_category", it.foodType)
                if (it.isSelected) {
                    val daysJsonArray = JSONArray()
                    for (foodDays in it.getmFoodDaysSelection()) {
                        if (foodDays.isSelected) {
                            daysJsonArray.put(foodDays.daysName)
                        }
                    }
                    mIndividualJsonObject.put("days", daysJsonArray)
                    jsonArray?.put(mIndividualJsonObject)
                } /*else {
                    val daysJsonArray = JSONArray()
                    mIndividualJsonObject.put("days", daysJsonArray)
                    jsonArray.put(mIndividualJsonObject)
                }*/
            }

            Log.d("GlobalFood", jsonArray.toString())
            updateFoodPreferences(jsonArray!!)
        }
    }

    var progress: Progress? = null

    private fun updateFoodPreferences(jsonArray: JSONArray) {
        progress = Progress.getInstance()
        progress?.showProgress(this@GlobalFoodSelectionActivity)
        val upcomingBook: Call<GlobalFoodPrefResponse> = RetrofitAPICaller.getInstance(this@GlobalFoodSelectionActivity)
            .stellarJetAPIs.updateGlobalFoodPreferences(
            SharedPreferencesHelper.getUserToken(this@GlobalFoodSelectionActivity),
            jsonArray
        )

        upcomingBook.enqueue(object : Callback<GlobalFoodPrefResponse> {
            override fun onResponse(
                call: Call<GlobalFoodPrefResponse>,
                response:
                Response<GlobalFoodPrefResponse>
            ) {

                progress?.hideProgress()

                if (response.code() == 200) {

//                    getUserData();
                    UiUtils.showToast(
                        this@GlobalFoodSelectionActivity,
                        response.body()?.message!!
                    )

                    Log.i("", ""+jsonArray)

                    HomeActivity.sUserData!!.customer_prefs.food_prefs.clear()

                    for (i in 0..(jsonArray.length() - 1)) {
                        val item = jsonArray.getJSONObject(i)
                        var cat = item.get("food_category").toString()
                        if(item.has("days")) {
                            var days = item.getJSONArray("days")
                            var daysList = mutableListOf<String>()
                            for(d in 0 until  days.length()) {
                                daysList.add(days[d].toString())
                            }
                            var foodPref: FoodPref? = FoodPref(daysList, cat)
                            HomeActivity.sUserData!!.customer_prefs.food_prefs.add(foodPref!!)
                        } else {
                            var daysList = mutableListOf<String>()
                            var foodPref: FoodPref? = FoodPref(daysList, cat)
                            HomeActivity.sUserData!!.customer_prefs.food_prefs.add(foodPref!!)
                        }

                        Log.i("", ""+item)

                        // Your code here
                    }


                    /*for (i in 0 until mSelectedFoodList.size) {
                        val foodType = mSelectedFoodList[i].foodType!!
                        HomeActivity.sUserData!!.customer_prefs.food_prefs[i].food_category = foodType
                        HomeActivity.sUserData!!.customer_prefs.food_prefs[i].days.clear()
                        for (foodDays in mSelectedFoodList[i].getmFoodDaysSelection()) {
                            var daysList = mutableListOf<String>()
                            if (foodDays.isSelected) {
                                daysList.add(foodDays.daysName)
                                HomeActivity.sUserData!!.customer_prefs.food_prefs[i].days.add(foodDays.daysName)
                            }
                        }
                    }*/
                    Log.d("Food", HomeActivity.sUserData!!.customer_prefs.toString())



                } else {
                    UiUtils.showToast(
                        this@GlobalFoodSelectionActivity,
                        "Please try again later"
                    )

                }
                finish()
            }

            override fun onFailure(call: Call<GlobalFoodPrefResponse>, t: Throwable) {
                Log.d("Booking", "onResponse: $t")
                progress?.hideProgress()
                UiUtils.showServerErrorDialog(this@GlobalFoodSelectionActivity)
            }
        })
    }

    private fun makeFoodListLayout() {
        val foodCategoryCount = HomeActivity.sUserData!!.customer_prefs.food_categories.size
        var foodPrefCount = HomeActivity.sUserData!!.customer_prefs.food_prefs.size
        val layoutInflater: LayoutInflater = LayoutInflater.from(this@GlobalFoodSelectionActivity)

        var foodPrefList = HomeActivity.sUserData!!.customer_prefs.food_prefs
        var foodCategoryList = HomeActivity.sUserData!!.customer_prefs.food_categories

        for (i in 0 until foodCategoryCount) {
            /*if(i==foodPrefCount) {
                break;
            }*/
            val v = layoutInflater.inflate(R.layout.layout_row_global_food, null)
            v.tag = "FoodItems $i"
            if (!HomeActivity.sUserData!!.customer_prefs.food_categories[i].show_days) {
                val daysLayout = v.findViewById<LinearLayout>(com.ns.stellarjet.R.id.layout_row_global_days)
                daysLayout.visibility = View.GONE
            }
            layout_global_food.addView(v)
            val foodType = HomeActivity.sUserData!!.customer_prefs.food_categories[i].cat_key
            val mFoodDaysList = mutableListOf<FoodDaysSelection>()
            var daylist = mutableListOf<String>()
            var foodPref = FoodPref(daylist, foodCategoryList[i].cat_key)

            var coa = foodPrefList.contains(foodPref)
            var containSunday = false
            var containMonday = false
            var containTuesday = false
            var containWednesday = false
            var containThursday = false
            var containFriday = false
            var containSaturday = false

            if(coa) {
                var index = foodPrefList.indexOf(foodPref)
                if(index != -1) {
                    containSunday = HomeActivity.sUserData!!.customer_prefs.food_prefs[index].days.contains("Sunday")
                    containMonday = HomeActivity.sUserData!!.customer_prefs.food_prefs[index].days.contains("Monday")
                    containTuesday = HomeActivity.sUserData!!.customer_prefs.food_prefs[index].days.contains("Tuesday")
                    containWednesday =
                        HomeActivity.sUserData!!.customer_prefs.food_prefs[index].days.contains("Wednesday")
                    containThursday =
                        HomeActivity.sUserData!!.customer_prefs.food_prefs[index].days.contains("Thursday")
                    containFriday = HomeActivity.sUserData!!.customer_prefs.food_prefs[index].days.contains("Friday")
                    containSaturday =
                        HomeActivity.sUserData!!.customer_prefs.food_prefs[index].days.contains("Saturday")
                }

            }

                Log.i("", "")

                mFoodDaysList.add(
                    FoodDaysSelection(
                        "Sunday",
                        containSunday
                    )
                )
                mFoodDaysList.add(
                    FoodDaysSelection(
                        "Monday",
                        containMonday
                    )
                )
                mFoodDaysList.add(
                    FoodDaysSelection(
                        "Tuesday",
                        containTuesday
                    )
                )
                mFoodDaysList.add(
                    FoodDaysSelection(
                        "Wednesday",
                        containWednesday
                    )
                )
                mFoodDaysList.add(
                    FoodDaysSelection(
                        "Thursday",
                        containThursday
                    )
                )
                mFoodDaysList.add(
                    FoodDaysSelection(
                        "Friday",
                        containFriday
                    )
                )
                mFoodDaysList.add(
                    FoodDaysSelection(
                        "Saturday",
                        containSaturday
                    )
                )


                // Commented By Ashwani
            /*if (HomeActivity.sUserData.customer_prefs.food_prefs.isNotEmpty() &&
                HomeActivity.sUserData.customer_prefs.food_prefs[i].days.isNotEmpty()
            ) {*/

            // Added By Ashwani
            /*var dd = 0;

            var isSelected = false
            for (day in HomeActivity.sUserData!!.customer_prefs.food_prefs[dd].days) {
                for (it in mFoodDaysList) {
                    if (day.equals(it.daysName, true)) {
                        it.isSelected = true
                    }
                }
                dd++;
            }*/
            //}
            val foodCategorySelection = FoodCategorySelection(foodType, coa, mFoodDaysList)
            mSelectedFoodList.add(foodCategorySelection)
            Log.i("", "")
        }
    }

    private fun fillFoodListLayout() {
        val foodCount = layout_global_food.childCount
        var foodPrefCount = HomeActivity.sUserData!!.customer_prefs.food_prefs.size

        var foodPrefList = HomeActivity.sUserData!!.customer_prefs.food_prefs
        var foodCategoryList = HomeActivity.sUserData!!.customer_prefs.food_categories

        for (i in 0 until foodCount) {
            val textViewFoodName = layout_global_food.getChildAt(i)
                .findViewById<TextView>(com.ns.stellarjet.R.id.textView_row_global_food_name)
            val textViewFoodTypeHeading = layout_global_food.getChildAt(i)
                .findViewById<TextView>(com.ns.stellarjet.R.id.textView_row_global_food_days_heading)
            val daysLayout = layout_global_food.getChildAt(i)
                .findViewById<LinearLayout>(com.ns.stellarjet.R.id.layout_row_global_days)
            val foodsNameLayout = layout_global_food.getChildAt(i)
                .findViewById<LinearLayout>(com.ns.stellarjet.R.id.layout_row_global_food_name)
            val daysImageView = layout_global_food.getChildAt(i)
                .findViewById<ImageView>(com.ns.stellarjet.R.id.imageView_row_global_days)
            val foodNameImageView = layout_global_food.getChildAt(i)
                .findViewById<ImageView>(com.ns.stellarjet.R.id.imageView_row_global_food_name)
            val daysRecyclerView = layout_global_food.getChildAt(i)
                .findViewById<RecyclerView>(com.ns.stellarjet.R.id.recyclerView_food_days)
            val foodType = HomeActivity.sUserData!!.customer_prefs.food_categories[i].cat_text



            var daylist = mutableListOf<String>()
            var foodPref = FoodPref(daylist, foodCategoryList[i].cat_key)
//            var isSelectedByUser = HomeActivity.sUserData!!.customer_prefs.food_prefs[i].days.size > 0
            var isSelectedByUser = foodPrefList.contains(foodPref)


            mSelectedFoodList[i].isSelected = isSelectedByUser;
            textViewFoodName.text = foodType
            val foodTypeHeading = "Restrict $foodType for these days"
            textViewFoodTypeHeading.text = foodTypeHeading
            val foodPrefSize = HomeActivity.sUserData!!.customer_prefs.food_prefs.size
            if (mSelectedFoodList[i].isSelected) {
                foodNameImageView.setImageResource(R.mipmap.ic_food_select)
            } else {
                foodNameImageView.setImageResource(R.mipmap.ic_food_unselect)
            }
            foodsNameLayout.setOnClickListener {
                if (mSelectedFoodList[i].isSelected) {
                    foodNameImageView.setImageResource(R.mipmap.ic_food_unselect)
                    mSelectedFoodList[i].isSelected = false
                    daysRecyclerView.visibility = View.GONE
                    daysImageView.setImageDrawable(getDrawable(R.drawable.ic_down))
                } else {
                    foodNameImageView.setImageResource(R.mipmap.ic_food_select)
                    mSelectedFoodList[i].isSelected = true
                }
            }
            daysLayout.setOnClickListener {
                if (mSelectedFoodList[i].isSelected) {
                    scrollView_global_food.fullScroll(View.FOCUS_DOWN)
                    if (daysRecyclerView.visibility == View.GONE) {
                        daysImageView.setImageDrawable(getDrawable(R.drawable.ic_up))
                        daysRecyclerView.visibility = View.VISIBLE
                    } else {
                        daysImageView.setImageDrawable(getDrawable(R.drawable.ic_down))
                        daysRecyclerView.visibility = View.GONE
                    }
                }
            }
            val foodDaysAdapter = FoodDaysAdapter(mSelectedFoodList[i].getmFoodDaysSelection(),
                onSelectUsersListener = { foodDaysSelection: FoodDaysSelection, position: Int ->
                    mSelectedFoodList[i].getmFoodDaysSelection()[position].daysName = foodDaysSelection.daysName
                    mSelectedFoodList[i].getmFoodDaysSelection()[position].isSelected = foodDaysSelection.isSelected
                })
            val layoutManager = LinearLayoutManager(
                this@GlobalFoodSelectionActivity,
                RecyclerView.HORIZONTAL,
                false
            )

            daysRecyclerView.layoutManager = layoutManager
            daysRecyclerView.adapter = foodDaysAdapter
        }
    }


    private fun getUserData() {
        val mCustomerDataResponseCall = RetrofitAPICaller.getInstance(this@GlobalFoodSelectionActivity)
            .stellarJetAPIs.getCustomerData(
            SharedPreferencesHelper.getUserToken(this@GlobalFoodSelectionActivity)
        )

        mCustomerDataResponseCall.enqueue(object : Callback<LoginResponse> {
            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                progress?.hideProgress()
                if (response.body() != null) {
                    Log.d("Splash", "onResponse: " + response.body()!!)
                    /* save seat count */
                    SharedPreferencesHelper.saveSeatCount(
                        this@GlobalFoodSelectionActivity,
                        response.body()!!.data.user_data.customer_prefs.seats_available
                    )
                    val prepaidTerms = response.body()!!.data.user_data.customer_prefs.membership_details
                        .prepaid_terms
                    if (prepaidTerms.equals("true", ignoreCase = true)) {
                        SharedPreferencesHelper.saveMembershipType(
                            this@GlobalFoodSelectionActivity,
                            UIConstants.PREFERENCES_MEMBERSHIP_SUBSCRIPTION
                        )
                    } else if (prepaidTerms.equals("false", ignoreCase = true)) {
                        SharedPreferencesHelper.saveMembershipType(
                            this@GlobalFoodSelectionActivity,
                            UIConstants.PREFERENCES_MEMBERSHIP_PAY_AS_U_GO
                        )
                    }

                    val seatCost = response.body()!!.data.user_data.customer_prefs
                        .membership_details.seat_cost
                    SharedPreferencesHelper.saveSeatCost(this@GlobalFoodSelectionActivity, Integer.valueOf(seatCost))

                    finish()
                } else {
                    try {
                        val mJsonObject = JSONObject(response.errorBody()!!.string())
                        val errorMessage = mJsonObject.getString("message")
                        if (errorMessage.equals(UIConstants.USER_TOKEN_EXPIRY, ignoreCase = true)) {
                            //                            getNewToken();
                        } else {
                            UiUtils.showSimpleDialog(
                                this@GlobalFoodSelectionActivity, errorMessage
                            )
                        }
                    } catch (e: JSONException) {
                        e.printStackTrace()
                    } catch (e: IOException) {
                        e.printStackTrace()
                    }

                }

            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                Log.d("splash", "onFailure: $t")
                progress?.hideProgress()
            }
        })
    }


}
