package com.example.ui

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.DatePickerDialog.OnDateSetListener
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.fitness_appka.MainActivity
import com.example.fitness_appka.MainMenu
import com.example.fitness_appka.R
import java.util.*


class NutritionActivity : AppCompatActivity() {

    private var datePickerDialog: DatePickerDialog? = null
    private var dateButton: Button? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_nutrition)
        initDatePicker()

        var dateV  = intent.getStringExtra("date")

        dateButton = findViewById(R.id.datePickerButton)
        if (dateV == null) {
            dateButton?.text = getTodaysDate()
        }
        else {
            dateButton?.text = dateV
        }

        val breakfastButton = findViewById<Button>(R.id.breakfastButton)
        breakfastButton.setOnClickListener {
            val intent = Intent(this, FoodActivity::class.java)
            intent.putExtra("type", "breakfast")
            intent.putExtra("date", dateButton?.text)
            startActivity(intent)
        }

        val lunchButton= findViewById<Button>(R.id.lunchButton)
        lunchButton.setOnClickListener {
            val intent = Intent(this, FoodActivity::class.java)
            intent.putExtra("type", "lunch")
            intent.putExtra("date", dateButton?.text)
            startActivity(intent)
        }

        val dinnerButton = findViewById<Button>(R.id.dinnerButton)
        dinnerButton.setOnClickListener {
            val intent = Intent(this, FoodActivity::class.java)
            intent.putExtra("type", "dinner")
            intent.putExtra("date", dateButton?.text)
            startActivity(intent)
        }

        val snackButton = findViewById<Button>(R.id.snackButton)
        snackButton.setOnClickListener {
            val intent = Intent(this, FoodActivity::class.java)
            intent.putExtra("type", "snack")
            intent.putExtra("date", dateButton?.text)
            startActivity(intent)
        }

        val createNewFoodButton = findViewById<TextView>(R.id.createNewFoodButton)
        createNewFoodButton.setOnClickListener {
            val intent = Intent(this, CreateNewFoodActivity::class.java)
            startActivity(intent)
        }

        val menuButton = findViewById<ImageButton>(R.id.menuImageButton)
        menuButton.setOnClickListener{
            val intent = Intent(this, MainMenu::class.java)
            startActivity(intent)
        }
    }

    private fun getTodaysDate(): String? {
        val cal: Calendar = Calendar.getInstance()
        val year: Int = cal.get(Calendar.YEAR)
        var month: Int = cal.get(Calendar.MONTH)
        month += 1
        val day: Int = cal.get(Calendar.DAY_OF_MONTH)
        return makeDateString(day, month, year)
    }

    private fun initDatePicker() {
        val dateSetListener =
            OnDateSetListener { datePicker, year, month, day ->
                var month = month
                month += 1
                val date = makeDateString(day, month, year)
                dateButton!!.text = date
            }
        val cal: Calendar = Calendar.getInstance()
        val year: Int = cal.get(Calendar.YEAR)
        val month: Int = cal.get(Calendar.MONTH)
        val day: Int = cal.get(Calendar.DAY_OF_MONTH)
        val style: Int = AlertDialog.THEME_HOLO_LIGHT
        datePickerDialog = DatePickerDialog(this, style, dateSetListener, year, month, day)
        datePickerDialog!!.getDatePicker().setMaxDate(System.currentTimeMillis());
    }

    private fun makeDateString(day: Int, month: Int, year: Int): String? {
        return "$year-$month-$day"
    }

    fun openDatePicker(view: View?) {
        datePickerDialog!!.show()
    }
}