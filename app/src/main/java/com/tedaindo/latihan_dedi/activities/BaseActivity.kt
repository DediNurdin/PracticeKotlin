package com.tedaindo.latihan_dedi.activities

import android.app.Dialog
import android.content.SharedPreferences
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.StrictMode
import android.os.StrictMode.ThreadPolicy
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.tedaindo.latihan_dedi.R
import com.tedaindo.latihan_dedi.helpers.Preferences.PREFS_NAME
import com.tedaindo.latihan_dedi.helpers.Preferences.customPreferences


open class BaseActivity : AppCompatActivity() {

    private var doubleBackToExitState = false
    lateinit var prefs: SharedPreferences
    private var progressDialog: Dialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        prefs = customPreferences(this, PREFS_NAME)
        val policy = ThreadPolicy.Builder().permitAll().build()

        StrictMode.setThreadPolicy(policy)

        if (progressDialog == null) {
            progressDialog = Dialog(this)
            progressDialog!!.window!!.setBackgroundDrawable(ColorDrawable(0))
        }
    }

    fun doubleBackToExit() {
        if (doubleBackToExitState) {
            super.onBackPressed()
            return
        }

        this.doubleBackToExitState = true
        Toast.makeText(this, R.string.click_again_to_exit, Toast.LENGTH_SHORT).show()

        Handler(Looper.getMainLooper()).postDelayed({
            doubleBackToExitState = false
        }, 2000)
    }

    fun loadFragment(fragment: Fragment) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.fr_container, fragment)
        transaction.commit()
    }

    fun showProgressDialog(show: Boolean) {
        when {
            show -> {
                if (!isFinishing) {
                    progressDialog!!.setCanceledOnTouchOutside(false)
                    progressDialog!!.show()
                }
            }
            else -> try {
                if (progressDialog!!.isShowing && !isFinishing) {
                    progressDialog!!.dismiss()
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

}