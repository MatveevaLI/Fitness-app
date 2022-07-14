package com.example.ui

import android.R.attr.button
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.fitness_appka.*
import com.google.gson.JsonPrimitive
import io.ktor.client.features.*
import io.ktor.client.request.*
import io.ktor.http.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking


var type_food: String = ""
class CreateNewFoodActivity : AppCompatActivity() {

    private fun String.onlyLetters() = all { it.isLetter() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_new_food)

        val saveButton = findViewById<Button>(R.id.saveButton)


        var name = findViewById<EditText>(R.id.nameEditText)
        var fats = findViewById<EditText>(R.id.fatsEditText)
        var proteins = findViewById<EditText>(R.id.proteinsEditText)
        var carbohydrates = findViewById<EditText>(R.id.carbohydratesEditText)
        var calories = findViewById<EditText>(R.id.caloriesEditText)
        var mass = findViewById<EditText>(R.id.massEditText)

        var errorMessageMassTextView = findViewById<TextView>(R.id.errorMessageMassTextView)
        var errorMessageCaloriesTextView = findViewById<TextView>(R.id.errorMessageCaloriesTextView)
        var errorMessageProteinsTextView = findViewById<TextView>(R.id.errorMessageProteinsTextView)
        var errorMessageCarbohydratesTextView = findViewById<TextView>(R.id.errorMessageCarbohydratesTextView)
        var errorMessageFatsTextView = findViewById<TextView>(R.id.errorMessageFatsTextView)
        var errorMessageNameTextView = findViewById<TextView>(R.id.errorMessageNameTextView)
        errorMessageMassTextView.visibility = View.GONE
        errorMessageCaloriesTextView.visibility = View.GONE
        errorMessageProteinsTextView.visibility = View.GONE
        errorMessageCarbohydratesTextView.visibility = View.GONE
        errorMessageFatsTextView.visibility = View.GONE
        errorMessageNameTextView.visibility = View.GONE


        val menuButton = findViewById<ImageButton>(R.id.menuImageButton)
        menuButton.setOnClickListener{
            val intent = Intent(this, MainMenu::class.java)
            startActivity(intent)
        }

        var radioGroup = findViewById<RadioGroup>(R.id.myRadioGroup);

        radioGroup.setOnCheckedChangeListener { _, checkedId -> // find which radio button is selected
            type_food = when (checkedId) {
                R.id.meatRadioButton -> {
                    "meat"
                }
                R.id.cerealsRadioButton -> {
                    "cereals"
                }
                R.id.vegetablesRadioButton -> {
                    "vegetables"
                }
                else -> {
                    "fruits"
                }
            }
        }

        var meatRadioButton = findViewById<RadioButton>(R.id.meatRadioButton)
        var cerealsRadioButton = findViewById<RadioButton>(R.id.cerealsRadioButton)
        var vegetablesRadioButton = findViewById<RadioButton>(R.id.vegetablesRadioButton)
        var fruitsRadioButton = findViewById<RadioButton>(R.id.fruitsRadioButton)

        saveButton.setOnClickListener {

            val name = name.text.replace("\\s".toRegex(), "")
            val fats = fats.text
            val proteins = proteins.text
            val carbohydrates = carbohydrates.text
            val calories = calories.text
            val mass = mass.text

            if (name.toString().onlyLetters()) {
                if (fats.toString().isNotEmpty()) {
                    if (proteins.toString().isNotEmpty()) {
                        if (carbohydrates.toString().isNotEmpty()) {
                            if (calories.toString().isNotEmpty()) {
                                if (mass.toString().isNotEmpty()) {
                                    var map = mutableMapOf<String, String>()
                                    map["name"] = findViewById<EditText>(R.id.nameEditText).text.toString()
                                    map["fats"] = fats.toString()
                                    map["protein"] = proteins.toString()
                                    map["carbohydrates"] = carbohydrates.toString()
                                    map["calories"] = calories.toString()
                                    map["mass"] = mass.toString()
                                    map["type"] = type_food
                                    lateinit var response: JsonPrimitive
                                    var requestAlright = true
                                    try {
                                        runBlocking {
                                            launch {
                                                response = client.post("$server_ip/products/") {
                                                    contentType(ContentType.Application.Json)
                                                    header("Authorization", auth_token)
                                                    body = map
                                                }
                                            }
                                        }
                                    }
                                    catch (e: ClientRequestException){
                                        requestAlright = false
                                    }
                                    if(requestAlright){
                                        val intent = Intent(this, NutritionActivity::class.java)
                                        startActivity(intent)
                                    }

                                }
                                else {
                                    errorMessageMassTextView.text = "mass value required"
                                    errorMessageMassTextView.visibility = View.VISIBLE
                                }
                            }
                            else{
                                errorMessageCaloriesTextView.text = "calories value required"
                                errorMessageCaloriesTextView.visibility = View.VISIBLE
                            }
                        }
                        else {
                            errorMessageCarbohydratesTextView.text = "carbohydrates value required"
                            errorMessageCarbohydratesTextView.visibility = View.VISIBLE
                        }
                    }
                    else {
                        errorMessageProteinsTextView.text = "proteins value required"
                        errorMessageProteinsTextView.visibility = View.VISIBLE
                    }
                }
                else {
                    errorMessageFatsTextView.text = "fats value required"
                    errorMessageFatsTextView.visibility = View.VISIBLE
                }
            }
            else {
                errorMessageNameTextView.text = "The name can only contain letters"
                errorMessageNameTextView.visibility = View.VISIBLE
            }
    }

        val backButton = findViewById<Button>(R.id.backButton)
        backButton.setOnClickListener {
            val intent = Intent(this, NutritionActivity::class.java)
            startActivity(intent)
        }
    }
}