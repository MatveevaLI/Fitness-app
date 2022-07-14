package com.example.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import com.example.fitness_appka.*
import com.google.gson.JsonObject
import io.ktor.client.request.*
import io.ktor.http.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class ProgressResultActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_progress_result)

        var foodValueProteinsTextView = findViewById<TextView>(R.id.foodValueProteinsTextView)
        var foodValueCaloriesTextView = findViewById<TextView>(R.id.foodValueCaloriesTextView)
        var foodValueCarbohydratesTextView = findViewById<TextView>(R.id.foodValueCarbohydratesTextView)
        var foodValueFatsTextView = findViewById<TextView>(R.id.foodValueFatsTextView)

        var maps = mutableMapOf<String, String>()

        maps["date_from"] = intent.getStringExtra("date_from").toString()
        maps["date_to"] = intent.getStringExtra("date_to").toString()

        lateinit var data: JsonObject
        runBlocking {
            launch {
                data = client.get("$server_ip/users/me/progress/") {
                    contentType(ContentType.Application.Json)
                    header("Authorization", auth_token)
                    body = maps
                }
            }
        }

        var fats = data.get("fats").asString
        var calories = data.get("calories").asString
        var carbohydrates = data.get("carbohydrates").asString
        var protein = data.get("protein").asString

        foodValueProteinsTextView.text = protein
        foodValueCaloriesTextView.text = calories
        foodValueFatsTextView.text = fats
        foodValueCarbohydratesTextView.text = carbohydrates

        val BackToProgressButton = findViewById<Button>(R.id.BackToProgressButton)
        BackToProgressButton.setOnClickListener {
            val intent = Intent(this, ProgressActivity::class.java)
            startActivity(intent)
        }

        val menuButton = findViewById<ImageButton>(R.id.menuImageButton)
        menuButton.setOnClickListener{
            val intent = Intent(this, MainMenu::class.java)
            startActivity(intent)
        }

    }
}