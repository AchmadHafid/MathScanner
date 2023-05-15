package io.github.achmadhafid.mathscanner.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import io.github.achmadhafid.mathscanner.R

class ScanResultAdapter() : ListAdapter<ScanResult, ScanResultAdapter.ViewHolder>(ScanResultDiffCallback) {

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        private val tvOperation: TextView = view.findViewById(R.id.tv_operation)
        private val tvResult: TextView = view.findViewById(R.id.tv_result)
        private val tvTimestamp: TextView = view.findViewById(R.id.tv_timestamp)
        private val ivSource: ImageView = view.findViewById(R.id.iv_source)

        infix fun bind(scanResult: ScanResult) {
            tvOperation.text = scanResult.operation
            tvResult.text = "${scanResult.result}"
            tvTimestamp.text = scanResult.formattedTimestamp
            scanResult.sourceUri?.let {
                ivSource.setImageURI(it)
            } ?: ivSource.setImageResource(R.drawable.img_source_placeholder)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_scan_result, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder bind getItem(position)
    }

}

private object ScanResultDiffCallback : DiffUtil.ItemCallback<ScanResult>() {

    override fun areItemsTheSame(oldItem: ScanResult, newItem: ScanResult) =
        oldItem.timestamp == newItem.timestamp

    override fun areContentsTheSame(oldItem: ScanResult, newItem: ScanResult) =
        oldItem == newItem

}
