package com.ns.stellarjet.utils

import android.content.Context
import android.view.Gravity
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import com.ns.stellarjet.R


class UiUtils{

    companion object {
        fun showToast(context : Context, message : String){
            val toast = Toast.makeText(
                context,
                message
                , Toast.LENGTH_LONG)
            toast.setGravity(Gravity.CENTER, 0, 0)
            toast.show()
        }

        /**
         * shows the simple dialog with ok button
         */
        fun showSimpleDialog(context: Context, message :String){
            val alertDialogBuilder = AlertDialog.Builder(context)
            alertDialogBuilder.setMessage(message)
            alertDialogBuilder.setPositiveButton("Ok") { _dialog, _ ->
                run {
                    _dialog.dismiss()
                }
            }
            val simpleDialog = alertDialogBuilder.create()
            simpleDialog.setCanceledOnTouchOutside(false)
            simpleDialog.setCancelable(false)
            simpleDialog.setOnShowListener {
                simpleDialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(
                    ContextCompat.getColor(context,R.color.colorButtonNew))
            }
            simpleDialog.show()
        }

        /**
         * shows the simple dialog with ok button
         */
        fun showNoInternetDialog(context: Context){
            val alertDialogBuilder = AlertDialog.Builder(context)
            alertDialogBuilder.setMessage(context.resources.getString(R.string.error_not_connected_internet))
            alertDialogBuilder.setPositiveButton("Ok") { _dialog, _ ->
                run {
                    _dialog.dismiss()
                }
            }
            val simpleDialog = alertDialogBuilder.create()
            simpleDialog.setCanceledOnTouchOutside(false)
            simpleDialog.setCancelable(false)
            simpleDialog.setOnShowListener {
                simpleDialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(
                    ContextCompat.getColor(context,R.color.colorButtonNew))
            }
            simpleDialog.show()
        }

        /**
         * shows the simple dialog with ok button
         */
        fun showServerErrorDialog(context: Context){
            val alertDialogBuilder = AlertDialog.Builder(context)
            alertDialogBuilder.setMessage(context.resources.getString(R.string.error_server))
            alertDialogBuilder.setPositiveButton("Ok") { _dialog, _ ->
                run {
                    _dialog.dismiss()
                }
            }
            val simpleDialog = alertDialogBuilder.create()
            simpleDialog.setCanceledOnTouchOutside(false)
            simpleDialog.setCancelable(false)
            simpleDialog.setOnShowListener {
                simpleDialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(
                    ContextCompat.getColor(context,R.color.colorButtonNew))
            }
            simpleDialog.show()
        }
    }
}