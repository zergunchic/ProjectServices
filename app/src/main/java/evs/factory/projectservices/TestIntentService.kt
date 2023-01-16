package evs.factory.projectservices

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

class TestIntentService:Service() {

    private val coroutineScope = CoroutineScope(Dispatchers.Main)

    override fun onCreate() {
        super.onCreate()
        log("onCreate")
        createNotificationChannel()
        startForeground(NOTIFICATION_ID, createNotification())

    }
    //С версии API 26 действия в фоне должгы вызывать уведомлпения
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        log("OnStartCommandForeground")
        coroutineScope.launch {
            for(i in 0 until 100){
                delay(1000)
                log("Timer $i")
            }
            stopSelf()
        }
        //Возвращаемое значение определяет как ведет себя сервис при перезапуске
        //START_STICKY
        //START_NOT_STICKY
        //Выбранное значение перезапустит сервис с параметрами интента
        return START_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()
        coroutineScope.cancel()
        log("OnDestroy")
    }
    override fun onBind(p0: Intent?): IBinder? {
        TODO("Not yet implemented")
    }

    private fun log(message: String){
        Log.d("SERVICE_TAG","ForegroundService:$message")
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
        private const val START_POINT = "start"
        fun newIntent(context: Context):Intent{
            return Intent(context, TestIntentService::class.java)
        }
    }
}