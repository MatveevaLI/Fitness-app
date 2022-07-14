package com.example.fitness_appka

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.ContactsContract
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.gson.JsonObject
import io.ktor.client.features.*
import io.ktor.client.request.*
import io.ktor.http.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.net.HttpURLConnection
import java.net.URL

class Registration : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registration)

        var RegistrationCompleted = true


        val go_back = findViewById<Button>(R.id.back_to_login_button)
        go_back.setOnClickListener{
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        val create_user = findViewById<Button>(R.id.create_button)
        create_user.setOnClickListener {
            val email = findViewById<EditText>(R.id.editTextTextEmailAddress).text.toString()
            val username = findViewById<EditText>(R.id.editTextTextPersonName2).text.toString()
            val password = findViewById<EditText>(R.id.editTextTextPassword2).text.toString()

            var map = mutableMapOf<String,String>() // dict that can be modified
            map["username"] = username
            map["password"] = password
            map["email"] = email

            lateinit var response : JsonObject
            try {
                runBlocking {
                    launch {
                        //POST na URL, content type a body poziadavky
                        response = client.post("$server_ip/auth/users/") {
                            contentType(ContentType.Application.Json)
                            body = map
                        }

                        println(response)

                    }
                }
            }
            catch (e: ClientRequestException){
                Toast.makeText(this, "Incorrect credentials", Toast.LENGTH_SHORT).show()
                RegistrationCompleted = false
            }

            println(RegistrationCompleted)
            if(RegistrationCompleted){
                Toast.makeText(this@Registration,"Your account has been created",Toast.LENGTH_SHORT).show()
                // osetrit pri exceptione lebo zrusi appku
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
            }



        }




    }
}