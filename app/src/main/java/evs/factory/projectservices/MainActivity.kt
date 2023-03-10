package evs.factory.projectservices

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.app.NotificationCompat
import evs.factory.projectservices.databinding.ActivityMainBinding
import java.util.*

class MainActivity : AppCompatActivity() {

    private val binding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        binding.button.setOnClickListener{
            startService(MyService.newIntent(this, 10))
        }
        binding.button2.setOnClickListener{
            showNotification()
        }
    }
//Любое уведомление должно быть создано в канале
    private fun showNotification(){
    val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
     if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
         val notificationChannel = NotificationChannel(
            CHANNEL_ID, CHANNEL_NAME ,NotificationManager.IMPORTANCE_DEFAULT
        )
         notificationManager.createNotificationChannel(notificationChannel)
    }
    //Компатибилити класс
        val notification = NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("Сервис на работе")
            .setContentText("Hi")
            .setSmallIcon(R.drawable.ic_launcher_background)
            .build()


        notificationManager.notify(99813, notification)
    }

    companion object{
        private const val CHANNEL_ID ="channel_id"
        private const val CHANNEL_NAME ="channel_name"
    }
}