package com.ns.stellarjet.drawer

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
import com.ns.networking.model.foods.FoodCategorySelection
import com.ns.networking.model.foods.FoodDaysSelection
import com.ns.networking.model.foods.GlobalFoodPrefResponse
import com.ns.networking.retrofit.RetrofitAPICaller
import com.ns.stellarjet.R
import com.ns.stellarjet.drawer.adapter.FoodDaysAdapter
import com.ns.stellarjet.home.HomeActivity
import com.ns.stellarjet.utils.Progress
import com.ns.stellarjet.utils.SharedPreferencesHelper
import com.ns.stellarjet.utils.UiUtils
import kotlinx.android.synthetic.main.activity_global_food_selection.*
import org.json.JSONArray
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class GlobalFoodSelectionActivity : AppCompatActivity() {


    private val mSelectedFoodList = mutableListOf<FoodCategorySelection>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(com.ns.stellarjet.R.layout.activity_global_food_selection)

        makeFoodListLayout()
        fillFoodListLayout()

        button_global_food_back.setOnClickListener {
            onBackPressed()
        }

        button_global_food_confirm.setOnClickListener {
            val jsonArray = JSONArray()
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
                    jsonArray.put(mIndividualJsonObject)
                } else {
                    val daysJsonArray = JSONArray()
                    mIndividualJsonObject.put("days", daysJsonArray)
                    jsonArray.put(mIndividualJsonObject)
                }
            }

            Log.d("GlobalFood", jsonArray.toString())
            updateFoodPreferences(jsonArray)
        }
    }

    private fun updateFoodPreferences(jsonArray: JSONArray) {
        val progress = Progress.getInstance()
        progress.showProgress(this@GlobalFoodSelectionActivity)
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
                progress.hideProgress()
                if (response.code() == 200) {
                    UiUtils.showToast(
                        this@GlobalFoodSelectionActivity,
                        response.body()?.message!!
                    )
                    for (i in 0 until mSelectedFoodList.size) {
                        val foodType = mSelectedFoodList[i].foodType
                        HomeActivity.sUserData.customer_prefs.food_prefs[i].food_category = foodType
                        HomeActivity.sUserData.customer_prefs.food_prefs[i].days.clear()
                        for (foodDays in mSelectedFoodList[i].getmFoodDaysSelection()) {
                            var daysList = mutableListOf<String>()
                            if (foodDays.isSelected) {
                                daysList.add(foodDays.daysName)
                                HomeActivity.sUserData.customer_prefs.food_prefs[i].days.add(foodDays.daysName)
                            }
                        }
                    }
                    Log.d("Food", HomeActivity.sUserData.customer_prefs.toString())
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
                progress.hideProgress()
                UiUtils.showServerErrorDialog(this@GlobalFoodSelectionActivity)
            }
        })
    }

    private fun makeFoodListLayout() {
        val foodCount = HomeActivity.sUserData.customer_prefs.food_categories.size
        val layoutInflater: LayoutInflater = LayoutInflater.from(this@GlobalFoodSelectionActivity)

        for (i in 0 until foodCount) {
            val v = layoutInflater.inflate(R.layout.layout_row_global_food, null)
            v.tag = "FoodItems $i"
            if (!HomeActivity.sUserData.customer_prefs.food_categories[i].show_days) {
                val daysLayout = v.findViewById<LinearLayout>(com.ns.stellarjet.R.id.layout_row_global_days)
                daysLayout.visibility = View.GONE
            }
            layout_global_food.addView(v)
            val foodType = HomeActivity.sUserData.customer_prefs.food_categories[i].cat_key
            var isSelected = false
            val mFoodDaysList = mutableListOf<FoodDaysSelection>()
            mFoodDaysList.add(FoodDaysSelection("Sunday", false))
            mFoodDaysList.add(FoodDaysSelection("Monday", false))
            mFoodDaysList.add(FoodDaysSelection("Tuesday", false))
            mFoodDaysList.add(FoodDaysSelection("Wednesday", false))
            mFoodDaysList.add(FoodDaysSelection("Thursday", false))
            mFoodDaysList.add(FoodDaysSelection("Friday", false))
            mFoodDaysList.add(FoodDaysSelection("Saturday", false))
            // Commented By Ashwani
            /*if (HomeActivity.sUserData.customer_prefs.food_prefs.isNotEmpty() &&
                HomeActivity.sUserData.customer_prefs.food_prefs[i].days.isNotEmpty()
            ) {*/

            // Added By Ashwani
            var dd = 0;

            isSelected = true
            for (day in HomeActivity.sUserData.customer_prefs.food_prefs[dd].days) {
                for (it in mFoodDaysList) {
                    if (day.equals(it.daysName, true)) {
                        it.isSelected = true
                    }
                }
                dd++;
            }
            //}
            val foodCategorySelection = FoodCategorySelection(foodType, isSelected, mFoodDaysList)
            mSelectedFoodList.add(foodCategorySelection)
        }
    }

    private fun fillFoodListLayout() {
        val foodCount = layout_global_food.childCount
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
            val foodType = HomeActivity.sUserData.customer_prefs.food_categories[i].cat_text
            textViewFoodName.text = foodType
            val foodTypeHeading = "Restrict $foodType for these days"
            textViewFoodTypeHeading.text = foodTypeHeading
            val foodPrefSize = HomeActivity.sUserData.customer_prefs.food_prefs.size
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
}
