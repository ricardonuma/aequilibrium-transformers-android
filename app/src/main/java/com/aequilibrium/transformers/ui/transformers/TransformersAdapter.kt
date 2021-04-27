package com.aequilibrium.transformers.ui.transformers

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.aequilibrium.transformers.MyApplication
import com.aequilibrium.transformers.R
import com.aequilibrium.transformers.data.model.Transformer
import com.aequilibrium.transformers.databinding.BattleRowItemBinding
import com.aequilibrium.transformers.databinding.TransformerRowItemBinding
import com.bumptech.glide.Glide
import java.util.*

class TransformersAdapter(
    private val transformerList: ArrayList<Transformer>,
    val context: Context
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private lateinit var transformerClickListener: OnTransformerClickListener
    private lateinit var battleClickListener: OnBattleClickListener

    private val ITEM_TYPE_TRANSFORMER = 0
    private val ITEM_TYPE_BUTTON = 1

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
        if (viewType == ITEM_TYPE_TRANSFORMER) {
            val transformerRowItemBindingItemBinding = TransformerRowItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
            TransformerViewHolder(
                context,
                transformerRowItemBindingItemBinding,
                transformerClickListener
            )
        } else {
            val battleRowItemBinding =
                BattleRowItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            BattleViewHolder(battleRowItemBinding, battleClickListener)
        }

    override fun getItemCount(): Int {
        return transformerList.size
    }

    override fun getItemViewType(position: Int): Int {
        return if (position == 0) {
            ITEM_TYPE_BUTTON
        } else {
            ITEM_TYPE_TRANSFORMER
        }
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val transformer = transformerList[position]

        if (holder is TransformerViewHolder) {
            holder.bind(TransformerRowItem(transformer))
        } else {
            (holder as BattleViewHolder).bind()
        }
    }

    fun addTransformerList(transformerList: List<Transformer>) {
        this.transformerList.clear()
        this.transformerList.addAll(transformerList)
        this.transformerList.add(Transformer("", "", "", 0, 0, 0, 0, 0, 0, 0, 0, ""))
        notifyDataSetChanged()
    }

    fun addBattleClickListener(battleClickListener: OnBattleClickListener) {
        this.battleClickListener = battleClickListener
    }

    interface OnBattleClickListener {
        fun onBattleClickListener()
    }

    class BattleViewHolder(
        private val battleRowItemBinding: BattleRowItemBinding,
        private val onBattleClickListener: OnBattleClickListener
    ) : RecyclerView.ViewHolder(battleRowItemBinding.root) {
        fun bind() {
            battleRowItemBinding.battleButton.setOnClickListener {
                onBattleClickListener.onBattleClickListener()
            }
        }
    }

    fun addTransformerClickListener(transformerClickListener: OnTransformerClickListener) {
        this.transformerClickListener = transformerClickListener
    }

    class TransformerViewHolder(
        private val context: Context,
        private val binding: TransformerRowItemBinding,
        private val transformerClickListener: OnTransformerClickListener
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(transformerRowItem: TransformerRowItem) {
            binding.transformerRowItem = transformerRowItem

            binding.container.setBackgroundColor(
                if (transformerRowItem.team == "A") {
                    ContextCompat.getColor(context, R.color.colorRed)
                } else {
                    ContextCompat.getColor(context, R.color.colorPurple)
                }
            )

            Glide.with(context)
                .load(transformerRowItem.team_icon)
                .into(binding.teamIconImageView)

            binding.container.setOnClickListener {
                transformerClickListener.onTransformerClick(transformerRowItem.transformerObject)
            }
        }
    }

    interface OnTransformerClickListener {
        fun onTransformerClick(transformer: Transformer)
    }
}

data class TransformerRowItem(val transformer: Transformer) {

    val transformerObject: Transformer
        get() {
            return transformer
        }

    val name: String
        get() {
            return transformer.name
        }

    val team: String
        get() {
            return transformer.team
        }

    val team_icon: String
        get() {
            return transformer.team_icon
        }

    val strength: String
        get() {
            return MyApplication.getInstance().getString(
                R.string.transformer_stats_values,
                MyApplication.getInstance().getString(R.string.transformer_strength),
                transformer.strength.toString()
            )
        }

    val intelligence: String
        get() {
            return MyApplication.getInstance().getString(
                R.string.transformer_stats_values,
                MyApplication.getInstance().getString(R.string.transformer_intelligence),
                transformer.intelligence.toString()
            )
        }

    val speed: String
        get() {
            return MyApplication.getInstance().getString(
                R.string.transformer_stats_values,
                MyApplication.getInstance().getString(R.string.transformer_speed),
                transformer.speed.toString()
            )
        }

    val endurance: String
        get() {
            return MyApplication.getInstance().getString(
                R.string.transformer_stats_values,
                MyApplication.getInstance().getString(R.string.transformer_endurance),
                transformer.endurance.toString()
            )
        }

    val rank: String
        get() {
            return MyApplication.getInstance().getString(
                R.string.transformer_stats_values,
                MyApplication.getInstance().getString(R.string.transformer_rank),
                transformer.rank.toString()
            )
        }

    val courage: String
        get() {
            return MyApplication.getInstance().getString(
                R.string.transformer_stats_values,
                MyApplication.getInstance().getString(R.string.transformer_courage),
                transformer.courage.toString()
            )
        }

    val firepower: String
        get() {
            return MyApplication.getInstance().getString(
                R.string.transformer_stats_values,
                MyApplication.getInstance().getString(R.string.transformer_firepower),
                transformer.firepower.toString()
            )
        }

    val skill: String
        get() {
            return MyApplication.getInstance().getString(
                R.string.transformer_stats_values,
                MyApplication.getInstance().getString(R.string.transformer_skill),
                transformer.skill.toString()
            )
        }
}

