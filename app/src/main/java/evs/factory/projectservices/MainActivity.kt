package evs.factory.projectservices

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.job.JobInfo
import android.app.job.JobScheduler
import android.app.job.JobWorkItem
import android.content.ComponentName
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import evs.factory.projectservices.databinding.ActivityMainBinding
import java.util.*

class MainActivity : AppCompatActivity() {
    private var page = 0
    private val binding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        binding.button.setOnClickListener{
            stopService(TestForegroundService.newIntent(this))
            startService(MyService.newIntent(this, 10))
        }
//        binding.button2.setOnClickListener{
//            showNotification()
//        }

        binding.button2.setOnClickListener{
            ContextCompat.startForegroundService(this,
                TestForegroundService.newIntent(this))
        }

        binding.button3.setOnClickListener{
            ContextCompat.startForegroundService(this,
            TestIntentService.newIntent(this))
        }

        binding.button4.setOnClickListener{
            val componentName = ComponentName(this, TestJobService::class.java)
            val jobInfo = JobInfo.Builder(TestJobService.JOB_ID, componentName)
//                .setExtras(TestJobService.newBundle(page++))
                .setRequiresCharging(true)
                .setRequiredNetworkType(JobInfo.NETWORK_TYPE_UNMETERED)
                //.setPersisted(true)
                //.setPeriodic()
                .build()
            val jobScheduler = getSystemService(JOB_SCHEDULER_SERVICE) as JobScheduler

            val intent = TestJobService.newIntent(page++)
            jobScheduler.enqueue(jobInfo, JobWorkItem(intent))
            //переписывает существующий джоб
            //jobScheduler.schedule(jobInfo)
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