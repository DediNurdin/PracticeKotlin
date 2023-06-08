package com.tedaindo.latihan_dedi.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient
import com.tedaindo.latihan_dedi.R
import android.content.Intent as Intent1

class WebFragment : Fragment() {

    private lateinit var wvWeb: WebView
    private var url = "https://dedinurdin.netlify.app/"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_web, container, false)
        initUI(view)
        setupActions()

        return view
    }

    private fun setupActions() {
    }

    private fun initUI(view: View) {

        view.let {
            wvWeb = it.findViewById(R.id.wv_web)
        }

        wvWeb.settings.javaScriptEnabled = true
        wvWeb.webViewClient = object : WebViewClient() {
            @Deprecated("Deprecated in Java")
            override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
                view?.loadUrl(url!!)
                return true
            }
        }

        wvWeb.loadUrl(url)
    }

}