package com.example.fitness_appka

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowManager
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import android.widget.Toolbar
import com.example.ui.NutritionActivity
import com.google.gson.JsonObject
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.features.*
import io.ktor.client.features.json.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

var server_ip: String = "http://" + "147.175.160.171" + ":8000"
/*var server_ip: String = "http://" + "192.168.31.116" + ":8000"*/
var auth_token: String = "Token"


val client = HttpClient(CIO) {
    install(JsonFeature) {
        serializer = GsonSerializer()
        acceptContentTypes += ContentType("application", "json")
    }
}


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getWindow().setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main)

        lateinit var toolbar: Toolbar

        var username_et = findViewById(R.id.username_et) as EditText
        var password_et = findViewById(R.id.password_et) as EditText
        val login_btn = findViewById(R.id.login_button) as Button
        val reg_btn = findViewById(R.id.register_button) as Button


        login_btn.setOnClickListener {
            val username = username_et.text
            val password = password_et.text

            var map = mutableMapOf<String, String>()
            map["username"] = username.toString()
            map["password"] = password.toString()
            lateinit var response: JsonObject

            try {
                runBlocking {
                    launch {
                        response = client.post("$server_ip/auth/token/login") {
                            contentType(ContentType.Application.Json)
                            body = map
                        }
                    }
                }
                var token = response.get("auth_token").asString
                auth_token = "$auth_token $token"
                val intent = Intent(this, MainMenu::class.java)
                startActivity(intent)
            }
            catch (e: ClientRequestException){
                Toast.makeText(this, "Incorrect credentials", Toast.LENGTH_SHORT).show()
            }

        }

        reg_btn.setOnClickListener {
            val intent = Intent(this, Registration::class.java)
            startActivity(intent)
        }
    }
}