/*
 * Copyright © 2020 NHSX. All rights reserved.
 */

package uk.nhs.nhsx.sonar.android.app.diagnose

import android.content.Context
import androidx.work.BackoffPolicy
import androidx.work.Constraints
import androidx.work.CoroutineWorker
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import org.joda.time.LocalDate
import timber.log.Timber
import uk.nhs.nhsx.sonar.android.app.appComponent
import uk.nhs.nhsx.sonar.android.app.status.Symptom
import java.util.concurrent.TimeUnit

class SubmitContactEventsWorker(appContext: Context, private val params: WorkerParameters) :
    CoroutineWorker(appContext, params) {

    private val work by lazy { appComponent.submitContactEventsWork() }

    override suspend fun doWork(): Result {
        Timber.d("Started uploading contact events... ")
        return work.doWork(params.inputData)
    }

    companion object {
        fun schedule(context: Context, symptomsDate: LocalDate, symptoms: List<Symptom>) {
            val constraints = Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .build()

            val request =
                OneTimeWorkRequestBuilder<SubmitContactEventsWorker>()
                    .setConstraints(constraints)
                    .setInputData(SubmitContactEventsWork.data(symptomsDate, symptoms))
                    .setBackoffCriteria(BackoffPolicy.EXPONENTIAL, 1, TimeUnit.MINUTES)
                    .build()

            WorkManager.getInstance(context).enqueue(request)
        }
    }
}
