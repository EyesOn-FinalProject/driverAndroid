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
import android.widget.TextView
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
            mqttClient.connect(arrayOf<String>("eyeson/bus"))
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun publish(data: String) {
        //mqttClient 의 publish 기능의 메소드를 호출
        mqttClient.publish("mydata/function", data)
    }

    fun onReceived(topic: String, message: MqttMessage) {
        val msg = String(message.payload)
        val msgList = msg.split("/")
        val resId = findViewById<ImageView>(R.id.imageIcon)
        val tvId = findViewById<TextView>(R.id.textView)
        print(msg)
        if(msgList[0].equals("reservation")){
            print(2)
            resId.setImageResource(R.drawable.disabled_person1)
            tvId.setText("해당 정류장 : \n" + msgList[1]) // 버스 정류장은 "어디"이다.
        }
        else if(msgList[0].equals("complete")){
            resId.setImageResource(R.drawable.disabled_person2)
            tvId.setText("이번 정거장에서 \n 하차합니다.")
//            tvId.text = "해당 정류장 : " + msgList[1]
        }
        else{
            resId.setImageResource(R.drawable.disabled_person2) // 이미지 바꿔야 함
            tvId.setText("감사합니다.")
        }
    }
}