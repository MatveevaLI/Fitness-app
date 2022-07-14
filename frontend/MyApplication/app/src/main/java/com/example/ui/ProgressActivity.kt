package com.example.ui

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.WindowManager
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
import java.util.*

class ProgressActivity : AppCompatActivity() {

    private var dateFromPickerDialog: DatePickerDialog? = null
    private var dateFromButton: Button? = null

    private var dateToPickerDialog: DatePickerDialog? = null
    private var dateToButton: Button? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_progress)

        initDateFromPicker()
        initDateToPicker()

        dateFromButton = findViewById(R.id.dateFromPickerButton)
        dateFromButton?.text = getTodaysDate()

        dateToButton = findViewById(R.id.dateToPickerButton)
        dateToButton?.text = getTodaysDate()

        val showProgressButton = findViewById<Button>(R.id.showProgressButton)
        showProgressButton.setOnClickListener {

            val intent = Intent(this, ProgressResultActivity::class.java)
            intent.putExtra("date_from", dateFromButton?.text.toString())
            intent.putExtra("date_to", dateToButton?.text.toString())
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

    private fun initDateFromPicker() {
        val dateSetListener1 =
            DatePickerDialog.OnDateSetListener { datePicker, year, month, day ->
                var month = month
                month += 1
                val date = makeDateString(day, month, year)
                dateFromButton!!.text = date
            }
        val cal: Calendar = Calendar.getInstance()
        val year: Int = cal.get(Calendar.YEAR)
        val month: Int = cal.get(Calendar.MONTH)
        val day: Int = cal.get(Calendar.DAY_OF_MONTH)
        val style: Int = AlertDialog.THEME_HOLO_LIGHT
        dateFromPickerDialog = DatePickerDialog(this, style, dateSetListener1, year, month, day)
        dateFromPickerDialog!!.datePicker.maxDate = System.currentTimeMillis();
    }

    private fun initDateToPicker() {
        val dateSetListener =
            DatePickerDialog.OnDateSetListener { datePicker, year, month, day ->
                var month = month
                month += 1
                val date = makeDateString(day, month, year)
                dateToButton!!.text = date
            }
        val cal: Calendar = Calendar.getInstance()
        val year: Int = cal.get(Calendar.YEAR)
        val month: Int = cal.get(Calendar.MONTH)
        val day: Int = cal.get(Calendar.DAY_OF_MONTH)
        val style: Int = AlertDialog.THEME_HOLO_LIGHT
        dateToPickerDialog = DatePickerDialog(this, style, dateSetListener, year, month, day)
        dateToPickerDialog!!.getDatePicker().setMaxDate(System.currentTimeMillis());
    }

    private fun makeDateString(day: Int, month: Int, year: Int): String? {
        return "$year-$month-$day"
    }

    fun openDateToPicker(view: View?) {
        dateToPickerDialog!!.show()
    }

    fun openDateFromPicker(view: View?) {
        dateFromPickerDialog!!.show()
    }
}