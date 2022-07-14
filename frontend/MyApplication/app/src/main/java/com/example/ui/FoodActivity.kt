package com.example.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.fitness_appka.*
import com.example.ui.adapters.FoodAdapter
import com.example.ui.data.FoodItem
import com.example.ui.data.ListFoodItem
import io.ktor.client.request.*
import io.ktor.http.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

var typeFood: String = ""
var dateRec : String = ""

class FoodActivity : AppCompatActivity(), FoodAdapter.CellClickListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_food)

        var type = intent.getStringExtra("type")
        typeFood = "$type"

        var date = intent.getStringExtra("date")
        dateRec = "$date"

        var map = mutableMapOf<String, String>()

        // getting the recyclerview by its id
        val recyclerView = findViewById<RecyclerView>(R.id.foodRecyclerView)
        val foodText = findViewById<TextView>(R.id.foodTextView)

        when (type) {
            "breakfast" -> {
                foodText.text = "breakfast"
                map["category"] = "breakfast"
            }
            "lunch" -> {
                foodText.text = "lunch"
                map["category"] = "lunch"
            }
            "dinner" -> {
                foodText.text = "dinner"
                map["category"] = "dinner"
            }
            "snack" -> {
                foodText.text = "snack"
                map["category"] = "snack"
            }

        }
        // this creates a vertical layout Manager
        recyclerView.layoutManager = LinearLayoutManager(this)

        map["date"] = date.toString()
        lateinit var data: List<FoodItem>
        runBlocking {
            launch {
                data = client.get("$server_ip/users/me/nutrition/") {
                    contentType(ContentType.Application.Json)
                    header("Authorization", auth_token)
                    body = map
                }
            }
        }

        recyclerView.adapter = FoodAdapter(data, this)

        val addFoodButton = findViewById<TextView>(R.id.addFoodButton)
        addFoodButton.setOnClickListener {
            val intent = Intent(this, AddFoodActivity::class.java)
            intent.putExtra("type", typeFood)
            intent.putExtra("date", dateRec)
            startActivity(intent)
        }

        val menuButton = findViewById<ImageButton>(R.id.menuImageButton)
        menuButton.setOnClickListener{
            val intent = Intent(this, MainMenu::class.java)
            startActivity(intent)
        }

        val backButton = findViewById<TextView>(R.id.backButton)
        backButton.setOnClickListener {
            val intent = Intent(this, NutritionActivity::class.java)
            intent.putExtra("date", dateRec)
            startActivity(intent)
        }
    }

    override fun onCellClickListener(Item: FoodItem) {
        /*Toast.makeText(this,"Cell clicked:)", Toast.LENGTH_SHORT).show()*/
        val intent = Intent(this, EditRecordActivity::class.java)
        intent.putExtra("id", Item.id)
        startActivity(intent)
    }


}