package com.example.yogiflow.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.yogiflow.databinding.PosesRowLayoutBinding
import com.example.yogiflow.models.Poses
import com.example.yogiflow.models.Result
import com.example.yogiflow.util.PosesDiffUtil

class PosesAdapter : RecyclerView.Adapter<PosesAdapter.MyViewHolder>() {

    public var poses = emptyList<Result>()

    class MyViewHolder(private val binding: PosesRowLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(result: Result){
            binding.result = result
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): MyViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = PosesRowLayoutBinding.inflate(layoutInflater, parent, false)
                return MyViewHolder(binding)
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentPose = poses[position]
        holder.bind(currentPose)
    }

    override fun getItemCount(): Int {
        return poses.size
    }

    fun setData(newData: Poses){
        val posesDiffUtil =
            PosesDiffUtil(poses, newData.results)
        val diffUtilResult = DiffUtil.calculateDiff(posesDiffUtil)
        poses = newData.results
        diffUtilResult.dispatchUpdatesTo(this)
    }
}