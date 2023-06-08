package com.tedaindo.latihan_dedi.activities


import android.Manifest
import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.*
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.afollestad.materialdialogs.MaterialDialog
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import com.tedaindo.latihan_dedi.R
import kotlin.system.exitProcess

class LatihanDuaActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var tvlat: TextView
    private lateinit var tvlong: TextView
    private lateinit var btnRefresh: Button
    private lateinit var btnCopy: Button

    private var lat: Double = 0.0
    private var lon: Double = 0.0

    private var locationListener: LocationListener? = null
    private lateinit var locationManager: LocationManager
    private var hasGps = false
    private var hasNetwork = false

    private var locationGPS: Location? = null
    private var locationNetwork: Location? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_latihan_dua)

        supportActionBar?.title = "Latihan 2"
        initUI()
        setupAction()
    }

    override fun onClick(v: View) {
        when (v) {
            btnRefresh -> {
                if (ContextCompat.checkSelfPermission(
                        this,
                        Manifest.permission.ACCESS_FINE_LOCATION
                    )
                    == PackageManager.PERMISSION_GRANTED
                ) {
                    getCurrentLoc()
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
            btnCopy -> {
                val clipboardManager =
                    getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                val clipData = ClipData.newPlainText(
                    "text",
                    tvlat.text.toString().plus(" ").plus(tvlong.text.toString())
                )
                clipboardManager.setPrimaryClip(clipData)
                Toast.makeText(this, "Text Telah di Copy", Toast.LENGTH_LONG).show()
            }

        }
    }

    private fun setupAction() {
        btnRefresh.setOnClickListener(this)
        btnCopy.setOnClickListener(this)
    }

    private fun initUI() {
        tvlat = findViewById(R.id.tv_lat)
        tvlong = findViewById(R.id.tv_long)
        btnRefresh = findViewById(R.id.btn_refresh)
        btnCopy = findViewById(R.id.btn_copy)
        checkPermission()

    }

    private fun getCurrentLoc() {
        if (lat == 0.0 || lon == 0.0) {
            getLocation()
        } else {
            tvlat.text = lat.toString()
            tvlong.text = lon.toString()
        }
    }

    @SuppressLint("MissingPermission")
    private fun getLocation() {
        locationManager =
            (getSystemService(Context.LOCATION_SERVICE) as LocationManager)

        hasGps = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
        hasNetwork = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)

        if (hasGps || hasNetwork) {
            if (hasGps) {
                Log.d("CodeAndroidLocation", "hasGPS")
                locationManager.requestLocationUpdates(
                    LocationManager.GPS_PROVIDER,
                    5000, 0F, object : LocationListener {
                        override fun onLocationChanged(p0: Location) {
                            locationNetwork = p0
                            lat = locationNetwork!!.latitude
                            lon = locationNetwork!!.longitude
                        }

                        override fun onStatusChanged(
                            provider: String?,
                            status: Int,
                            extras: Bundle?
                        ) {
                            locationListener = this
                        }
                    })

                val localGPSLocation =
                    locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
                if (localGPSLocation != null) {
                    locationGPS = localGPSLocation
                }
            }

            if (hasNetwork) {
                Log.d("CodeAndroidLocation", "hasGPS")
                locationManager.requestLocationUpdates(
                    LocationManager.NETWORK_PROVIDER,
                    5000, 0F, object : LocationListener {
                        override fun onLocationChanged(p0: Location) {
                            locationNetwork = p0
                            lat = locationNetwork!!.latitude
                            lon = locationNetwork!!.longitude
                        }

                        override fun onStatusChanged(
                            provider: String?,
                            status: Int,
                            extras: Bundle?
                        ) {
                            locationListener = this
                        }
                    })

                val localNetworkLocation =
                    locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)
                if (localNetworkLocation != null)
                    locationNetwork = localNetworkLocation
            }

            if (locationGPS != null && locationNetwork != null) {
                if (locationGPS!!.accuracy > locationNetwork!!.accuracy) {
                    lat = locationNetwork!!.latitude
                    lon = locationNetwork!!.longitude
                } else {
                    lat = locationGPS!!.latitude
                    lon = locationGPS!!.longitude
                }
            }
        } else {
            MaterialDialog(this).message(R.string.lbl_gps_mati)
                .show {
                    positiveButton(R.string.lbl_ya) {
                        startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS))
                    }
                }
        }
    }

    private fun getLocation2() {

        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            )
            != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                PERMISSION_REQUEST_ACCESS_FINE_LOCATION
            )
            return
        }
        locationManager.requestLocationUpdates(
            LocationManager.NETWORK_PROVIDER,
            0L,
            0f,
            locationListener!!
        )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PERMISSION_REQUEST_ACCESS_FINE_LOCATION) {
            when (grantResults[0]) {
                PackageManager.PERMISSION_GRANTED -> getLocation2()
                PackageManager.PERMISSION_DENIED -> {
                    Toast.makeText(this, "Failed", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    companion object {
        private const val PERMISSION_REQUEST_ACCESS_FINE_LOCATION = 100
    }

    private fun checkPermission(
    ) {
        Dexter.withActivity(this).withPermissions(
            Manifest.permission.INTERNET,
            Manifest.permission.ACCESS_WIFI_STATE,
            Manifest.permission.ACCESS_NETWORK_STATE,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION,
        ).withListener(object : MultiplePermissionsListener {
            override fun onPermissionsChecked(report: MultiplePermissionsReport) {
                if (report.areAllPermissionsGranted()) {
                    getLocation()
                    if (lat == 0.0 || lon == 0.0) {
                        getCurrentLoc()
                    } else {
                        tvlat.text = lat.toString()
                        tvlong.text = lon.toString()
                    }

                }
            }

            override fun onPermissionRationaleShouldBeShown(
                p0: MutableList<com.karumi.dexter.listener.PermissionRequest>?,
                token: PermissionToken?
            ) {
                showRationalDialogForPermissions()
            }

        }).onSameThread().check()
    }

    private fun showRationalDialogForPermissions() {
        AlertDialog.Builder(this)
            .setMessage(resources.getString(R.string.pesan_alasan))
            .setPositiveButton(resources.getString(R.string.lbl_pengaturan)) { _, _ ->
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
            .setNegativeButton(
                resources.getString(R.string.lbl_batal),
                DialogInterface.OnClickListener { dialog, which -> dialog.cancel() })
            .show()
    }

}