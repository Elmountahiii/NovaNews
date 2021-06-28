package com.redgunner.novanews.view.fragment

import android.annotation.SuppressLint
import android.content.ActivityNotFoundException
import android.content.Intent
import android.content.res.Configuration
import android.text.Html
import android.view.View
import android.webkit.WebChromeClient
import android.widget.FrameLayout
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.webkit.WebSettingsCompat
import androidx.webkit.WebViewFeature
import com.bumptech.glide.Glide
import com.redgunner.novanews.R
import com.redgunner.novanews.models.post.Post
import com.redgunner.novanews.state.PostState
import com.redgunner.novanews.viewmodel.SharedViewModel
import kotlinx.android.synthetic.main.fragment_detail.*
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat


class DetailFragment : Fragment(R.layout.fragment_detail) {


    private val viewModel: SharedViewModel by activityViewModels()
    private val navArgs: DetailFragmentArgs by navArgs()
    private var articleURL = ""
    private var articleTitle = ""


    override fun onStart() {
        super.onStart()
        setUpWebView()
        val postId = arguments?.getString("post_id")
        if (postId != null) {

            viewModel.getPostById(postId.toInt())


        } else {

            viewModel.getPostById(navArgs.articleId)

        }




        lifecycleScope.launch {
            viewModel.postState.collect { postState ->
                when (postState) {

                    is PostState.Empty -> {

                        if (postId != null) {
                            viewModel.getPostById(postId.toInt())
                        } else {
                            viewModel.getPostById(navArgs.articleId)

                        }


                    }

                    is PostState.Loading -> {
                        shimmerState(isShimmer = true)

                    }

                    is PostState.Success -> {
                        shimmerState(isShimmer = false)

                        showPost(post = postState.post)
                        articleURL = postState.post.guid.rendered
                        articleTitle = postState.post.title.rendered

                    }

                    is PostState.Error -> {




                    }
                }


            }
        }

    }

    override fun onResume() {
        super.onResume()


        commentschip.setOnClickListener {

            findNavController().navigate(
                DetailFragmentDirections.actionDetailFragmentToCommentsFragment(
                    navArgs.articleId
                )
            )
        }

        backArrow.setOnClickListener {

            findNavController().popBackStack()
        }

        facebookchip.setOnClickListener {

            shareOnFacebook()


        }

        twitterchip.setOnClickListener {

            shareOnTwitter()


        }

        emailchip.setOnClickListener {
            shareOnEmail()

        }
        sharechip.setOnClickListener {

            normalShare()

        }
    }

    private fun normalShare() {
        val shareIntent: Intent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, articleURL)
            type = "text/plain"
        }

        requireContext().startActivity(Intent.createChooser(shareIntent, "send to"))
    }

    private fun shareOnEmail() {
        val emailIntent = Intent(Intent.ACTION_SEND)

        emailIntent.type = "text/plain"
        emailIntent.`package` = "com.google.android.gm"
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, articleTitle)
        emailIntent.putExtra(Intent.EXTRA_TEXT, articleURL)
        try {
            requireContext().startActivity(emailIntent)
        } catch (e: ActivityNotFoundException) {
            Toast.makeText(context, "Email not installed", Toast.LENGTH_LONG).show()

        }
    }

    private fun shareOnTwitter() {

        val tweet = Intent(Intent.ACTION_SEND)
        tweet.type = "text/plain"
        tweet.`package` = "com.twitter.android"
        tweet.putExtra(Intent.EXTRA_TEXT, articleURL)

        try {
            requireContext().startActivity(tweet)

        } catch (e: ActivityNotFoundException) {
            Toast.makeText(context, "Twitter not installed", Toast.LENGTH_LONG).show()

        }

    }

    private fun shareOnFacebook() {
        val facebookIntent = Intent(Intent.ACTION_SEND)
        facebookIntent.type = "text/plain"
        facebookIntent.`package` = "com.facebook.katana"
        facebookIntent.putExtra(Intent.EXTRA_TEXT, articleURL)
        try {
            requireContext().startActivity(facebookIntent)
        } catch (e: ActivityNotFoundException) {
            Toast.makeText(context, "Facebook not installed", Toast.LENGTH_LONG).show()


        }
    }


    @SuppressLint("SimpleDateFormat")
    private fun showPost(post: Post) {


        val htmlContent =
            "<!DOCTYPE html> <html> <head> </head><meta name= viewport content= width=device-width  initial-scale=1.0 > <style>img{display: inline;height: auto;max-width: 100%;}  iframe{} video{display: inline;width: 100%;poster=}  div{display:flex;top: 100%;}  </style> <body>   ${
                post.content.rendered.replace(
                    "\"",
                    ""
                )
            } </body></html>"


        postWebView.loadDataWithBaseURL(
            null,
            htmlContent,
            "text/html; charset=utf-8",
            "UTF-8",
            null
        )



        if (!post._embedded.wp_FeaturedMedia.isNullOrEmpty()) {
            Glide.with(this@DetailFragment)
                .load(post._embedded.wp_FeaturedMedia[0].source_url)
                .into(postImage)
        }




        postTitle.text = Html.fromHtml(Html.fromHtml(post.title.rendered).toString())
        val parser = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss")
        val formatter = SimpleDateFormat("dd.MM.yyyy")
        val time = formatter.format(parser.parse(post.date))


        timeWithCategory.text = "$time . ${post._embedded.wp_Term[0][0].name}"
    }


    private fun shimmerState(isShimmer: Boolean) {

        if (isShimmer) {
            detailShimmerLayout.isVisible = isShimmer

        } else {
            detailShimmerLayout.isVisible = isShimmer
            backArrow.isVisible = true
            timeWithCategory.isVisible = true
            postTitle.isVisible = true
            cardView2.isVisible = true
            postWebView.isVisible = true
            AppName.isVisible = true
            commentschip.isVisible = true
            facebookchip.isVisible = true
            twitterchip.isVisible = true
            emailchip.isVisible = true
            sharechip.isVisible = true


        }
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun setUpWebView() {
        when (resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK) {
            Configuration.UI_MODE_NIGHT_YES -> {
                if (WebViewFeature.isFeatureSupported(WebViewFeature.FORCE_DARK)) {


                    WebSettingsCompat.setForceDark(
                        postWebView.settings,
                        WebSettingsCompat.FORCE_DARK_ON
                    )

                }
            }
            Configuration.UI_MODE_NIGHT_NO, Configuration.UI_MODE_NIGHT_UNDEFINED -> {
                if (WebViewFeature.isFeatureSupported(WebViewFeature.FORCE_DARK)) {


                    WebSettingsCompat.setForceDark(
                        postWebView.settings,
                        WebSettingsCompat.FORCE_DARK_OFF
                    )

                }
            }
            else -> {
                //
            }
        }

        postWebView.apply {

            this.fitsSystemWindows = true
            this.settings.apply {
                javaScriptEnabled = true
                loadWithOverviewMode = true
                useWideViewPort = true

            }
            this.setInitialScale(1)


            this.webChromeClient = object : WebChromeClient() {

                private var mCustomView: View? = null
                private var mCustomViewCallback: CustomViewCallback? = null
                private var mOriginalOrientation = 0
                private var mOriginalSystemUiVisibility = 0

                override fun onHideCustomView() {
                    super.onHideCustomView()
                    (activity!!.window.decorView as FrameLayout).removeView(mCustomView)

                    this.mCustomView = null
                    activity!!.window.decorView.setSystemUiVisibility(this.mOriginalSystemUiVisibility)
                    activity!!.requestedOrientation = this.mOriginalOrientation
                    this.mCustomViewCallback?.onCustomViewHidden()
                    this.mCustomViewCallback = null

                }


                override fun onShowCustomView(view: View?, callback: CustomViewCallback?) {
                    super.onShowCustomView(view, callback)


                    if (this.mCustomView != null) {
                        onHideCustomView()
                        return
                    }

                    this.mCustomView = view
                    this.mOriginalSystemUiVisibility =
                        activity?.window?.decorView!!.getSystemUiVisibility()
                    this.mOriginalOrientation = activity!!.requestedOrientation
                    this.mCustomViewCallback = callback

                    (activity!!.window.decorView as FrameLayout).addView(
                        mCustomView,
                        FrameLayout.LayoutParams(-1, -1)
                    )
                    activity!!.window.decorView
                        .setSystemUiVisibility(3846 or View.SYSTEM_UI_FLAG_LAYOUT_STABLE)


                }


            }

        }


    }


}