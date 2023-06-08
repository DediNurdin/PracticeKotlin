package com.tedaindo.latihan_dedi.helpers

import android.content.Context
import android.content.SharedPreferences

object Preferences {
    const val PREFS_NAME = "dataYanmaPresisi"
    const val APP_BASE_URL = "https://api.yanmapolri.com/api/"
    const val APP_BASE_WEB_URL = "https://yanmapolri.com/"
    const val APP_BASE_NEWS_IMAGE_URL = "https://api.yanmapolri.com/static/news_image/"
    const val APP_BASE_USER_IMAGE_URL = "https://api.yanmapolri.com/static/user_image/"
    const val APP_BASE_USER_DOC_URL = "https://api.yanmapolri.com/static/user_doc/"
    const val APP_BASE_TRMAKAM_IMAGE_URL = "https://api.yanmapolri.com/static/trmakam_image/"
    const val APP_BASE_TRKENDARAAN_IMAGE_URL = "https://api.yanmapolri.com/static/trkendaraan_image/"
    const val APP_BASE_DENAH_IMAGE_URL = "https://api.yanmapolri.com/static/denah_image/"

    const val RC_GET_PICTURE_STORAGE = 1001
    const val RC_DETAIL_notification = 301
    const val RC_ADD_SIP = 101
    const val PICK_FILE = 3
    const val RC_APPROVAL_DOCUMENT = 201
    const val RESULT_REJECT_SIP_OK = 402
    const val RESULT_APPROVAL_DOCUMENT_OK = 401
    const val RESULT_APPROVAL_SIP = 403
    const val RESULT_NUMBERING_OK = 404
    const val FILE_NAME = "photo.jpg"
    const val REQUEST_CODE = 13

    private const val REFRESH_TOKEN = "refreshToken"
    private const val ACCESS_TOKEN = "accessToken"
    private const val ACCOUNT_ID = "accountId"
    private const val AUTH_ID = "authId"
    private const val AUTH_NAME = "authName"
    private const val FULL_NAME = "fullName"
    private const val EMAIL = "email"
    private const val PHONE = "phone"
    private const val BIRTH_PLACE = "birthPlace"
    private const val BIRTH_DATE = "birthDate"
    private const val GENDER = "gender"
    private const val ACTIVE = "active"
    private const val ADD_BY = "addBy"
    private const val ADD_TIME = "addTime"
    private const val NOTIFICATION = "notification"
    private const val PICTURE = "picture"
    private const val ADDRESS = "address"
    private const val NIP_NRP = "nikNrp"
    private const val JABATAN = "jabatan"
    private const val PANGKAT = "pangkat"
    private const val DIVISION = "division"
    private const val KTP = "ktp"

    fun customPreferences(context: Context, name: String): SharedPreferences = context.getSharedPreferences(name,
        Context.MODE_PRIVATE)

    private inline fun SharedPreferences.editMe(operation: (SharedPreferences.Editor) -> Unit) {
        val editMe = edit()
        operation(editMe)
        editMe.apply()
    }

    var SharedPreferences.refreshToken get() = getString(REFRESH_TOKEN, "")
        set(value) {
            editMe { it.putString(REFRESH_TOKEN, value) }
        }

    var SharedPreferences.accessToken get() = getString(ACCESS_TOKEN, "")
        set(value) {
            editMe { it.putString(ACCESS_TOKEN, value) }
        }

    var SharedPreferences.accountId get() = getString(ACCOUNT_ID, "")
        set(value) {
            editMe { it.putString(ACCOUNT_ID, value) }
        }

    var SharedPreferences.authId get() = getInt(AUTH_ID, 0)
        set(value) {
            editMe { it.putInt(AUTH_ID, value) }
        }

    var SharedPreferences.authName get() = getString(AUTH_NAME, "")
        set(value) {
            editMe { it.putString(AUTH_NAME, value) }
        }

    var SharedPreferences.fullName get() = getString(FULL_NAME, "")
        set(value) {
            editMe { it.putString(FULL_NAME, value) }
        }

    var SharedPreferences.email get() = getString(EMAIL, "")
        set(value) {
            editMe { it.putString(EMAIL, value) }
        }

    var SharedPreferences.phone get() = getString(PHONE, "")
        set(value) {
            editMe { it.putString(PHONE, value) }
        }

    var SharedPreferences.birthPlace get() = getString(BIRTH_PLACE, "")
        set(value) {
            editMe { it.putString(BIRTH_PLACE, value) }
        }

    var SharedPreferences.birthDate get() = getString(BIRTH_DATE, "")
        set(value) {
            editMe { it.putString(BIRTH_DATE, value) }
        }

    var SharedPreferences.gender get() = getString(GENDER, "")
        set(value) {
            editMe { it.putString(GENDER, value) }
        }

    var SharedPreferences.active get() = getInt(ACTIVE, 0)
        set(value) {
            editMe { it.putInt(ACTIVE, value) }
        }

    var SharedPreferences.addBy get() = getString(ADD_BY, "")
        set(value) {
            editMe { it.putString(ADD_BY, value) }
        }

    var SharedPreferences.addTime get() = getString(ADD_TIME, "")
        set(value) {
            editMe { it.putString(ADD_TIME, value) }
        }

    var SharedPreferences.notification get() = getInt(NOTIFICATION, 0)
        set(value) {
            editMe { it.putInt(NOTIFICATION, value) }
        }

    var SharedPreferences.picture get() = getString(PICTURE, "")
        set(value) {
            editMe { it.putString(PICTURE, value) }
        }

    var SharedPreferences.address get() = getString(ADDRESS, "")
        set(value) {
            editMe { it.putString(ADDRESS, value) }
        }

    var SharedPreferences.nipNrp get() = getString(NIP_NRP, "")
        set(value) {
            editMe { it.putString(NIP_NRP, value) }
        }

    var SharedPreferences.jabatan get() = getString(JABATAN, "")
        set(value) {
            editMe { it.putString(JABATAN, value) }
        }

    var SharedPreferences.pangkat get() = getString(PANGKAT, "")
        set(value) {
            editMe { it.putString(PANGKAT, value) }
        }

    var SharedPreferences.division get() = getString(DIVISION, "")
        set(value) {
            editMe { it.putString(DIVISION, value) }
        }

    var SharedPreferences.ktp get() = getString(KTP, "")
        set(value) {
            editMe { it.putString(KTP, value) }
        }
}