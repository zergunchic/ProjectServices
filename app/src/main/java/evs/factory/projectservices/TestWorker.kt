package evs.factory.projectservices

import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.core.app.JobIntentService
import androidx.work.*

class TestWorker(context: Context, private val workerParameters: WorkerParameters):Worker(context, workerParameters) {
    //Выполняется в другом потоке
    override fun doWork(): Result {
        log("doWork")
        val page = workerParameters.inputData.getInt(PAGE,0)
        for(i in 0 until 5){
            Thread.sleep(1000)
            log("Timer $i $page")
        }
        return Result.success()
    }

    private fun log(message: String){
        Log.d("SERVICE_TAG","NewJobIntentService:$message")
    }
    companion object {
        private const val PAGE = "Page"
        const val WORK_NAME = "worker"

        fun makeRequest(page:Int):OneTimeWorkRequest{
            return OneTimeWorkRequestBuilder<TestWorker>()
                .setInputData(workDataOf(PAGE to page))
                .setConstraints(makeConstraints())
                .build()
        }

        private fun makeConstraints():Constraints{
            return Constraints.Builder()
                .setRequiresCharging(true)
                .build()
        }
    }

}