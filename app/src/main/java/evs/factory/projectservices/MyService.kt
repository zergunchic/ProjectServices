package evs.factory.projectservices

import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.IBinder
import android.util.Log
import kotlinx.coroutines.*

class MyService:Service() {

    private val coroutineScope = CoroutineScope(Dispatchers.Main)

    override fun onCreate() {
        super.onCreate()
        log("onCreate")
    }
    //С версии API 26 действия в фоне должгы вызывать уведомлпения
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        log("OnStartCommand")
        val start = intent?.getIntExtra(START_POINT, 0)?:0
        coroutineScope.launch {
            for(i in start until start + 100){
                delay(1000)
                log("Timer $i")
            }
        }
        //Возвращаемое значение определяет как ведет себя сервис при перезапуске
        //START_STICKY
        //START_NOT_STICKY
        //Выбранное значение перезапустит сервис с параметрами интента
        return START_REDELIVER_INTENT
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
        Log.d("SERVICE_TAG","MyService:$message")
    }

    companion object {

        private const val START_POINT = "start"
        fun newIntent(context: Context, start:Int):Intent{
            return Intent(context, MyService::class.java).apply { putExtra(START_POINT, start) }
        }
    }
}