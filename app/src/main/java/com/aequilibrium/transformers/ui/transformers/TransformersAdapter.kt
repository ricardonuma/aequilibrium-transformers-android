package com.aequilibrium.transformers.ui.transformers

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.aequilibrium.transformers.R
import com.aequilibrium.transformers.data.model.Transformer
import com.aequilibrium.transformers.databinding.TransformerRowItemBinding
import com.bumptech.glide.Glide
import java.util.*

class TransformersAdapter(
    private val transformerList: ArrayList<Transformer>,
    val context: Context
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private lateinit var transformerClickListener: OnTransformerClickListener


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val transformerRowItemBindingItemBinding =
            TransformerRowItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TransformerViewHolder(
            context,
            transformerRowItemBindingItemBinding,
            transformerClickListener
        )
    }

    override fun getItemCount(): Int {
        return transformerList.size
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val transformer = transformerList[position]

        if (holder is TransformerViewHolder) {
            holder.bind(transformer)
        }
    }

    fun addTransformerClickListener(transformerClickListener: OnTransformerClickListener) {
        this.transformerClickListener = transformerClickListener
    }

    fun addTransformerList(transformerList: List<Transformer>) {
        this.transformerList.clear()
        this.transformerList.addAll(transformerList)
        notifyDataSetChanged()
    }

    class TransformerViewHolder(
        private val context: Context,
        private val binding: TransformerRowItemBinding,
        private val transformerClickListener: OnTransformerClickListener
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(transformer: Transformer) {
            binding.transformer = transformer

            binding.container.setBackgroundColor(
                if (transformer.team == "A") {
                    ContextCompat.getColor(context, R.color.colorRed)
                } else {
                    ContextCompat.getColor(context, R.color.colorPurple)
                }
            )

            Glide.with(context)
                .load(transformer.team_icon)
                .into(binding.teamIconImageView)
            binding.container.setOnClickListener {
                transformerClickListener.onTransformerClick(transformer)
            }
        }
    }

    interface OnTransformerClickListener {
        fun onTransformerClick(transformer: Transformer)
    }
}

