package com.kimwash.tempsensor

import android.os.Bundle
import android.util.Log
import android.widget.*
import androidx.appcompat.app.AppCompatActivity

class SettingActivity : AppCompatActivity() {

    private lateinit var applyButton: Button
    private lateinit var intervalTextView: TextView
    private lateinit var hostTextView: TextView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        super.setContentView(R.layout.activity_setting)
        val settingToolbar = findViewById<androidx.appcompat.widget.Toolbar>(R.id.settingToolbar)
        setSupportActionBar(settingToolbar)
        supportActionBar?.title = "Setting"
        supportActionBar?.subtitle = "Change settings for your thermometer."
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        applyButton = findViewById(R.id.applySetting)
        intervalTextView = findViewById(R.id.intervalText)
        hostTextView = findViewById(R.id.hostText)

        intervalTextView.text = PreferenceManager.getInt(this, "interval").toString()
        hostTextView.text = PreferenceManager.getString(this, "host")

        applyButton.setOnClickListener {
            PreferenceManager.setInt(this, "interval", intervalTextView.text.toString().toInt())
            PreferenceManager.setString(this, "host", hostTextView.text.toString())
            Toast.makeText(this, "Setting saved.", Toast.LENGTH_SHORT).show()
            finish()
        }

    }
}