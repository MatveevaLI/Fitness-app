import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.fitness_appka.R
import com.example.ui.data.ListFoodItem
import android.widget.Toast
import com.example.ui.AddFoodActivity
import android.widget.Filter
import java.util.*
import kotlin.collections.ArrayList

class ListFoodAdapter(private val mList: List<ListFoodItem>, private val cellClickListener: CellClickListener) : RecyclerView.Adapter<ListFoodAdapter.ViewHolder>() {

    interface CellClickListener {
        fun onCellClickListener(Item: ListFoodItem)
    }

    // create new views
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // inflates the card_view_design view
        // that is used to hold list item
        val view = LayoutInflater.from(parent.context).inflate(R.layout.activity_list_food_item, parent, false)
        return ViewHolder(view)
    }


    // binds the list items to a view
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val Item = mList[position]

        // sets the text to the textview from our itemHolder class
        holder.name.text = Item.name

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
        val name: TextView = itemView.findViewById(R.id.nameFoodTextView)
    }
}

