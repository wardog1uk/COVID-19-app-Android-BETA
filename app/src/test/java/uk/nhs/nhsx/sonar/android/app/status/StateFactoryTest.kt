/*
 * Copyright © 2020 NHSX. All rights reserved.
 */

package uk.nhs.nhsx.sonar.android.app.status

import org.assertj.core.api.Assertions.assertThat
import org.joda.time.DateTime
import org.joda.time.DateTimeZone.UTC
import org.joda.time.LocalDate
import org.junit.Test

class StateFactoryTest {

    private val today = LocalDate(2020, 4, 10)

    @Test
    fun `when symptoms date is today, red state is valid until 7 days after today`() {
        val state = StateFactory.red(today, setOf(Symptom.COUGH), today)

        assertThat(state.until).isEqualTo(DateTime(2020, 4, 17, 7, 0).toDateTime(UTC))
    }

    @Test
    fun `when symptoms date is yesterday, red state is valid until 6 days after today`() {
        val state = StateFactory.red(today.minusDays(1), setOf(Symptom.COUGH), today)

        assertThat(state.until).isEqualTo(DateTime(2020, 4, 16, 7, 0).toDateTime(UTC))
    }

    @Test
    fun `when symptoms date is 7 days ago, red state is valid until tomorrow`() {
        val state = StateFactory.red(today.minusDays(7), setOf(Symptom.COUGH), today)

        assertThat(state.until).isEqualTo(DateTime(2020, 4, 11, 7, 0).toDateTime(UTC))
    }

    @Test
    fun `when extending the state, red state is valid until tomorrow`() {
        val state = StateFactory.extendedRed(Symptom.COUGH, today = today)

        assertThat(state.until).isEqualTo(DateTime(2020, 4, 11, 7, 0).toDateTime(UTC))
    }

    @Test
    fun `when symptoms date is 8 days ago without temperature, state should be recovery`() {
        val state = StateFactory.decide(today.minusDays(8), setOf(Symptom.COUGH), today)

        assertThat(state).isInstanceOf(RecoveryState::class.java)
    }

    @Test
    fun `when symptoms date is 7 days ago without temperature, state should be recovery`() {
        val state = StateFactory.decide(today.minusDays(7), setOf(Symptom.COUGH), today)

        assertThat(state).isInstanceOf(RecoveryState::class.java)
    }

    @Test
    fun `when symptoms date is 6 days ago with temperature, state should be red`() {
        val state = StateFactory.decide(today.minusDays(6), setOf(Symptom.TEMPERATURE), today)

        assertThat(state).isInstanceOf(RedState::class.java)
    }

    @Test
    fun `ember state is valid until 13 days after  today`() {
        val state = StateFactory.ember(today = today)

        assertThat(state.until).isEqualTo(DateTime(2020, 4, 23, 7, 0).toDateTime(UTC))
    }
}
