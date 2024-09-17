package com.mosys.billing

import android.app.Activity
import android.app.Fragment
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ProgressBar
import android.widget.TextView

/**
 * BrowseErrorActivity shows how to use ErrorFragment.
 */
class BrowseErrorActivity : Activity() {

    private lateinit var errorFragment: ErrorFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        errorFragment = ErrorFragment()
        fragmentManager
            .beginTransaction()
            .add(android.R.id.content, errorFragment)
            .commit()
    }

    class ErrorFragment : Fragment() {
        private lateinit var errorView: TextView
        private lateinit var progressBar: ProgressBar

        override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
        ): View? {
            val frameLayout = FrameLayout(activity)
            frameLayout.layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )

            errorView = TextView(activity)
            errorView.layoutParams = FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            ).apply {
                gravity = Gravity.CENTER
            }
            errorView.text = "An error occurred."
            errorView.setTextColor(resources.getColor(android.R.color.white))

            progressBar = ProgressBar(activity)
            progressBar.layoutParams = FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            ).apply {
                gravity = Gravity.CENTER
            }
            progressBar.visibility = View.GONE

            frameLayout.addView(errorView)
            frameLayout.addView(progressBar)

            return frameLayout
        }

        fun setErrorContent() {
            errorView.visibility = View.VISIBLE
            progressBar.visibility = View.GONE
        }
    }
}