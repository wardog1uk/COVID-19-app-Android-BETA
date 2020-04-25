/*
 * Copyright © 2020 NHSX. All rights reserved.
 */

package uk.nhs.nhsx.sonar.android.app.notifications

import android.content.Context
import android.content.Intent
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.Test
import uk.nhs.nhsx.sonar.android.app.appComponent
import uk.nhs.nhsx.sonar.android.app.di.ApplicationComponent

class ReminderBroadcastReceiverTest {

    private val manager = mockk<ReminderManager>(relaxed = true)
    private val context = mockk<Context>(relaxed = true)
    private val intent = mockk<Intent>()
    private val applicationComponent = mockk<ApplicationComponent>(relaxed = true)
    private val sut = ReminderBroadcastReceiver().apply {
        this.reminderManager = manager
    }

    @Test
    fun onReceive() {
        every { context.appComponent } returns applicationComponent

        sut.onReceive(context, intent)

        verify(exactly = 1) { manager.handleReminderBroadcast(intent) }
    }
}