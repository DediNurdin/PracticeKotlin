package com.tedaindo.latihan_dedi.activities

import android.graphics.PorterDuff
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.tedaindo.latihan_dedi.R
import com.tedaindo.latihan_dedi.fragments.HomeFragment
import com.tedaindo.latihan_dedi.fragments.LainFragment
import com.tedaindo.latihan_dedi.fragments.akunFragment

class UtamaActivity : BaseActivity(), View.OnClickListener {

    private var layouts: Array<LinearLayout>? = null
    private var imageViews: Array<ImageView>? = null
    private var textViews: Array<TextView>? = null

    lateinit var llHome: LinearLayout
    private lateinit var ivHome: ImageView
    private lateinit var tvHome: TextView
    lateinit var llMessage: LinearLayout
    private lateinit var ivMessage: ImageView
    private lateinit var tvMessage: TextView
    private lateinit var llMore: LinearLayout
    private lateinit var ivMore: ImageView
    private lateinit var tvMore: TextView
    private lateinit var tvCount: TextView

    private val homeFragment = HomeFragment()
    private val lainFragment = LainFragment()
    private val akunFragment = akunFragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_utama)

//        if (FBMessagingServices.FirebaseAddOnSound.r != null) {
//            FBMessagingServices.FirebaseAddOnSound.r!!.stop()
//        }

        initUI()
        setupActions()

//        if (prefs.account != "") {
//            loadCountInbox()
//        }

    }

//    private fun loadCountInbox() {
//        if (prefs.account != "") {
//            if (isNetworkAvailable(this)) {
//                InboxRepository().count(this, prefs.refreshToken)
//
//            } else {
//                Toast.makeText(
//                    this, resources.getString(R.string.no_internet_message),
//                    Toast.LENGTH_SHORT
//                ).show()
//            }
//        }
//    }

    private fun initUI() {
        llHome = findViewById(R.id.ll_home_main)
        ivHome = findViewById(R.id.iv_home_main)
        tvHome = findViewById(R.id.tv_home_main)
        llMessage = findViewById(R.id.ll_message_main)
        ivMessage = findViewById(R.id.iv_message_main)
        tvMessage = findViewById(R.id.tv_message_main)
        llMore = findViewById(R.id.ll_more_main)
        ivMore = findViewById(R.id.iv_more_main)
        tvMore = findViewById(R.id.tv_more_main)

        layouts = arrayOf(llHome, llMessage, llMore)
        imageViews = arrayOf(ivHome, ivMessage, ivMore)
        textViews = arrayOf(tvHome, tvMessage, tvMore)

        onClick(llHome)
    }

    private fun setupActions() {
        llHome.setOnClickListener(this)
        llMessage.setOnClickListener(this)
        llMore.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v) {
            llHome -> {
                supportActionBar?.title = "Home"
                setSelected(llHome, homeFragment)
            }

            llMessage -> {

                supportActionBar?.title = "Akun"
                setSelected(llMessage, akunFragment)

            }

            llMore -> {
                supportActionBar?.title = "More"
                setSelected(llMore, lainFragment)

            }
        }
    }

    private var resultInbox = registerForActivityResult(
        ActivityResultContracts
            .StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            llMessage.callOnClick()
        }
    }

    private var resultMore = registerForActivityResult(
        ActivityResultContracts
            .StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            llMore.callOnClick()
        }
    }

    fun setSelected(mBarImg: LinearLayout, fragment: Fragment) {
        for ((index, linearLayout) in layouts!!.withIndex()) {
            if (linearLayout === mBarImg) {
                enable(linearLayout, imageViews?.get(index)!!, textViews?.get(index)!!)
                loadFragment(fragment)
            } else {
                disableTab(linearLayout, imageViews?.get(index)!!, textViews?.get(index)!!)
            }
        }
    }

    private fun disableTab(linearLayout: LinearLayout, imageView: ImageView, textView: TextView) {
        val params = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.WRAP_CONTENT,
            LinearLayout.LayoutParams.MATCH_PARENT
        )
        params.weight = 0.5f
        params.gravity = Gravity.CENTER
        linearLayout.layoutParams = params
        linearLayout.background = null
        textView.visibility = View.GONE
        imageView.background = null

        imageView.setColorFilter(
            ContextCompat.getColor(this, R.color.textColorSecondary),
            PorterDuff.Mode.MULTIPLY
        )
    }

    private fun enable(linearLayout: LinearLayout, imageView: ImageView, textView: TextView) {
        linearLayout.background = resources.getDrawable(R.drawable.bg_bottom)

        val params = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.WRAP_CONTENT,
            LinearLayout.LayoutParams.MATCH_PARENT
        )
        params.weight = 1.2f

        linearLayout.layoutParams = params
        imageView.setColorFilter(
            ContextCompat.getColor(
                this,
                R.color.white
            )
        )
        imageView.background = resources.getDrawable(R.drawable.bg_button)
        textView.visibility = View.VISIBLE
    }

    fun onCountSuccess(result: String?) {
        tvCount.text = result
    }

    fun onCountFailure(message: String) {
        if (message != "") {
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
        }

    }

    override fun onBackPressed() {
        doubleBackToExit()
    }


}