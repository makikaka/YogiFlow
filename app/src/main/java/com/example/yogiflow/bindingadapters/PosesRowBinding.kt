package com.example.yogiflow.bindingadapters

import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.databinding.BindingAdapter
import androidx.navigation.findNavController
import coil.load
import com.example.yogiflow.R
import com.example.yogiflow.models.Result
import com.example.yogiflow.ui.fragments.poses.PosesFragmentDirections
import org.jsoup.Jsoup
import java.lang.Exception

class PosesRowBinding {

    companion object {

        @BindingAdapter("onPoseClickListener")
        @JvmStatic
        fun onPoseClickListener(poseRowLayout: ConstraintLayout, result: Result) {
            Log.d("onPoseClickListener", "CALLED")
            poseRowLayout.setOnClickListener {
                try {
                    val action =
                        PosesFragmentDirections.actionPosesFragmentToDetailsActivity(result)
                    poseRowLayout.findNavController().navigate(action)
                } catch (e: Exception) {
                    Log.d("onPoseClickListener", e.toString())
                }
            }
        }

        @BindingAdapter("loadImageFromUrl")
        @JvmStatic
        fun loadImageFromUrl(imageView: ImageView, imageUrl: String) {
            imageView.load(imageUrl) {
                crossfade(600)
                error(R.drawable.ic_error_placeholder)
            }
        }

        @BindingAdapter("parseHtml")
        @JvmStatic
        fun parseHtml(textView: TextView, description: String?){
            if(description != null) {
                val desc = Jsoup.parse(description).text()
                textView.text = desc
            }
        }

    }

}