package com.example.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageButton
import android.widget.TextView
import com.example.fitness_appka.*
import com.google.gson.JsonObject
import com.google.gson.JsonPrimitive
import io.ktor.client.request.*
import io.ktor.http.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.util.regex.Pattern

class EditFoodActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_food)

        var map = mutableMapOf<String, String>()
        val textPattern = "[a-zA-Z]+"

        // Najst produkt podla name
        var productName = intent.getStringExtra("name")
        map["name"] = productName.toString()
        lateinit var response: JsonObject
        runBlocking {
            launch {
                response = client.get("$server_ip/product/") {
                    contentType(ContentType.Application.Json)
                    header("Authorization", auth_token)
                    body = map
                }
            }
        }

        val menuButton = findViewById<ImageButton>(R.id.menuImageButton)
        menuButton.setOnClickListener{
            val intent = Intent(this, MainMenu::class.java)
            startActivity(intent)
        }

        // Vyplnit vsetky polia pre produkt
        val foodNameTextView = findViewById<TextView>(R.id.foodNameTextView)
        val foodValueFatsEditText = findViewById<TextView>(R.id.foodValueFatsEditText)
        val foodValueProteinsEditText = findViewById<TextView>(R.id.foodValueProteinsEditText)
        val foodValueCaloriesEditText = findViewById<TextView>(R.id.foodValueCaloriesEditText)
        val foodValueCarbohydratesEditText =
            findViewById<TextView>(R.id.foodValueCarbohydratesEditText)
        val foodValueMassEditText = findViewById<TextView>(R.id.foodValueMassEditText)

        var name = response.get("name").asString
        var fats = response.get("fats").asString
        var calories = response.get("calories").asString
        var carbohydrates = response.get("carbohydrates").asString
        var protein = response.get("protein").asString
        var mass = response.get("mass").asString
        var id = response.get("id").asString

        foodNameTextView.text = name
        foodValueFatsEditText.text = fats
        foodValueCaloriesEditText.text = calories
        foodValueCarbohydratesEditText.text = carbohydrates
        foodValueProteinsEditText.text = protein
        foodValueMassEditText.text = mass

        //ulozit editovanie
        val saveEditedFoodButton = findViewById<TextView>(R.id.saveEditedFoodButton)
        saveEditedFoodButton.setOnClickListener {

            var maps = mutableMapOf<String, String>()

            maps["name"] = foodNameTextView.text.toString()
            maps["protein"] = foodValueProteinsEditText.text.toString()
            maps["fats"] = foodValueFatsEditText.text.toString()
            maps["carbohydrates"] = foodValueCarbohydratesEditText.text.toString()
            maps["mass"] = foodValueMassEditText.text.toString()
            maps["calories"] = foodValueCaloriesEditText.text.toString()

            lateinit var data: JsonPrimitive
            runBlocking {
                launch {
                    data = client.put("$server_ip/products/$id/") {
                        contentType(ContentType.Application.Json)
                        header("Authorization", auth_token)
                        body = maps
                    }
                }
            }
            val intent = Intent(this, ProductDetailActivity::class.java)
            intent.putExtra("name", name.toString())
            startActivity(intent)
        }
    }
}