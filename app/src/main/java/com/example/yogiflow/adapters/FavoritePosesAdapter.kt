package com.example.yogiflow.adapters

import android.view.*
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.yogiflow.R
import com.example.yogiflow.data.database.entities.FavoritesEntity
import com.example.yogiflow.databinding.FavoritePosesRowLayoutBinding
import com.example.yogiflow.ui.fragments.favorites.FavoritePosesFragmentDirections
import com.example.yogiflow.util.PosesDiffUtil
import com.example.yogiflow.viewmodels.MainViewModel
import com.google.android.material.snackbar.Snackbar

class FavoritePosesAdapter(
    private val requireActivity: FragmentActivity,
    private val mainViewModel: MainViewModel
) : RecyclerView.Adapter<FavoritePosesAdapter.MyViewHolder>(), ActionMode.Callback {

    private var multiSelection = false

    private lateinit var mActionMode: ActionMode
    private lateinit var rootView: View

    private var selectedPoses = arrayListOf<FavoritesEntity>()
    private var myViewHolders = arrayListOf<MyViewHolder>()
    private var favoritePoses = emptyList<FavoritesEntity>()

    class MyViewHolder(val binding: FavoritePosesRowLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(favoritesEntity: FavoritesEntity) {
            binding.favoritesEntity = favoritesEntity
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): MyViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = FavoritePosesRowLayoutBinding.inflate(layoutInflater, parent, false)
                return MyViewHolder(binding)
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        myViewHolders.add(holder)
        rootView = holder.itemView.rootView

        val currentPose = favoritePoses[position]
        holder.bind(currentPose)

        saveItemStateOnScroll(currentPose, holder)

        /**
         * Single Click Listener
         * */
        holder.binding.favoritePosesRowLayout.setOnClickListener {
            if (multiSelection) {
                applySelection(holder, currentPose)
            } else {
                val action =
                    FavoritePosesFragmentDirections.actionFavoritePosesFragmentToDetailsActivity(
                        currentPose.result
                    )
                holder.itemView.findNavController().navigate(action)
            }
        }

        /**
         * Long Click Listener
         * */
        holder.binding.favoritePosesRowLayout.setOnLongClickListener {
            if (!multiSelection) {
                multiSelection = true
                requireActivity.startActionMode(this)
                applySelection(holder, currentPose)
                true
            } else {
                applySelection(holder, currentPose)
                true
            }

        }

    }

    private fun saveItemStateOnScroll(currentPose: FavoritesEntity, holder: MyViewHolder){
        if (selectedPoses.contains(currentPose)) {
            changePoseStyle(holder, R.color.cardBackgroundLightColor, R.color.colorPrimary)
        } else {
            changePoseStyle(holder, R.color.cardBackgroundColor, R.color.strokeColor)
        }
    }

    private fun applySelection(holder: MyViewHolder, currentPose: FavoritesEntity) {
        if (selectedPoses.contains(currentPose)) {
            selectedPoses.remove(currentPose)
            changePoseStyle(holder, R.color.cardBackgroundColor, R.color.strokeColor)
            applyActionModeTitle()
        } else {
            selectedPoses.add(currentPose)
            changePoseStyle(holder, R.color.cardBackgroundLightColor, R.color.colorPrimary)
            applyActionModeTitle()
        }
    }

    private fun changePoseStyle(holder: MyViewHolder, backgroundColor: Int, strokeColor: Int) {
        holder.binding.favoritePosesRowLayout.setBackgroundColor(
            ContextCompat.getColor(requireActivity, backgroundColor)
        )
        holder.binding.favoriteRowCardView.strokeColor =
            ContextCompat.getColor(requireActivity, strokeColor)
    }

    private fun applyActionModeTitle() {
        when (selectedPoses.size) {
            0 -> {
                mActionMode.finish()
                multiSelection = false
            }
            1 -> {
                mActionMode.title = "${selectedPoses.size} item selected"
            }
            else -> {
                mActionMode.title = "${selectedPoses.size} items selected"
            }
        }
    }

    override fun getItemCount(): Int {
        return favoritePoses.size
    }

    override fun onCreateActionMode(actionMode: ActionMode?, menu: Menu?): Boolean {
        actionMode?.menuInflater?.inflate(R.menu.favorites_contextual_menu, menu)
        mActionMode = actionMode!!
        applyStatusBarColor(R.color.contextualStatusBarColor)
        return true
    }

    override fun onPrepareActionMode(actionMode: ActionMode?, menu: Menu?): Boolean {
        return true
    }

    override fun onActionItemClicked(actionMode: ActionMode?, menu: MenuItem?): Boolean {
        if (menu?.itemId == R.id.delete_favorite_pose_menu) {
            selectedPoses.forEach {
                mainViewModel.deleteFavoritePose(it)
            }
            showSnackBar("${selectedPoses.size} Pose/s removed.")

            multiSelection = false
            selectedPoses.clear()
            actionMode?.finish()
        }
        return true
    }

    override fun onDestroyActionMode(actionMode: ActionMode?) {
        myViewHolders.forEach { holder ->
            changePoseStyle(holder, R.color.cardBackgroundColor, R.color.strokeColor)
        }
        multiSelection = false
        selectedPoses.clear()
        applyStatusBarColor(R.color.statusBarColor)
    }

    private fun applyStatusBarColor(color: Int) {
        requireActivity.window.statusBarColor =
            ContextCompat.getColor(requireActivity, color)
    }

    fun setData(newFavoritePoses: List<FavoritesEntity>) {
        val favoritePosesDiffUtil =
            PosesDiffUtil(favoritePoses, newFavoritePoses)
        val diffUtilResult = DiffUtil.calculateDiff(favoritePosesDiffUtil)
        favoritePoses = newFavoritePoses
        diffUtilResult.dispatchUpdatesTo(this)
    }

    private fun showSnackBar(message: String) {
        Snackbar.make(
            rootView,
            message,
            Snackbar.LENGTH_SHORT
        ).setAction("Okay") {}
            .show()
    }

    fun clearContextualActionMode() {
        if (this::mActionMode.isInitialized) {
            mActionMode.finish()
        }
    }

}