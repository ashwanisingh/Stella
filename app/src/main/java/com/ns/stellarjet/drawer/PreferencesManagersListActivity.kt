package com.ns.stellarjet.drawer

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ns.networking.model.secondaryusers.DeactivateSecondaryUserResponse
import com.ns.networking.model.secondaryusers.SecondaryUserInfoList
import com.ns.networking.model.secondaryusers.SecondaryUsersListResponse
import com.ns.networking.retrofit.RetrofitAPICaller
import com.ns.stellarjet.databinding.ActivityPreferencesManagersAddBinding
import com.ns.stellarjet.drawer.adapter.SecondaryUserListAdapter
import com.ns.stellarjet.utils.Progress
import com.ns.stellarjet.utils.SharedPreferencesHelper
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class PreferencesManagersListActivity : AppCompatActivity(), (SecondaryUserInfoList) -> Unit,
        (SecondaryUserInfoList, Int) -> Unit {

    private lateinit var mSecondaryListBinding : ActivityPreferencesManagersAddBinding
    private var mSecondaryUsersList : MutableList<SecondaryUserInfoList> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(com.ns.stellarjet.R.layout.activity_preferences_managers_add)

        mSecondaryListBinding = DataBindingUtil.setContentView(
            this ,
            com.ns.stellarjet.R.layout.activity_preferences_managers_add
        )

        /*mSecondaryListBinding.buttonManagersAdd.setOnClickListener {
            var managersBottomFragment = ManagersBottomFragment()
            managersBottomFragment.show(supportFragmentManager , managersBottomFragment.tag)
        }*/
        mSecondaryListBinding.buttonManagerAddBack.setOnClickListener {
            onBackPressed()
        }

        mSecondaryListBinding.buttonManagersAdd.setOnClickListener {

            val mAddSecondaryUserIntent = Intent(
                this ,
                PreferenceManagerInfoActivity::class.java
            )
            mAddSecondaryUserIntent.putExtra("isAddSecondaryUser" , true)
            startActivity(mAddSecondaryUserIntent)
        }
    }

    override fun onResume() {
        super.onResume()
        mSecondaryUsersList.clear()
        getSecondaryUsersList()
    }

    private fun getSecondaryUsersList(){
        val progress = Progress.getInstance()
        progress.showProgress(this)
        val secondaryUserCallList: Call<SecondaryUsersListResponse> = RetrofitAPICaller.getInstance(this)
            .stellarJetAPIs.getSecondaryUsersList(
            SharedPreferencesHelper.getUserToken(this),
            1
        )

        secondaryUserCallList.enqueue(object : Callback<SecondaryUsersListResponse> {
            override fun onResponse(
                call: Call<SecondaryUsersListResponse>,
                response:
                Response<SecondaryUsersListResponse>) {
                progress.hideProgress()
                if(response.body()!=null && response.body()!!.resultcode == 1){
                    mSecondaryUsersList.addAll(response.body()!!.data.secondary_users)
                    val mSecondaryUserListAdapter = SecondaryUserListAdapter(
                        mSecondaryUsersList ,
                        this@PreferencesManagersListActivity ,
                        this@PreferencesManagersListActivity
                    )

                    val layoutManager = LinearLayoutManager(
                        this@PreferencesManagersListActivity ,
                        RecyclerView.VERTICAL ,
                        false
                    )

                    mSecondaryListBinding.recyclerViewManagers.adapter = mSecondaryUserListAdapter
                    mSecondaryListBinding.recyclerViewManagers.layoutManager = layoutManager

                }
            }

            override fun onFailure(call: Call<SecondaryUsersListResponse>, t: Throwable) {
                Log.d("Booking", "onResponse: $t")
                progress.hideProgress()
                Toast.makeText(this@PreferencesManagersListActivity , "Server Error" , Toast.LENGTH_SHORT).show()
            }
        })
    }


    /***
     * when the itemView is clicked
     * */
    override fun invoke(p1: SecondaryUserInfoList) {
        val mAddSecondaryUserIntent = Intent(
            this ,
            PreferenceManagerInfoActivity::class.java
        )
        mAddSecondaryUserIntent.putExtra("isAddSecondaryUser" , false)
        startActivity(mAddSecondaryUserIntent)
    }


    /**
     * when each row overflow icon is clicked
     */
    override fun invoke(p1: SecondaryUserInfoList, index: Int) {
        /* val managersBottomFragment = ManagersBottomFragment()
         val bundle = Bundle()
         bundle.putString("SecondaryName" , p1.su_name)
         bundle.putInt("SecondaryId" , p1.su_id)
         managersBottomFragment.arguments = bundle
         managersBottomFragment.show(supportFragmentManager , managersBottomFragment.tag)*/
        showDeleteConfirmationDialog(p1.su_id , p1.su_name , index)
    }


    private fun showDeleteConfirmationDialog(id: Int, name: String, index: Int) {
        val alertDialogBuilder = AlertDialog.Builder(this@PreferencesManagersListActivity)
        alertDialogBuilder.setMessage("Are you sure want to delete $name ?")
        alertDialogBuilder.setPositiveButton(
            "Ok"
        ) { arg0, arg1 -> deleteUser(id , name , index) }
        alertDialogBuilder.setNegativeButton("Cancel") { dialog, which -> dialog.dismiss() }
        val alertDialog = alertDialogBuilder.create()
        alertDialog.setOnShowListener { dialog ->
            alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(
                resources.getColor(com.ns.stellarjet.R.color.colorButtonNew)
            )
            alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(
                resources.getColor(com.ns.stellarjet.R.color.colorButtonNew)
            )
        }
        alertDialog.show()
    }

    private fun deleteUser(id: Int, name: String, index: Int) {
        val progress = Progress.getInstance()
        progress.showProgress(this@PreferencesManagersListActivity)
        val deactivateSecondaryUserResponseCall =
            RetrofitAPICaller.getInstance(this@PreferencesManagersListActivity)
                .stellarJetAPIs.deactivateSecondaryUser(
                SharedPreferencesHelper.getUserToken(this@PreferencesManagersListActivity),
                id,
                2
            )

        deactivateSecondaryUserResponseCall.enqueue(object : Callback<DeactivateSecondaryUserResponse> {
            override fun onResponse(
                call: Call<DeactivateSecondaryUserResponse>,
                response: Response<DeactivateSecondaryUserResponse>
            ) {
                progress.hideProgress()
                if (response.body() != null) {
                    Log.d("ManagersBottomFragment", "onResponse: " + response.body()!!)
                    if (response.body()!!.resultcode == 1) {
                        Toast.makeText(
                            this@PreferencesManagersListActivity,
                            "$name Deleted", Toast.LENGTH_SHORT
                        ).show()
                        mSecondaryUsersList.removeAt(index)
                        mSecondaryListBinding.recyclerViewManagers.adapter?.notifyItemRemoved(index)
                    }
                }
            }

            override fun onFailure(call: Call<DeactivateSecondaryUserResponse>, t: Throwable) {
                progress.hideProgress()
                Log.d("ManagersBottomFragment", "onFailure: $t")
            }
        })
    }

}
