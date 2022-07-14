package com.developerspace.webrtcsample

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.fitness_appka.MainMenu
import com.example.fitness_appka.R
import com.example.ui.webrtc.Constants
import com.example.ui.webrtc.RTCActivity
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_start.*

class NoMainActivity : AppCompatActivity() {

    val db = Firebase.firestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_start)
        Constants.isIntiatedNow = true
        Constants.isCallEnded = true
        start_meeting.setOnClickListener {
            if (meeting_id.text.toString().trim().isNullOrEmpty())
                meeting_id.error = "Please enter meeting id"
            else {
                db.collection("calls")
                    .document(meeting_id.text.toString())
                    .get()
                    .addOnSuccessListener {
                        if (it["type"]=="OFFER" || it["type"]=="ANSWER" || it["type"]=="END_CALL") {
                            meeting_id.error = "Please enter new meeting ID"
                        } else {
                            val intent = Intent(this@NoMainActivity, RTCActivity::class.java)
                            intent.putExtra("meetingID",meeting_id.text.toString())
                            intent.putExtra("isJoin",false)
                            startActivity(intent)
                        }
                    }
                    .addOnFailureListener {
                        meeting_id.error = "Please enter new meeting ID"
                    }
            }
        }

        join_meeting.setOnClickListener {
            if (meeting_id.text.toString().trim().isNullOrEmpty())
                meeting_id.error = "Please enter meeting id"
            else {
                val intent = Intent(this@NoMainActivity, RTCActivity::class.java)
                intent.putExtra("meetingID",meeting_id.text.toString())
                intent.putExtra("isJoin",true)
                startActivity(intent)
            }
        }

        backMenu_button.setOnClickListener {
            val intent = Intent(this@NoMainActivity, MainMenu::class.java)
            startActivity(intent)
        }

    }
}