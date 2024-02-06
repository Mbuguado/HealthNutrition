package com.example.healthnutrition.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient
import com.example.healthnutrition.R


class NewsBlogFragment : Fragment() {

    private lateinit var webView: WebView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_news_blog, container, false)

        webView = view.findViewById(R.id.webview)
        webView.webViewClient = WebViewClient()

        webView.settings.javaScriptEnabled = true
        webView.loadUrl("https://www.medicalnewstoday.com/articles/317051#gymnema")
        return view
    }

}