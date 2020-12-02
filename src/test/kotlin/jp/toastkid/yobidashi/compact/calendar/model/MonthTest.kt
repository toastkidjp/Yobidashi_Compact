package jp.toastkid.yobidashi.compact.calendar.model

import org.junit.jupiter.api.Assertions.assertSame
import org.junit.jupiter.api.Test
import java.util.GregorianCalendar

internal class MonthTest {

    @Test
    fun testFrom() {
        assertSame(Month.DEC, Month.from(GregorianCalendar(2020, 11, 2)))
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