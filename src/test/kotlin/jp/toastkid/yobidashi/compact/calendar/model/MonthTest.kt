package jp.toastkid.yobidashi.compact.calendar.model

import org.junit.jupiter.api.Assertions.assertSame
import org.junit.jupiter.api.Test
import java.time.LocalDate

internal class MonthTest {

    @Test
    fun testFrom() {
        assertSame(Month.DEC, Month.from(LocalDate.of(2020, 12, 2)))
    }

    @Test
    fun testFromName() {
        assertSame(Month.JAN, Month.fromName(""))
        assertSame(Month.JAN, Month.fromName("test"))
        assertSame(Month.JAN, Month.fromName("JAN"))
        assertSame(Month.FEB, Month.fromName("FEB"))
        assertSame(Month.JAN, Month.fromName("may"))
    }

}