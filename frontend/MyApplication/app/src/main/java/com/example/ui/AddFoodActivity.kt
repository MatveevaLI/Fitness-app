package com.example.ui

import ListFoodAdapter
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.*
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.fitness_appka.*
import com.example.ui.data.ListFoodItem
import com.google.gson.JsonObject
import io.ktor.client.request.*
import io.ktor.http.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class AddFoodActivity : AppCompatActivity(), ListFoodAdapter.CellClickListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_food)

        // getting the recyclerview by its id

        val recyclerView = findViewById<RecyclerView>(R.id.listFoodRecyclerView)

        // this creates a vertical layout Manager
        recyclerView.layoutManager = LinearLayoutManager(this)

        lateinit var data: List<ListFoodItem>
        runBlocking {
            launch {
                data = client.get("$server_ip/products/") {
                    contentType(ContentType.Application.Json)
                    header("Authorization", auth_token)
                }
            }
        }
        // tomuto kus nerozumiem
        recyclerView.adapter = ListFoodAdapter(data, this)

        val menuButton = findViewById<ImageButton>(R.id.menuImageButton)
        menuButton.setOnClickListener{
            val intent = Intent(this, MainMenu::class.java)
            startActivity(intent)
        }

        val backButton = findViewById<TextView>(R.id.backButton)
        backButton.setOnClickListener {
            val intent = Intent(this, FoodActivity::class.java)
            intent.putExtra("type", typeFood)
            intent.putExtra("date", dateRec)
            startActivity(intent)
        }
    }

    override fun onCellClickListener(Item: ListFoodItem) {
        /*Toast.makeText(this,"Cell clicked!!!!", Toast.LENGTH_SHORT).show()*/
        val intent = Intent(this, ProductDetailActivity::class.java)
        intent.putExtra("name", Item.name)
        intent.putExtra("date", dateRec)
        startActivity(intent)
    }
}