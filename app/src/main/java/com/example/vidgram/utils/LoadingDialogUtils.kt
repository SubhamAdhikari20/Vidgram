package com.example.vidgram.utils

import android.app.Activity
import android.app.AlertDialog
import com.example.vidgram.R

class LoadingDialogUtils(val activity: Activity) {
    private var alertDialog: AlertDialog? = null  // Changed to nullable var

    fun show() {
        activity.runOnUiThread {
            // Dismiss existing dialog if showing
            alertDialog?.dismiss()

            val builder = AlertDialog.Builder(activity)
                .setView(R.layout.loading_dialog)
                .setCancelable(false)

            alertDialog = builder.create()
            alertDialog?.setCanceledOnTouchOutside(false)
            alertDialog?.show()
        }
    }

    fun dismiss() {
        activity.runOnUiThread {
            alertDialog?.dismiss()  // Safe call with null check
            alertDialog = null
        }
    }

}