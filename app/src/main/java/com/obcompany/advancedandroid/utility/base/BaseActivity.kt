package com.obcompany.advancedandroid.utility.base

import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.obcompany.advancedandroid.utility.DialogUtil

open class BaseActivity : AppCompatActivity(){
    lateinit var loadingDialog: AlertDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        loadingDialog = DialogUtil.progress(this)
    }

    override fun onDestroy() {
        loadingDialog.dismiss()
        super.onDestroy()
    }
}