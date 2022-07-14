package com.example.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import com.example.fitness_appka.*
import com.google.gson.JsonObject
import com.google.gson.JsonPrimitive
import io.ktor.client.request.*
import io.ktor.http.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class ProductDetailActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product_detail)

        var map = mutableMapOf<String, String>()
        //  Vypis produktu podla name

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

        val foodNameTextView = findViewById<TextView>(R.id.foodNameTextView)
        val foodValueFatsTextView = findViewById<TextView>(R.id.foodValueFatsTextView)
        val foodValueProteinsTextView = findViewById<TextView>(R.id.foodValueProteinsTextView)
        val foodValueCaloriesTextView = findViewById<TextView>(R.id.foodValueCaloriesTextView)
        val foodValueCarbohydratesTextView =
            findViewById<TextView>(R.id.foodValueCarbohydratesTextView)
        val foodValueMassTextView = findViewById<TextView>(R.id.foodValueMassTextView)

        var name = response.get("name").asString
        var fats = response.get("fats").asString
        var calories = response.get("calories").asString
        var carbohydrates = response.get("carbohydrates").asString
        var protein = response.get("protein").asString
        var id = response.get("id").asString
        var mass = response.get("mass").asString
        var userIdProd = response.get("user").asInt

        foodNameTextView.text = name
        foodValueFatsTextView.text = fats
        foodValueCaloriesTextView.text = calories
        foodValueCarbohydratesTextView.text = carbohydrates
        foodValueProteinsTextView.text = protein
        foodValueMassTextView.text = mass

        // editovanie produktu ak je owner
        val editFoodButton = findViewById<Button>(R.id.editFoodButton)

        lateinit var resp: JsonObject
        runBlocking {
            launch {
                resp = client.get("$server_ip/users/me") {
                    contentType(ContentType.Application.Json)
                    header("Authorization", auth_token)
                }
            }
        }
        var userId = resp.get("id").asInt

        if (userId == userIdProd) {
            editFoodButton.visibility = View.VISIBLE
        } else {
            editFoodButton.visibility = View.GONE
        }

        editFoodButton.setOnClickListener {
            val intent = Intent(this, EditFoodActivity::class.java)
            intent.putExtra("id", id.toString())
            intent.putExtra("name", name.toString())
            startActivity(intent)
        }

        val menuButton = findViewById<ImageButton>(R.id.menuImageButton)
        menuButton.setOnClickListener{
            val intent = Intent(this, MainMenu::class.java)
            startActivity(intent)
        }

        // pridat produkt do food diary
        val addNewFoodButton = findViewById<TextView>(R.id.addNewFoodButton)
        var errorMessageTextView = findViewById<TextView>(R.id.errorMessageTextView)
        errorMessageTextView.visibility = View.GONE

        addNewFoodButton.setOnClickListener {

            var numeric = true
            numeric = findViewById<EditText>(R.id.setMassValueEditText).text.toString().matches("-?\\d+(\\.\\d+)?".toRegex())
            var value = findViewById<EditText>(R.id.setMassValueEditText).text.toString()

            if (numeric and (value.toInt() >= 0)) {
                var maps = mutableMapOf<String, String>()
                var type = typeFood

                maps["category"] = type
                maps["product"] = id
                maps["mass"] = value

                lateinit var data: JsonPrimitive
                runBlocking {
                    launch {
                        data = client.post("$server_ip/users/me/nutrition/") {
                            contentType(ContentType.Application.Json)
                            header("Authorization", auth_token)
                            body = maps
                        }
                    }
                }

                // ??? prejst na nutrition activity alebo na food activity
                /*val intent = Intent(this, FoodActivity::class.java)
                intent.putExtra("type", typeFood)
                intent.putExtra("date", dateRec)*/

                val intent = Intent(this, NutritionActivity::class.java)
                startActivity(intent)
            }
            else {
                errorMessageTextView.text = "Value must be greater than 0"
                errorMessageTextView.visibility = View.VISIBLE

                finish();
                startActivity(getIntent())
            }

        }
    }
}