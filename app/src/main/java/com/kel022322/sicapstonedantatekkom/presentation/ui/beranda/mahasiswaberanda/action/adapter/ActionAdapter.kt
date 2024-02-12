package com.kel022322.sicapstonedantatekkom.presentation.ui.beranda.mahasiswaberanda.action.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.kel022322.sicapstonedantatekkom.data.local.action.ActionModel
import com.kel022322.sicapstonedantatekkom.databinding.ItemActionMahasiswaBerandaBinding

class ActionAdapter(private val navController: NavController) :
	RecyclerView.Adapter<ActionAdapter.ActionViewHolder>() {

	private val diffCallback = object : DiffUtil.ItemCallback<ActionModel>() {
		override fun areItemsTheSame(oldItem: ActionModel, newItem: ActionModel): Boolean {
			return oldItem.titleAction == newItem.titleAction
		}

		override fun areContentsTheSame(oldItem: ActionModel, newItem: ActionModel): Boolean {
			return oldItem.hashCode() == newItem.hashCode()
		}
	}

	private val differ = AsyncListDiffer(this, diffCallback)

	fun setList(actions: List<ActionModel>?) {
		differ.submitList(actions)
	}

	inner class ActionViewHolder(private val binding: ItemActionMahasiswaBerandaBinding) :
		RecyclerView.ViewHolder(binding.root) {

		fun bind(actionBeranda: ActionModel) {
			binding.apply {
				val iconDrawable = ContextCompat.getDrawable(itemView.context,
					actionBeranda.iconAction
				)
				ivIconAction.setImageDrawable(iconDrawable)
				tvJudulAction.text = actionBeranda.titleAction
			}

			binding.root.setOnClickListener {
				// Assuming you have an actionId defined in your navigation graph
				val actionId = actionBeranda.actionAction
				navController.navigate(actionId!!)
			}
		}
	}

	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ActionViewHolder {
		val binding =
			ItemActionMahasiswaBerandaBinding.inflate(LayoutInflater.from(parent.context), parent, false)
		return ActionViewHolder(binding)
	}

	override fun onBindViewHolder(holder: ActionViewHolder, position: Int) {
		holder.bind(differ.currentList[position])
	}

	override fun getItemCount(): Int {
		return differ.currentList.size
	}
}
