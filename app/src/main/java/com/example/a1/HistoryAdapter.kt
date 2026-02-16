package com.example.a1

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.a1.databinding.ItemHistoryBinding

class HistoryAdapter(
    private val onEditClick: (position: Int, text: String) -> Unit,
    private val onDeleteClick: (position: Int) -> Unit
) : RecyclerView.Adapter<HistoryAdapter.HistoryViewHolder>() {

    private var historyList: List<String> = emptyList()

    inner class HistoryViewHolder(private val binding: ItemHistoryBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(text: String, position: Int) {
            binding.txtNumber.text = "${position + 1}."
            binding.txtContent.text = text

            binding.btnEdit.setOnClickListener {
                onEditClick(position, text)
            }

            binding.btnDelete.setOnClickListener {
                onDeleteClick(position)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryViewHolder {
        val binding = ItemHistoryBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return HistoryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: HistoryViewHolder, position: Int) {
        val item = historyList[position]
        holder.bind(item, position)
    }

    override fun getItemCount(): Int {
        return historyList.size
    }

    // ✅ OPTIMIZED: Specific notify methods

    // Called when entire list replaced
    fun updateList(newList: List<String>) {
        historyList = newList
        notifyDataSetChanged()  // OK here - entire list changed
    }

    // Called when single item edited
    fun updateItem(position: Int, newText: String) {
        if (position in historyList.indices) {
            val mutableList = historyList.toMutableList()
            mutableList[position] = newText
            historyList = mutableList
            notifyItemChanged(position)  // ✅ Only this item!
        }
    }

    // Called when single item added
    fun addItem(text: String) {
        val mutableList = historyList.toMutableList()
        mutableList.add(text)
        historyList = mutableList
        notifyItemInserted(historyList.size - 1)  // ✅ Only new item!
    }

    // Called when single item deleted
    fun removeItem(position: Int) {
        if (position in historyList.indices) {
            val mutableList = historyList.toMutableList()
            mutableList.removeAt(position)
            historyList = mutableList
            notifyItemRemoved(position)  // ✅ Only removed item!

            // Update numbers for items after removed one
            if (position < historyList.size) {
                notifyItemRangeChanged(position, historyList.size - position)
            }
        }
    }

    // Called when all items cleared
    fun clearAll() {
        val size = historyList.size
        historyList = emptyList()
        notifyItemRangeRemoved(0, size)  // ✅ All items at once!
    }
}