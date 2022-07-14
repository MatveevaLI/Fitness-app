package com.example.fitness_appka

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Window
import android.view.WindowManager
import android.widget.Button
import com.developerspace.webrtcsample.NoMainActivity
import com.example.ui.NutritionActivity
import com.example.ui.ProfileUserActivity
import com.example.ui.ProgressActivity

class MainMenu : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN)

        setContentView(R.layout.activity_main_menu)

        val logoutBtn = findViewById<Button>(R.id.logout_button)
        logoutBtn.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        val nutritionBtn = findViewById<Button>(R.id.nutrition_button)

        nutritionBtn.setOnClickListener {
            /*Toast.makeText(this, "Button clicked", Toast.LENGTH_SHORT).show()*/
            val intent = Intent(this, NutritionActivity::class.java)
            startActivity(intent)
        }

        val profile_btn = findViewById<Button>(R.id.profile_button)
        profile_btn.setOnClickListener {
            val intent = Intent(this, ProfileUserActivity::class.java)
            startActivity(intent)
        }

        val progressButton = findViewById<Button>(R.id.progressButton)
        progressButton.setOnClickListener {
            val intent = Intent(this, ProgressActivity::class.java)
            startActivity(intent)
        }

        val callButton = findViewById<Button>(R.id.callButton)
        callButton.setOnClickListener {
            val intent = Intent(this, NoMainActivity::class.java)
            startActivity(intent)
        }
    }
}