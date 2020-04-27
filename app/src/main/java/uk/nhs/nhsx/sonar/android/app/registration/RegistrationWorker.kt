package uk.nhs.nhsx.sonar.android.app.registration

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import uk.nhs.nhsx.sonar.android.app.appComponent
import javax.inject.Inject

class RegistrationWorker(appContext: Context, workerParams: WorkerParameters) :
    CoroutineWorker(appContext, workerParams) {

    @Inject
    lateinit var registrationUseCase: RegistrationUseCase

    override suspend fun doWork(): Result {
        appComponent.inject(this)
        return RegistrationWork(registrationUseCase).doWork()
    }

    companion object {
        const val WAITING_FOR_ACTIVATION_CODE = "WAITING_FOR_ACTIVATION_CODE"
    }
}
