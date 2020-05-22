package uk.nhs.nhsx.sonar.android.app.status

import android.content.Context
import android.content.ContextWrapper
import android.view.View
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.isEnabled
import androidx.test.espresso.matcher.ViewMatchers.withId
import org.hamcrest.CoreMatchers.not
import org.hamcrest.Matcher
import uk.nhs.nhsx.sonar.android.app.R
import uk.nhs.nhsx.sonar.android.app.testhelpers.checkViewHasText
import uk.nhs.nhsx.sonar.android.app.testhelpers.waitForText

class OkRobot(context: Context) : ContextWrapper(context) {

    fun checkActivityIsDisplayed() {
        waitForText(R.string.status_initial_title, 6_000)
        onView(withId(R.id.status_initial_title)).check(matches(isDisplayed()))
    }

    fun checkFinalisingSetup() {
        checkViewHasText(R.id.registrationStatusText, R.string.registration_finalising_setup)
        verifyCheckMySymptomsButton(not(isEnabled()))
    }

    fun checkEverythingIsWorking() {
        // job retries after at least 10 seconds
        waitForText(R.string.registration_everything_is_working_ok, timeoutInMs = 20000)
        verifyCheckMySymptomsButton(isEnabled())
    }

    private fun verifyCheckMySymptomsButton(matcher: Matcher<View>) {
        onView(withId(R.id.status_not_feeling_well)).check(matches(matcher))
    }

    fun clickReadCurrentAdvice() {
        onView(withId(R.id.read_current_advice)).perform(click())
    }
}