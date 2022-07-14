package com.example.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageButton
import android.widget.TextView
import com.example.fitness_appka.*
import com.example.ui.data.FoodItem
import com.google.gson.JsonObject
import com.google.gson.JsonPrimitive
import io.ktor.client.request.*
import io.ktor.http.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class EditRecordActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_record)

        var record_id = intent.getStringExtra("id")

        lateinit var response: JsonObject
        runBlocking {
            launch {
                response = client.get("$server_ip/users/me/nutrition/$record_id/") {
                    contentType(ContentType.Application.Json)
                    header("Authorization", auth_token)
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
        val foodValueMassTextView = findViewById<TextView>(R.id.foodValueMassTextView)
        val foodValueFatsTextView = findViewById<TextView>(R.id.foodValueFatsTextView)
        val foodValueProteinsTextView = findViewById<TextView>(R.id.foodValueProteinsTextView)
        val foodValueCaloriesTextView = findViewById<TextView>(R.id.foodValueCaloriesTextView)
        val foodValueCarbohydratesTextView = findViewById<TextView>(R.id.foodValueCarbohydratesTextView)

        var name = response.get("name").asString
        var fats = response.get("fats").asString
        var calories = response.get("calories").asString
        var carbohydrates = response.get("carbohydrates").asString
        var protein = response.get("protein").asString
        var mass = response.get("mass").asString
        // record_id = response.get("id").asString

        foodNameTextView.text = name
        foodValueMassTextView.text = mass
        foodValueFatsTextView.text = fats
        foodValueProteinsTextView.text = protein
        foodValueCaloriesTextView.text = calories
        foodValueCarbohydratesTextView.text = carbohydrates

        //ulozit editovanie
        val saveEditedRecordButton = findViewById<TextView>(R.id.saveEditedRecordButton)
        val setMassValueEditText = findViewById<TextView>(R.id.setMassValueEditText)
        saveEditedRecordButton.setOnClickListener {

            var maps = mutableMapOf<String, String>()

            maps["name"] = foodNameTextView.text.toString()
            maps["protein"] = foodValueProteinsTextView.text.toString()
            maps["fats"] = foodValueFatsTextView.text.toString()
            maps["carbohydrates"] = foodValueCarbohydratesTextView.text.toString()
            maps["mass"] = setMassValueEditText.text.toString()
            maps["calories"] = foodValueCaloriesTextView.text.toString()

            maps["product"] = response.get("product").asString
            maps["category"] = response.get("category").asString

            lateinit var data: JsonPrimitive
            runBlocking {
                launch {
                    data = client.put("$server_ip/users/me/nutrition/$record_id/") {
                        contentType(ContentType.Application.Json)
                        header("Authorization", auth_token)
                        body = maps
                    }
                }
            }

            val intent = Intent(this, FoodActivity::class.java)
            intent.putExtra("type", typeFood)
            intent.putExtra("date", dateRec)
            startActivity(intent)

        }
    }
}