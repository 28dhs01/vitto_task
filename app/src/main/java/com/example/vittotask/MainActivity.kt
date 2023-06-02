package com.example.vittotask

import android.content.ContentValues.TAG
import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.firestore.FirebaseFirestore

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val btn = findViewById<Button>(R.id.button)
        val tv = findViewById<TextView>(R.id.tv)
        val userIpt = findViewById<TextView>(R.id.userInput)
        // set on-click listener
        btn.setOnClickListener {
            saveMsgOnFirestore(userIpt.text.toString())
        }
        readMsgFromFirestore()
    }
    private fun saveMsgOnFirestore(msg: String){
        val db = FirebaseFirestore.getInstance()
        val note : MutableMap<String,String> =  HashMap()
        note["noteTxt"] = msg
        db.collection("notes")
            .add(note)
            .addOnSuccessListener {
                Toast.makeText(this,"message added",Toast.LENGTH_LONG).show()
            }
            .addOnFailureListener{
                Toast.makeText(this,"message failed to be added",Toast.LENGTH_LONG).show()
            }
        readMsgFromFirestore()
    }
    private fun readMsgFromFirestore(){

        val tv = findViewById<TextView>(R.id.tv)

        val db = FirebaseFirestore.getInstance()

        db.collection("notes")
            .get()
            .addOnSuccessListener { result ->
                val list: MutableList<String> = mutableListOf()

                for (document in result) {
//                    Log.d(TAG, "${document.id} => ${document.data}")
//                    tv.text = document.data.toString()
                    list.add(document.data.toString())
                }
                println(list)
//                val x = result.size()
//                tv.text = x.toString()
                val random = (0 until list.size).random()
                tv.text = list[random]
            }
            .addOnFailureListener { exception ->
                Log.w(TAG, "Error getting documents.", exception)
            }
    }
}