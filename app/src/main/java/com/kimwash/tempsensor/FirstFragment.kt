package com.kimwash.tempsensor

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.os.PowerManager
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.TextView
import android.widget.Toast
import com.kimwash.tempsensor.networking.RetrofitService
import com.kimwash.tempsensor.networking.SensorData
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import org.json.JSONObject
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.*

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class FirstFragment : Fragment(), SensorEventListener {

    private val TAG = "FirstFragment"
    private lateinit var tempTextView: TextView
    private lateinit var humidityTextView: TextView
    private lateinit var lightTextView: TextView

    private lateinit var sensorManager: SensorManager
    private lateinit var tempSensor: Sensor
    private lateinit var humiditySensor: Sensor
    private lateinit var lightSensor: Sensor

    private var time = Calendar.getInstance().timeInMillis
    private lateinit var host: String
    private var interval:Int = 555

    var temperature: Float = 555F
    var humidity: Float = 555F
    var light: Float = 555F

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_first, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        interval = PreferenceManager.getInt(requireContext(), "interval")
        host = PreferenceManager.getString(requireContext(), "host")!!
        if (PreferenceManager.getString(requireContext(), "host") == null || PreferenceManager.getInt(requireContext(), "interval") == -1){

            Toast.makeText(requireContext(), "No host or interval found. going to setting.", Toast.LENGTH_SHORT).show()
            val intent = Intent(requireContext(), SettingActivity::class.java)
            startActivity(intent)
            requireActivity().finish()
        }

        tempTextView = view.findViewById(R.id.tempTView)
        humidityTextView = view.findViewById(R.id.humidityTView)
        lightTextView = view.findViewById(R.id.lightTView)

        sensorManager = context?.getSystemService(Context.SENSOR_SERVICE) as SensorManager
        tempSensor = sensorManager.getDefaultSensor(Sensor.TYPE_AMBIENT_TEMPERATURE)
        humiditySensor = sensorManager.getDefaultSensor(Sensor.TYPE_RELATIVE_HUMIDITY)
        lightSensor = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT)

        sensorManager.registerListener(this, tempSensor, SensorManager.SENSOR_DELAY_UI)
        sensorManager.registerListener(this, humiditySensor, SensorManager.SENSOR_DELAY_UI)
        sensorManager.registerListener(this, lightSensor, SensorManager.SENSOR_DELAY_UI)

    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
        when (accuracy) {
            SensorManager.SENSOR_STATUS_ACCURACY_LOW -> {
                Toast.makeText(context, "Warning: Accuracy is Low!", Toast.LENGTH_SHORT).show()
            }
        }
    }

    @SuppressLint("CheckResult")
    override fun onSensorChanged(event: SensorEvent?) {
        if (!this::host.isInitialized || interval == 555) {
            return
        }
        when (event!!.sensor.type) {
            Sensor.TYPE_RELATIVE_HUMIDITY -> {
                humidity = event.values[0]
            }
            Sensor.TYPE_AMBIENT_TEMPERATURE -> {
                temperature = event.values[0]
            }
            Sensor.TYPE_LIGHT -> {
                light = event.values[0]
            }
        }
        val currTime = Calendar.getInstance().timeInMillis
        if (time + interval < currTime && temperature != 555F) {
            time = currTime

            val service:RetrofitService = Retrofit.Builder()
                .baseUrl(host)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build()
                .create(RetrofitService::class.java)
            val resp = service.recordData(SensorData(temperature, humidity, light, Calendar.getInstance().time))
            resp.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    Log.d(TAG, it.success.toString())
                }, {
                    Log.d(TAG, it.message.toString())
                })



        }
        tempTextView.text = String.format("%.1f", temperature)
        humidityTextView.text = String.format("%.1f", humidity)
        lightTextView.text = String.format("%.1f", light)
    }
}