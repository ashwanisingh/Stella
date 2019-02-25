package com.ns.stellarjet.drawer

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.ns.networking.model.PurchaseSeatsResponse
import com.ns.networking.model.VerifyPurchaseResponse
import com.ns.networking.retrofit.RetrofitAPICaller
import com.ns.stellarjet.R
import com.ns.stellarjet.utils.Progress
import com.ns.stellarjet.utils.SharedPreferencesHelper
import com.ns.stellarjet.utils.UiUtils
import com.razorpay.Checkout
import com.razorpay.PaymentResultListener
import kotlinx.android.synthetic.main.activity_purchase.*
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.NumberFormat
import java.util.*


class PurchaseActivity : AppCompatActivity(), PaymentResultListener {


    private val mSeatPrice = 100000
    private var totalPrice: Int = 0
    private var mTotalSeats:Int = 0
    private var mPurchaseId:Int = 0
    private var mPaymentId : String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(com.ns.stellarjet.R.layout.activity_purchase)

        /**
         * Preload payment resources
         */
        Checkout.preload(applicationContext)

        editText_purchase_seat_count.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(s: Editable) {
                val totalSeats = s.toString()
                if(totalSeats.isNotEmpty()){
                    totalPrice = totalSeats.toIntOrNull()?.times(mSeatPrice)!!
                    val formatter = NumberFormat.getCurrencyInstance(Locale("en", "IN"))
                    val displayPrice = formatter.format(totalPrice)
                    textView_purchase_pay_amount.text = displayPrice
                }else{
                    textView_purchase_pay_amount.text = ""
                }
            }
        })

        button_purchase_buy_now.setOnClickListener {
            mTotalSeats = editText_purchase_seat_count.text.toString().toInt()
            if(mTotalSeats !=0){
//                startPayment()
                getOrderId()
            }else{
                UiUtils.showToast(this@PurchaseActivity , "Please enter the seats required")
            }
        }
    }

    private fun getOrderId(){
        val progress = Progress.getInstance()
        progress.showProgress(this@PurchaseActivity)
        val getOrderIdCall: Call<PurchaseSeatsResponse> = RetrofitAPICaller.getInstance(this@PurchaseActivity)
            .stellarJetAPIs.getOrderId(
            SharedPreferencesHelper.getUserToken(this@PurchaseActivity),
            mTotalSeats
        )

        getOrderIdCall.enqueue(object : Callback<PurchaseSeatsResponse> {
            override fun onResponse(
                call: Call<PurchaseSeatsResponse>,
                response:
                Response<PurchaseSeatsResponse>) {
                progress.hideProgress()
                if(response.body()!=null){
                    mPurchaseId = response.body()!!.data.purchase_id
                    startPayment()
                }
            }

            override fun onFailure(call: Call<PurchaseSeatsResponse>, t: Throwable) {
                Log.d("Booking", "onResponse: $t")
                progress.hideProgress()
                UiUtils.showServerErrorDialog(this@PurchaseActivity)
            }
        })
    }

    fun startPayment() {
        /**
         * Instantiate Checkout
         */
        val checkout = Checkout()

        /**
         * Set your logo here
         */
        checkout.setImage(R.mipmap.ic_stellar_launcher)

        /**
         * Reference to current activity
         */
        val activity = this@PurchaseActivity

        /**
         * Pass your payment options to the Razorpay Checkout as a JSONObject
         */
        try {
            val options = JSONObject()

            /**
             * Merchant Name
             * eg: ACME Corp || HasGeek etc.
             */
            options.put("name", resources.getString(R.string.app_name))

            val prefillDate = JSONObject()
            prefillDate.put("contact", SharedPreferencesHelper.getUserPhone(this@PurchaseActivity))
            prefillDate.put("email", SharedPreferencesHelper.getUserEmail(this@PurchaseActivity))

            options.put("prefill" , prefillDate)

            /**
             * Description can be anything
             * eg: Order #123123
             * Invoice Payment
             * etc.
             */
            options.put("description", mPurchaseId)

            options.put("currency", "INR")

            /**
             * Amount is always passed in PAISE
             * Eg: "500" = Rs 5.00
             */
            options.put("amount", mTotalSeats.times(100000).times(100))
//            options.put("amount", 100)

            checkout.open(activity, options)
        } catch (e: Exception) {
            Log.e("Passenger ", "Error in starting Razorpay Checkout", e)
        }

    }


    override fun onPaymentError(p0: Int, errorMessage: String?) {
        UiUtils.showToast(this@PurchaseActivity , "Error : $errorMessage")
    }

    override fun onPaymentSuccess(successMessage: String?) {
//        UiUtils.showToast(this@PurchaseActivity , "Success : $successMessage")
        Log.d("Purchase" , "onPaymentSuccess : $successMessage")
        mPaymentId = successMessage!!
        Log.d("Purchase" , "onPaymentSuccess : $mPurchaseId")
        verifyPurchase()
    }

    private fun verifyPurchase(){
        val progress = Progress.getInstance()
        progress.showProgress(this@PurchaseActivity)
        val getOrderIdCall: Call<VerifyPurchaseResponse> = RetrofitAPICaller.getInstance(this@PurchaseActivity)
            .stellarJetAPIs.verifyPurchase(
            SharedPreferencesHelper.getUserToken(this@PurchaseActivity),
            mPaymentId ,
            mPurchaseId.toString()
        )

        getOrderIdCall.enqueue(object : Callback<VerifyPurchaseResponse> {
            override fun onResponse(
                call: Call<VerifyPurchaseResponse>,
                response:
                Response<VerifyPurchaseResponse>) {
                progress.hideProgress()
                if(response.body()!=null){
                    UiUtils.showToast(this@PurchaseActivity ,
                        response.body()!!.message)
                    SharedPreferencesHelper.saveSeatCount(
                        this@PurchaseActivity ,
                        response.body()!!.data.updated_seats
                    )
                    finish()
                }
            }

            override fun onFailure(call: Call<VerifyPurchaseResponse>, t: Throwable) {
                Log.d("Booking", "onResponse: $t")
                progress.hideProgress()
                UiUtils.showServerErrorDialog(this@PurchaseActivity)
            }
        })
    }
}
