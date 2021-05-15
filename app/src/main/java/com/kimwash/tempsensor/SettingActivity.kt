package com.kimwash.tempsensor

import android.os.Bundle
import android.widget.LinearLayout
import android.widget.Toast
import android.widget.Toolbar
import androidx.appcompat.app.AppCompatActivity

class SettingActivity:AppCompatActivity() {

    private lateinit var changeIntervalButton: LinearLayout


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        super.setContentView(R.layout.activity_setting)
        val settingToolbar = findViewById<androidx.appcompat.widget.Toolbar>(R.id.settingToolbar)
        setSupportActionBar(settingToolbar)
        supportActionBar?.title = "Setting"
        supportActionBar?.subtitle = "Change settings for your thermometer."
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        changeIntervalButton = findViewById(R.id.changeInterval)
        changeIntervalButton.setOnClickListener {
            Toast.makeText(this, "Not prepared yet.", Toast.LENGTH_SHORT).show()
        }

    }
}