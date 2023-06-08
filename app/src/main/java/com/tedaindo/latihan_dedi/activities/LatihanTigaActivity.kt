package com.tedaindo.latihan_dedi.activities

import android.Manifest
import android.app.Dialog
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.drawable.ColorDrawable
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.location.LocationManager
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.text.Html
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.Button
import android.widget.FrameLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.tasks.OnCompleteListener
import com.tedaindo.latihan_dedi.R
import java.io.IOException
import java.util.*
import kotlin.system.exitProcess


open class LatihanTigaActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var btLocation: Button
    private lateinit var btRefresh: Button
    private lateinit var tvLat: TextView
    private lateinit var tvLong: TextView
    private lateinit var tvCounty: TextView
    private lateinit var tvLocality: TextView
    private lateinit var tvAddress: TextView
    private lateinit var tvNoFakeGPS: TextView
    private lateinit var tv7: TextView

    var fusedLocationProviderClient: FusedLocationProviderClient? = null

    private lateinit var dialog: Dialog
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_latihan_tiga)

        initUI()
        setupAction()
    }

    private fun initUI() {
        btLocation = findViewById(R.id.bt_location)
        btRefresh = findViewById(R.id.bt_refresh)
        tvLat = findViewById(R.id.text_view1)
        tvLong = findViewById(R.id.text_view2)
        tvCounty = findViewById(R.id.text_view3)
        tvLocality= findViewById(R.id.text_view4)
        tvAddress = findViewById(R.id.text_view5)
        tvNoFakeGPS = findViewById(R.id.text_view6)
        tv7 = findViewById(R.id.text_view7)

        checkForAllowMockLocationsApps(this)
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
    }

    private fun setupAction() {
        btLocation.setOnClickListener(this)
    }

    override fun onClick(v: View) {
        when (v) {
            btLocation -> {
                if (ActivityCompat.checkSelfPermission(
                        this,
                        Manifest.permission.ACCESS_FINE_LOCATION
                    ) == PackageManager.PERMISSION_GRANTED
                ) {
                    getLocation()
                    checkForAllowMockLocationsApps(this)
                } else {
                    ActivityCompat.requestPermissions(
                        this,
                        arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                        44
                    )
                }
            }
            btRefresh -> {
                if (ContextCompat.checkSelfPermission(
                        this,
                        Manifest.permission.ACCESS_FINE_LOCATION
                    )
                    == PackageManager.PERMISSION_GRANTED
                ) {
                    getLocation()
                } else {
                    try {
                        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                        val uri = Uri.fromParts("package", packageName, null)
                        intent.data = uri
                        startActivity(intent)
                        exitProcess(0)
                    } catch (e: ActivityNotFoundException) {
                        e.printStackTrace()
                    }
                }
            }
        }
    }

    private fun getLocation() {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }
        fusedLocationProviderClient!!.lastLocation.addOnCompleteListener(OnCompleteListener<Location> { task ->
            val location: Location = task.result
            if (location != null) {
                try {
                    //using geocoder to get location
                    val geocoder = Geocoder(this, Locale.getDefault())
                    val addresses: List<Address> = geocoder.getFromLocation(
                        location.getLatitude(), location.getLongitude(), 1
                    ) as List<Address>
                    tvLat.setText(
                        Html.fromHtml(
                            "<font color='#6200EE'><b>Latitude :</br><br></font>"
                                    + addresses[0].getLatitude()
                        )
                    )
                    tvLong.setText(
                        Html.fromHtml(
                            (
                                    "<font color='#6200EE'><b>Longitude :</br><br></font>"
                                            + addresses[0].getLongitude()
                                    )
                        )
                    )
                    tvCounty.setText(
                        Html.fromHtml(
                            (
                                    "<font color='#6200EE'><b>Country :</br><br></font>"
                                            + addresses[0].getCountryName()
                                    )
                        )
                    )
                    tvLocality.setText(
                        Html.fromHtml(
                            (
                                    "<font color='#6200EE'><b>Locality :</br><br></font>"
                                            + addresses[0].getLocality()
                                    )
                        )
                    )
                    tvAddress.setText(
                        Html.fromHtml(
                            (
                                    "<font color='#6200EE'><b>Address :</br><br></font>"
                                            + addresses[0].getAddressLine(0)
                                    )
                        )
                    )
                    //CHECK IF USER IS USING FAKE GPS OR NOT
                    if (location.isFromMockProvider() == true) {
                        dgLocation()
                    } else {
                        tvNoFakeGPS.setText(R.string.lbl_no_fake_gps)
                    }
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
        })
    }

    private fun dgLocation() {
        dialog = Dialog(this)
//        do not close dialog
        dialog.setCancelable(false)

        dialog.apply {
            window?.setBackgroundDrawable(ColorDrawable(0))
            setContentView(R.layout.dialog_lokasi)
            window?.setLayout(
                FrameLayout.LayoutParams.MATCH_PARENT,
                FrameLayout.LayoutParams.MATCH_PARENT
            )
            window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN)

            val btOk: Button = findViewById(R.id.btn_ok)

            btOk.setOnClickListener {
                dialog.dismiss()
                finishAffinity()
                System.exit(0)
            }
        }
        dialog.show()
    }

    //CHECK ADA BERAPA APLIKASI FAKE GPS YANG TERINSTALL DI DALAM DEVICE USER
    private fun checkForAllowMockLocationsApps(context: Context) {
        val manager = getSystemService(Context.LOCATION_SERVICE) as? LocationManager
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }
        if (manager?.getLastKnownLocation(LocationManager.GPS_PROVIDER)?.isFromMockProvider == true) {
            // Mock locations are/were used
        }
    }
}