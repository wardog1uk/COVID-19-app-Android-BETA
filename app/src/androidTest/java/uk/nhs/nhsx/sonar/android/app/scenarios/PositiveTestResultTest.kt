package uk.nhs.nhsx.sonar.android.app.scenarios

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.rule.ActivityTestRule
import org.joda.time.LocalDate
import org.joda.time.LocalTime
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import uk.nhs.nhsx.sonar.android.app.EspressoTest
import uk.nhs.nhsx.sonar.android.app.FlowTestStartActivity
import uk.nhs.nhsx.sonar.android.app.R
import uk.nhs.nhsx.sonar.android.app.inbox.TestInfo
import uk.nhs.nhsx.sonar.android.app.inbox.TestResult
import uk.nhs.nhsx.sonar.android.app.status.PositiveState
import uk.nhs.nhsx.sonar.android.app.status.UserState
import uk.nhs.nhsx.sonar.android.app.testhelpers.TestData
import uk.nhs.nhsx.sonar.android.app.testhelpers.robots.BottomDialogRobot
import uk.nhs.nhsx.sonar.android.app.testhelpers.robots.StatusRobot
import kotlin.reflect.KClass

class PositiveTestResultTest : EspressoTest() {

    private val statusRobot = StatusRobot()
    private val bottomDialogRobot = BottomDialogRobot()
    private val testData = TestData()

    @get:Rule
    val activityRule: ActivityTestRule<FlowTestStartActivity> =
        ActivityTestRule(FlowTestStartActivity::class.java)

    @Before
    fun setupFlowTestActivity() {
        testAppContext.app.startTestActivity<FlowTestStartActivity>()
    }

    @Test
    fun tookTestWhileInNeutral() {
        startAppWith(testData.defaultState)

        receivePositiveTestResult(testData.today)

        dismissPositiveTestResult()
        verifyStatusIs(PositiveState::class)
    }

    @Test
    fun tookTestWhileInSymptomatic() {
        val state = testData.symptomaticYesterday()

        startAppWith(state)

        receivePositiveTestResult(testData.today)

        dismissPositiveTestResult()
        verifyStatusIs(PositiveState::class)
    }

    @Test
    fun tookTestWhileInExposed() {
        val state = testData.exposedYesterday()

        startAppWith(state)

        receivePositiveTestResult(testData.today)

        dismissPositiveTestResult()
        verifyStatusIs(PositiveState::class)
    }

    @Test
    fun tookTestWhileInPositive() {
        val state = testData.positiveToday()

        startAppWith(state)

        receivePositiveTestResult(testData.today)

        dismissPositiveTestResult()
        verifyStatusIs(PositiveState::class)
    }

    @Test
    fun tookTestWhileInExposedSymptomaticWithinExposureWindow() {
        val state = testData.exposedSymptomaticYesterday()

        startAppWith(state)

        receivePositiveTestResult(testData.today)

        dismissPositiveTestResult()
        verifyStatusIs(PositiveState::class)
    }

    private fun startMainActivity() {
        onView(withId(R.id.start_main_activity)).perform(click())
    }

    private fun receivePositiveTestResult(testDate: LocalDate) {
        testAppContext.apply {
            simulateTestResultNotificationReceived(
                TestInfo(
                    result = TestResult.POSITIVE,
                    date = testDate.toDateTime(LocalTime.now())
                )
            )
            clickOnNotification(
                R.string.test_result_notification_title,
                R.string.test_result_notification_text
            )
        }
    }

    private fun <T : UserState> verifyStatusIs(userState: KClass<T>) {
        statusRobot.checkActivityIsDisplayed(userState)
    }

    private fun dismissPositiveTestResult() {
        bottomDialogRobot.checkTestResultDialogIsDisplayed(TestResult.POSITIVE)
        bottomDialogRobot.clickSecondCtaButton()
    }

    private fun startAppWith(state: UserState) {
        testAppContext.setFullValidUser(state)
        startMainActivity()
    }
}
