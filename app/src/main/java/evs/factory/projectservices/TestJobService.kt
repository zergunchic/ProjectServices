package evs.factory.projectservices

import android.app.job.JobParameters
import android.app.job.JobService
import android.content.Context
import android.content.Intent
import android.util.Log
import kotlinx.coroutines.*

class TestJobService:JobService() {

    private val coroutineScope = CoroutineScope(Dispatchers.Main)

    override fun onCreate() {
        super.onCreate()
        log("onCreate")
    }

    override fun onDestroy() {
        super.onDestroy()
        coroutineScope.cancel()
        log("OnDestroy")
    }

    override fun onStartJob(params: JobParameters?): Boolean {
        log("OnStartCommand")
        coroutineScope.launch {
            for(i in 0 until 100){
                delay(1000)
                log("Timer $i")
            }
            //как в методе онСтопДжоб
            jobFinished(params, true)
        }
        //Возвращаемое значение обозначает режим работы тру если работа будет продолжаться, фолс если работа закончена
        return true
    }

    override fun onStopJob(params: JobParameters?): Boolean {
        log("onStopJob")
        //примерно такая же логика
        return true
    }

    private fun log(message: String){
        Log.d("SERVICE_TAG","TestJobService:$message")
    }

    companion object {
        const val JOB_ID = 3
        fun newIntent(context: Context, start:Int):Intent{
            return Intent(context, TestJobService::class.java)
        }
    }
}