package com.example.yogiflow.ui.fragments.overview

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import coil.load
import com.example.yogiflow.R
import com.example.yogiflow.bindingadapters.PosesRowBinding
import com.example.yogiflow.databinding.FragmentOverviewBinding
import com.example.yogiflow.models.Result
import com.example.yogiflow.util.Constants.Companion.RECIPE_RESULT_KEY

class OverviewFragment : Fragment() {

    private var _binding: FragmentOverviewBinding? = null
    private val binding get() = _binding!!

    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentOverviewBinding.inflate(inflater, container, false)

        val args = arguments
        val myBundle: Result = args!!.getParcelable<Result>(RECIPE_RESULT_KEY) as Result

        binding.mainImageView.load(myBundle.img)
        binding.timeTextView.text = myBundle.pose_level.ifEmpty { "intermediate" }
        binding.titleTextView.text = myBundle.name_eng
        binding.aliasName.text = "Alias: ${myBundle.alias.ifEmpty { "no info" }}"
        binding.benefits.text = "Benefits: ${myBundle.benefits.ifEmpty { "no info" }}"
        binding.sanskritName.text = "Sanskrit name: ${myBundle.name_san.ifEmpty { "no info" }}"
        binding.poseType.text = "Pose Type: ${myBundle.pose_type.ifEmpty { "no info" }}"

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}