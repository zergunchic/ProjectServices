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
import androidx.core.app.JobIntentService
import androidx.core.app.NotificationCompat
import kotlinx.coroutines.*

class NewJobIntentService:JobIntentService() {

    override fun onCreate() {
        super.onCreate()
        log("onCreate")
    }

    override fun onHandleWork(intent: Intent) {
        log("onHandleIntent")
        val page = intent.getIntExtra(PAGE, 0)?:0
        for(i in 0 until 5){
            Thread.sleep(1000)
            log("Timer $i $page")
        }
    }
    override fun onDestroy() {
        super.onDestroy()
        log("OnDestroy")
    }

    private fun log(message: String){
        Log.d("SERVICE_TAG","NewJobIntentService:$message")
    }

    companion object {
        private const val PAGE = "Page"
        private val JOB_ID = 123
        fun enqueue(context: Context, page: Int){
            JobIntentService.enqueueWork(context, NewJobIntentService::class.java, JOB_ID,
            newIntent(context, page)
            )
        }

        private fun newIntent(context: Context, page: Int):Intent{
            return Intent(context, NewJobIntentService::class.java).apply {
                putExtra(PAGE, page)
            }
        }
    }
}