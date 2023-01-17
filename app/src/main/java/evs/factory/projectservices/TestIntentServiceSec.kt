package evs.factory.projectservices

import android.app.IntentService
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import kotlinx.coroutines.*

class TestIntentServiceSec:IntentService(INTENT_NAME) {

    override fun onCreate() {
        super.onCreate()
        log("onCreate")
        //Сохраняет интернт сервис при перезапуске
        setIntentRedelivery(true)
//        createNotificationChannel()
//        startForeground(NOTIFICATION_ID, createNotification())

    }

    override fun onHandleIntent(intent: Intent?) {
        log("onHandleIntent")
        val page = intent?.getIntExtra(PAGE, 0)?:0
        for(i in 0 until 5){
            Thread.sleep(1000)
            log("Timer $i $page")
        }
    }
    //С версии API 26 действия в фоне должгы вызывать уведомлпения

    override fun onDestroy() {
        super.onDestroy()
        log("OnDestroy")
    }
    private fun log(message: String){
        Log.d("SERVICE_TAG","TestIntentService:$message")
    }

    private fun createNotificationChannel(){
        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            val notificationChannel = NotificationChannel(
                CHANNEL_ID, CHANNEL_NAME,
                NotificationManager.IMPORTANCE_DEFAULT
            )
            notificationManager.createNotificationChannel(notificationChannel)
        }
    }
    private fun createNotification():Notification{
        //Компатибилити класс
        return NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("Сервис в фоне")
            .setContentText("Заголовок")
            .setSmallIcon(com.google.android.material.R.drawable.mtrl_ic_arrow_drop_down)
            .build()

    }

    companion object {
        private const val CHANNEL_ID ="channel_id"
        private const val CHANNEL_NAME ="channel_name"
        private const val NOTIFICATION_ID = 1
        private const val INTENT_NAME = "PAVEL_GO"
        private const val PAGE = "Page"
        fun newIntent(context: Context, page: Int):Intent{
            return Intent(context, TestIntentServiceSec::class.java).apply {
                putExtra(PAGE, page)
            }
        }
    }
}