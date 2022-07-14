package com.example.ui

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Base64
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView

import android.widget.Toast
import com.example.fitness_appka.*
import com.google.gson.JsonObject
import com.google.gson.JsonPrimitive
import com.squareup.picasso.Picasso
// import com.squareup.picasso.Picasso
import io.ktor.client.features.*
import io.ktor.client.request.*
import io.ktor.http.*
import kotlinx.android.synthetic.main.activity_profile_user.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.io.ByteArrayOutputStream


class ProfileUserActivity : AppCompatActivity() {

    private lateinit var img_user: ImageView

    companion object{
        val IMAGE_REQUEST_CODE = 100
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile_user)
        fillDataIntoET()

        img_user = findViewById(R.id.imageView)

        val addPhoto_btn = findViewById<Button>(R.id.addPhoto_button)
        addPhoto_btn.setOnClickListener {
            pickImageGallery()
            fillDataIntoET()
        }

        val save_btn = findViewById<Button>(R.id.save_button)
        save_btn.setOnClickListener {

            var map = getDataFromET()

            lateinit var response : JsonPrimitive
            var requestSuccessful = true
            try {
                runBlocking {
                    launch {
                        response = client.put("$server_ip/users/me/") {
                            contentType(ContentType.Application.Json)
                            header("Authorization", auth_token)
                            body = map
                        }
                    }
                }
            }
            catch (e: ClientRequestException){
                Toast.makeText(this@ProfileUserActivity, "Wrong format", Toast.LENGTH_SHORT).show()
                requestSuccessful = false
            }

            if(requestSuccessful){
                Toast.makeText(this, "Data successfully changed", Toast.LENGTH_SHORT).show()
                val intent = Intent(this, MainMenu::class.java)
                startActivity(intent)
            }
        }

        val goBack_btn = findViewById<Button>(R.id.goBack_button)
        goBack_btn.setOnClickListener {
            val intent = Intent(this, MainMenu::class.java)
            startActivity(intent)
        }



        val deleteAccount_btn = findViewById<Button>(R.id.delete_acc_button)
        deleteAccount_btn.setOnClickListener {
            var map = mutableMapOf<String,String>() // dict that can be modified
            map["password"] = "Hesielko1"   // toto tu dorobit lepsie

            lateinit var response : JsonObject
            runBlocking {
                launch {    //samotatny thread
                    response = client.delete("$server_ip/users/me/") {
                        contentType(ContentType.Application.Json)
                        header("Authorization", auth_token)
                        body = map
                    }
                    Toast.makeText(applicationContext,response.toString(), Toast.LENGTH_SHORT).show()
                }
            }
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }

    private fun pickImageGallery() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, IMAGE_REQUEST_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == IMAGE_REQUEST_CODE && resultCode == RESULT_OK){
            imageView.setImageURI(data?.data)

            var bitmap = (imageView.drawable as BitmapDrawable).bitmap
            var final_string = encodeImage(bitmap)

            var map = mutableMapOf<String, String>()

            //request na ziskanie "name"
            lateinit var response1: JsonObject
            runBlocking {
                launch {
                    response1 = client.get("$server_ip/users/me/") {
                        contentType(ContentType.Application.Json)
                        header("Authorization", auth_token)
                    }
                }
            }
            map["name"] = response1["name"].toString().replace("\"","")
            map["image"] = final_string.toString()





            lateinit var response: JsonPrimitive
            runBlocking {
                launch {
                    response = client.put("$server_ip/users/me/") {
                        contentType(ContentType.Application.Json)
                        header("Authorization", auth_token)
                        body = map
                    }
                }
            }
        }
    }

    private fun fillDataIntoET(){
        // tu dam GET a ziskam vsetky info
        lateinit var response : JsonObject
        runBlocking {
            launch {
                response = client.get("$server_ip/users/me/") {
                    contentType(ContentType.Application.Json)
                    header("Authorization", auth_token)
                }
                findViewById<EditText>(R.id.nameEditText).setText(response["name"].toString().replace("\"", ""))
                findViewById<EditText>(R.id.genderEditText).setText(response["gender"].toString().replace("\"", ""))
                findViewById<EditText>(R.id.weightEditText).setText(response["weight"].toString())
                findViewById<EditText>(R.id.heightEditText).setText(response["height"].toString())
                findViewById<EditText>(R.id.bdayEditText).setText(response["birthday"].toString().replace("\"", ""))
                findViewById<EditText>(R.id.mailEditText).setText(response["email"].toString().replace("\"", ""))

                var user_picture = findViewById<ImageView>(R.id.imageView)
                var picture_path = response["photo"].toString().replace("\"", "")
                var total_pic_path = server_ip + "/images/" + picture_path  // URL to load into

                println(total_pic_path)
                Picasso.get().load(total_pic_path).into(user_picture)



                // "https://media.istockphoto.com/photos/cobberdog-pup-on-white-background-picture-id1306641965"
            }
        }
    }

    private fun getDataFromET(): MutableMap<String, String> {
        var name_et = findViewById<EditText>(R.id.nameEditText).text.toString()
        var gender_et = findViewById<EditText>(R.id.genderEditText).text.toString()
        var weight_et = findViewById<EditText>(R.id.weightEditText).text.toString().toFloat()
        var height_et = findViewById<EditText>(R.id.heightEditText).text.toString().toInt()
        var bday_et = findViewById<EditText>(R.id.bdayEditText).text.toString()
        var mail_et = findViewById<EditText>(R.id.mailEditText).text.toString()

        var map = mutableMapOf<String, String>()
        map["name"] = name_et
        map["gender"] = gender_et
        map["weight"] = weight_et.toString()
        map["height"] = height_et.toString()
        map["birthday"] = bday_et
        map["email"] = mail_et

        return map






    }

    private fun encodeImage(bm: Bitmap): String? {
        val baos = ByteArrayOutputStream()
        bm.compress(Bitmap.CompressFormat.JPEG, 100, baos)
        val b = baos.toByteArray()
        return Base64.encodeToString(b, Base64.DEFAULT)
    }




}