package com.example.busdriver

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.graphics.BitmapFactory
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import androidx.core.app.NotificationCompat
import com.example.busdriver.classFile.MyMqtt
import org.eclipse.paho.android.service.MqttAndroidClient
import org.eclipse.paho.client.mqttv3.MqttClient
import org.eclipse.paho.client.mqttv3.MqttMessage

class MainActivity : AppCompatActivity() {
    lateinit var mqttClient: MyMqtt

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mqttClient = MyMqtt(applicationContext, "tcp://15.164.46.54:1883")

        try {
            mqttClient.setCallback(::onReceived)
            mqttClient.connect(arrayOf<String>("eyeson/#"))
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun publish(data: String) {
        //mqttClient 의 publish기능의의 메소드를 호출
        mqttClient.publish("mydata/function", data)
    }

    fun onReceived(topic: String, message: MqttMessage) {
        val msg = String(message.payload)
        var resId = findViewById<ImageView>()
        if(msg.equals("riding")){
            resId.setImageResource(R.drawable.disabled_person1)

        }
    }
}