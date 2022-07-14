package com.example.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.fitness_appka.R
import com.example.ui.data.FoodItem
import com.example.ui.data.ListFoodItem

class FoodAdapter (private val mList: List<FoodItem>, private val cellClickListener: CellClickListener) : RecyclerView.Adapter<FoodAdapter.ViewHolder>() {

    interface CellClickListener {
        fun onCellClickListener(Item: FoodItem)
    }
    // create new views
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // inflates the card_view_design view
        // that is used to hold list item
        val view = LayoutInflater.from(parent.context).inflate(R.layout.activity_food_item, parent, false)

        return ViewHolder(view)
    }

    // binds the list items to a view
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val Item = mList[position]

        // sets the text to the textview from our itemHolder class
        holder.name.text = Item.name
        holder.fats.text = Item.fats
        holder.calories.text = Item.calories
        holder.carbohydrates.text = Item.carbohydrates
        holder.proteins.text = Item.proteins
        holder.mass.text = Item.mass

        holder.itemView.setOnClickListener {
            cellClickListener.onCellClickListener(Item)
        }

    }

    // return the number of the items in the list
    override fun getItemCount(): Int {
        return mList.size
    }

    // Holds the views for adding it to image and text
    class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {
        val name: TextView = itemView.findViewById(R.id.foodTextView)
        val fats: TextView = itemView.findViewById(R.id.valueFatsTextView)
        val calories: TextView = itemView.findViewById(R.id.valueCaloriesTextView)
        val carbohydrates: TextView = itemView.findViewById(R.id.valueCarbohydratesTextView)
        val proteins: TextView = itemView.findViewById(R.id.valueProteinsTextView)
        val mass: TextView = itemView.findViewById(R.id.valueMassTextView)
    }
}