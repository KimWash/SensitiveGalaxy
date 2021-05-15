package com.kimwash.tempsensor

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.kimwash.tempsensor.networking.Post
import org.json.JSONObject
import java.util.*
import kotlin.math.pow

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class FirstFragment : Fragment(), SensorEventListener {

    private val TAG = "FirstFragment"
    private lateinit var tempTextView: TextView
    private lateinit var humidityTextView: TextView

    private lateinit var sensorManager: SensorManager
    private lateinit var tempSensor: Sensor
    private lateinit var humiditySensor: Sensor

    private var isFirstToSend = 1
    private var time = Calendar.getInstance().timeInMillis
    private val interval = 20000

    var temperature:Float = 0F
    var humidity:Float = 0F

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_first, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        tempTextView = view.findViewById(R.id.tempTView)
        humidityTextView = view.findViewById(R.id.humidityTView)

        sensorManager = context?.getSystemService(Context.SENSOR_SERVICE) as SensorManager
        tempSensor = sensorManager.getDefaultSensor(Sensor.TYPE_AMBIENT_TEMPERATURE)
        humiditySensor = sensorManager.getDefaultSensor(Sensor.TYPE_RELATIVE_HUMIDITY)

        sensorManager.registerListener(this, tempSensor, SensorManager.SENSOR_DELAY_UI)
        sensorManager.registerListener(this, humiditySensor, SensorManager.SENSOR_DELAY_UI)

    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
        when (accuracy){
            SensorManager.SENSOR_STATUS_ACCURACY_LOW -> {
                Toast.makeText(context, "Warning: Accuracy is Low!", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onSensorChanged(event: SensorEvent?) {
        when (event!!.sensor.type){
            Sensor.TYPE_RELATIVE_HUMIDITY -> {
                humidity = event.values[0]
            }
            Sensor.TYPE_AMBIENT_TEMPERATURE -> {
                temperature = event.values[0]
            }
        }
        val currTime = Calendar.getInstance().timeInMillis
        if (time + interval < currTime || isFirstToSend == 1){
            isFirstToSend = 0
            time = currTime
            val json = JSONObject()
            json.put("temperature", temperature)
            json.put("humidity", humidity)
            Log.d(TAG, Post("http://192.168.1.253:8000/api/record", json).execute().get().toString())
        }
        tempTextView.text = String.format("%.1f", temperature)
        humidityTextView.text = String.format("%.1f", humidity)
    }
}