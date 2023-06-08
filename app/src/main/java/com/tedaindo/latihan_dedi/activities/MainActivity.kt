package com.tedaindo.latihan_dedi.activities

import android.Manifest
import android.app.AlertDialog
import android.app.Dialog
import android.content.ActivityNotFoundException
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color.RED
import android.graphics.drawable.ColorDrawable
import android.graphics.pdf.PdfRenderer
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.provider.Settings
import android.util.Base64
import android.view.View
import android.view.WindowManager
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.FileProvider
import com.jsibbold.zoomage.ZoomageView
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import com.shockwave.pdfium.PdfDocument
import com.shockwave.pdfium.PdfiumCore
import com.tedaindo.latihan_dedi.R
import com.tedaindo.latihan_dedi.helpers.Preferences.FILE_NAME
import com.tedaindo.latihan_dedi.helpers.Preferences.PICK_FILE
import com.tedaindo.latihan_dedi.helpers.Preferences.REQUEST_CODE
import com.tedaindo.latihan_dedi.helpers.Utils
import java.io.File
import java.io.FileNotFoundException
import java.io.IOException
import kotlin.system.exitProcess

class MainActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var btPilihFile: Button
    private lateinit var btSimpan: Button
    private lateinit var tvTheme: TextView
    private lateinit var swTheme: Switch
    private lateinit var dialog: Dialog
    private lateinit var filePhoto: File

    private lateinit var btnKembali: Button
    private lateinit var btnSelanjutnya: Button

    private var lampiran = ""
    private var pushStats = false

    private lateinit var ivLampiran: ZoomageView
    private var ll = ""
    private var llThumb = ""
    private var llType = 0

    private var total_pages = 0
    private var display_page = 0

    var renderer: PdfRenderer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        supportActionBar?.title = "Latihan 1"

        initUI()
        setupAction()
    }

    private fun initUI() {
        btPilihFile = findViewById(R.id.bt_pilih_file)
        btSimpan = findViewById(R.id.bt_simpan)
        tvTheme = findViewById(R.id.tv_theme)
        ivLampiran = findViewById(R.id.iv_lampiran)
        swTheme = findViewById(R.id.sw_theme)
        btnKembali = findViewById(R.id.btn_kembali)
        btnSelanjutnya = findViewById(R.id.btn_selanjutnya)

        if (swTheme.isChecked) {
            tvTheme.text = "Tema Gelap"
            tvTheme.setTextColor(RED)
            pushStats = true
        } else {
            swTheme.isChecked = false
            tvTheme.text = "Tema Terang"
            tvTheme.setTextColor(RED)
            pushStats = false
        }

    }

    private fun setupAction() {
        btPilihFile.setOnClickListener(this)
        btSimpan.setOnClickListener(this)
        swTheme.setOnCheckedChangeListener { _, _ ->
            if (swTheme.isChecked) {
                tvTheme.text = "Tema Gelap"
                tvTheme.setTextColor(RED)
                pushStats = true
            } else {
                tvTheme.text = "Tema Terang"
                tvTheme.setTextColor(RED)
                pushStats = true
                pushStats = false
            }
        }

        btnKembali.setOnClickListener {
            if (display_page > 0) {
                display_page--
                _display(display_page)
            }
        }

        btnSelanjutnya.setOnClickListener {
            if (display_page < total_pages - 1) {
                display_page++
                _display(display_page)
            }
        }
    }

    override fun onClick(view: View?) {
        when (view) {
            btPilihFile -> {
                checkPermission()
                showDialogPilihFile()
                lampiran = "ll"
            }
            btSimpan -> {
                changeTheme()
            }
        }
    }

    private fun changeTheme() {
        if (swTheme.isChecked) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            tvTheme.text = "Tema Gelap"
            tvTheme.setTextColor(RED)
            pushStats = true
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            pushStats = false
        }
    }

    private fun showDialogPilihFile() {
        dialog = Dialog(this)
        //do not close dialog
//        dialog.setCancelable(false)

        dialog.apply {
            window?.setBackgroundDrawable(ColorDrawable(0))
            setContentView(R.layout.dialog_pilih_file)
            window?.setLayout(
                FrameLayout.LayoutParams.MATCH_PARENT,
                FrameLayout.LayoutParams.MATCH_PARENT
            )
            window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN)

            val tvStorage: TextView = findViewById(R.id.tv_penyimpanan_dg_pilih_file)
            val tvKamera: TextView = findViewById(R.id.tv_kamera_dg_pilih_file)

            tvStorage.setOnClickListener {
                chooseFile()
                dialog.dismiss()
            }

            tvKamera.setOnClickListener {
                showCamera()
                dialog.dismiss()
            }
        }
        dialog.show()
    }

    private fun getPhotoFile(fileName: String): File {
        val directoryStorage = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(fileName, ".jpg", directoryStorage)
    }

    private fun showCamera() {
        val takePhotoIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        filePhoto = getPhotoFile(FILE_NAME)

        val providerFile = FileProvider.getUriForFile(
            this,
            "com.tedaindo.latihan_dedi.fileprovider",
            filePhoto
        )
        takePhotoIntent.putExtra(MediaStore.EXTRA_OUTPUT, providerFile)
        if (takePhotoIntent.resolveActivity(this.packageManager) != null) {
            startActivityForResult(takePhotoIntent, REQUEST_CODE)
        } else {
            Toast.makeText(this, "Camera could not open", Toast.LENGTH_SHORT).show()
        }
    }

    private fun chooseFile() {

        val mimeTypes = arrayOf(
            "application/pdf",
            "image/png",
            "image/bmp",
            "image/jpg",
            "image/jpeg"
        )

        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.addCategory(Intent.CATEGORY_OPENABLE)

        intent.type = if (mimeTypes.size == 1) mimeTypes[0] else "*/*"
        if (mimeTypes.isNotEmpty()) {
            intent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes)
        }
        startActivityForResult(intent, PICK_FILE)
    }

    private fun selectedPicture(data: Uri) {
        val bmp = Utils.getBitmapFromStorage(data, this)
        val bmpResized = Utils.getResizedBitmap(bmp!!, 480)
        val base64Str = Utils.convertBitmapToBase64String(bmp)
        val base64StrResized = Utils.convertBitmapToBase64String(bmpResized)

        when (lampiran) {
            "ll" -> {
                ivLampiran.setImageBitmap(bmp)
                ll = base64Str
                llThumb = base64StrResized
                llType = 2
            }
        }
    }

    private fun selectedPdf(pdfUri: Uri) {
        val pageNumber = 0
        val pdfiumCore = PdfiumCore(this)
        try {
            val fd = contentResolver.openFileDescriptor(pdfUri, "r")
            val pdfDocument: PdfDocument = pdfiumCore.newDocument(fd)
            pdfiumCore.openPage(pdfDocument, pageNumber)
            val width = pdfiumCore.getPageWidthPoint(pdfDocument, pageNumber)
            val height = pdfiumCore.getPageHeightPoint(pdfDocument, pageNumber)
            val bmp = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
            pdfiumCore.renderPageBitmap(pdfDocument, bmp, pageNumber, 0, 0, width, height)

            val inputStream = contentResolver.openInputStream(pdfUri)
            val bytes = Utils.getBytes(inputStream!!)
            val base64Str = Base64.encodeToString(bytes, Base64.DEFAULT)
            val base64StrThumb = Utils.convertBitmapToBase64String(bmp)

            val bmpResized = Utils.getResizedBitmap(bmp!!, 480)
            val base64StrResized = Utils.convertBitmapToBase64String(bmpResized)

            when (lampiran) {
                "ll" -> {
                    ivLampiran.setImageBitmap(bmp)
                    ll = base64Str
                    llThumb = base64StrResized
                    llType = 1
                }
            }

        } catch (e: Exception) {
            //todo with exception
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == RESULT_OK) {
            when (requestCode) {
                PICK_FILE -> {

//                    val uri: Uri? = data!!.data
//                    try {
//                        val parcelFileDescriptor = uri?.let {
//                            contentResolver
//                                .openFileDescriptor(it, "r")
//                        }
//                        renderer = PdfRenderer(parcelFileDescriptor!!)
//                        total_pages = renderer!!.getPageCount()
//                        display_page = 0
//                        _display(display_page)
//                    } catch (fnfe: FileNotFoundException) {
//                    } catch (e: IOException) {
//                    }

                    if (data != null) {
                        val type = contentResolver.getType(data.data!!)

                        if (type != "application/pdf") {
                            btnKembali.visibility = View.GONE
                            btnSelanjutnya.visibility = View.GONE
                        } else {
                            btnKembali.visibility = View.VISIBLE
                            btnSelanjutnya.visibility = View.VISIBLE
                        }

                        if (type == "application/pdf") {
                            val uri: Uri? = data!!.data
                            try {
                                val parcelFileDescriptor = uri?.let {
                                    contentResolver
                                        .openFileDescriptor(it, "r")
                                }
                                renderer = PdfRenderer(parcelFileDescriptor!!)
                                total_pages = renderer!!.pageCount
                                display_page = 0
                                _display(display_page)
                            } catch (fnfe: FileNotFoundException) {
                            } catch (e: IOException) {
                            }
                        } else {
                            selectedPicture(data.data!!)
                        }
                    }

                }
                REQUEST_CODE -> {
//                    val compressedImageFile = Compressor.compress(this, File(filePhoto.absolutePath))
                    selectedPicture(Uri.fromFile(File(filePhoto.absolutePath)))

//                    if (data != null) {
//                    }
//                    val takenPhoto = BitmapFactory.decodeFile(filePhoto.absolutePath)
//                    ivSpk.setImageBitmap(takenPhoto)
//                    spkBase64 = ImageUtil.convertBitmapToBase64String(takenPhoto)

//                    ivSpk.setImageURI(data?.data)
//                    val bmp = ImageUtil.getBitmapFromStorage(data?.data!!, this)
//                    spkBase64 = ImageUtil.convertBitmapToBase64String(bmp)
                }
            }
        }
    }

    private fun checkPermission(
    ) {
        Dexter.withActivity(this).withPermissions(
            Manifest.permission.ACCESS_NETWORK_STATE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.ACCESS_WIFI_STATE,
            Manifest.permission.CAMERA,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.CALL_PHONE
        ).withListener(object : MultiplePermissionsListener {
            override fun onPermissionsChecked(report: MultiplePermissionsReport) {
                if (report.areAllPermissionsGranted()) {
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

    private fun _display(_n: Int) {
        if (renderer != null) {
            val page: PdfRenderer.Page = renderer!!.openPage(_n)
            val mBitmap = Bitmap.createBitmap(page.width, page.height, Bitmap.Config.ARGB_8888)
            page.render(mBitmap, null, null, PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY)

            val bmpResized = Utils.getResizedBitmap(mBitmap!!, 480)
            val base64StrResized = Utils.convertBitmapToBase64String(bmpResized)

            when (lampiran) {
                "ll" -> {
                    ivLampiran.setImageBitmap(mBitmap)
                    llThumb = base64StrResized
                    llType = 1
                }
            }
            page.close()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        renderer?.close()
    }
}
