package com.ns.stellarjet.utils

import android.content.Context
import android.view.Gravity
import android.widget.Toast

class ToastUtils{

    companion object {
        fun showToast(context : Context , message : String){
            val toast = Toast.makeText(
                context,
                message
                , Toast.LENGTH_LONG)
            toast.setGravity(Gravity.CENTER, 0, 0)
            toast.show()
        }
    }
}