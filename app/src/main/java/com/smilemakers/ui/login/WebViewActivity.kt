package com.smilemakers.ui.login

import android.annotation.SuppressLint
import android.app.ProgressDialog
import android.os.Bundle
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import com.smilemakers.R
import com.smilemakers.utils.hide
import com.smilemakers.utils.show
import kotlinx.android.synthetic.main.activity_webview.*

class WebViewActivity :AppCompatActivity(){

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_webview)


        progressBar.show()

        webView.webViewClient = WebViewClient()
        webView.loadUrl("http://thesmilemakers.in/thesmilemakers/")
        webView.settings.javaScriptEnabled = true
        webView.settings.setSupportZoom(true)

        webView.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
                view.loadUrl(url)
                progressBar.show()
                return true
            }

            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
            }
            @SuppressLint("SetJavaScriptEnabled")
            override fun onPageCommitVisible(view: WebView?, url: String?) {
                super.onPageCommitVisible(view, url)
                progressBar.hide()
            }
        }


    }

    override fun onBackPressed() {
        super.onBackPressed()
        if (webView.canGoBack())
            webView.goBack()
        else
            super.onBackPressed()
    }

}
