package com.ns.stellarjet.drawer

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.ns.stellarjet.R
import com.ns.stellarjet.utils.SharedPreferencesHelper
import com.ns.stellarjet.utils.UiUtils
import com.razorpay.Checkout
import com.razorpay.PaymentResultListener
import kotlinx.android.synthetic.main.activity_purchase.*
import org.json.JSONObject
import java.text.NumberFormat
import java.util.*


class PurchaseActivity : AppCompatActivity(), PaymentResultListener {


    private val mSeatPrice = 100000
    private var displayPrice: String = ""

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
                    val totalPrice = totalSeats.toIntOrNull()?.times(mSeatPrice)
                    val formatter = NumberFormat.getCurrencyInstance(Locale("en", "IN"))
                    displayPrice = formatter.format(totalPrice)
                    textView_purchase_pay_amount.text = displayPrice
                }else{
                    textView_purchase_pay_amount.text = ""
                }
            }
        })

        button_purchase_buy_now.setOnClickListener {
            val enterSeats = editText_purchase_seat_count.text.toString()
            if(enterSeats.isNotEmpty()){
                startPayment()
            }else{
                UiUtils.showToast(this@PurchaseActivity , "Please enter the seats required")
            }
        }
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
            options.put("description", "Order #123456")

            options.put("currency", "INR")

            /**
             * Amount is always passed in PAISE
             * Eg: "500" = Rs 5.00
             */
            options.put("amount", 100)

            checkout.open(activity, options)
        } catch (e: Exception) {
            Log.e("Passenger ", "Error in starting Razorpay Checkout", e)
        }

    }


    override fun onPaymentError(p0: Int, p1: String?) {
        UiUtils.showToast(this@PurchaseActivity , "Error : $p1")
    }

    override fun onPaymentSuccess(p0: String?) {
        UiUtils.showToast(this@PurchaseActivity , "Success : $p0")
    }
}
