package com.example.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.fitness_appka.*
import com.example.ui.data.ListFoodItem
import com.example.ui.data.ListFriend
import io.ktor.client.request.*
import io.ktor.http.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class Friends : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_friends)


        val recyclerView = findViewById<RecyclerView>(R.id.friendsRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        lateinit var data: List<ListFriend>
        runBlocking {
            launch {
                data = client.get("$server_ip/") {
                    contentType(ContentType.Application.Json)
                    header("Authorization", auth_token)
                }
            }
        }

        //recyclerView.adapter = ListFriendAdapter(data, this)

        val go_back_btn = findViewById<Button>(R.id.goBack1_button)
        go_back_btn.setOnClickListener{
            val intent = Intent(this, MainMenu::class.java)
            startActivity(intent)
        }

        val add_friend_btn = findViewById<Button>(R.id.goBack1_button)
        add_friend_btn.setOnClickListener{
            var username_et = findViewById<EditText>(R.id.nameEditText).text.toString()
            //mame username a  poslat poziadavku

        }

        val menuButton = findViewById<ImageButton>(R.id.menuImageButton)
        menuButton.setOnClickListener{
            val intent = Intent(this, MainMenu::class.java)
            startActivity(intent)
        }

    }
}