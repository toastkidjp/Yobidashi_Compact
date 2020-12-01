package jp.toastkid.yobidashi.compact.calendar.service

import jp.toastkid.yobidashi.compact.calendar.model.MoveableJapaneseHoliday
import java.util.Calendar
import java.util.GregorianCalendar

class MoveableHolidayCalculatorService {

    operator fun invoke(year: Int, month: Int, date: Int): Boolean {
        if (MoveableJapaneseHoliday.isTargetMonth(month).not()) {
            return false
        }

        if (year == 2019 && month == 5 && date == 1) {
            return true
        }

        val calendar = GregorianCalendar(year, month - 1, 1)
        val targetDay = MoveableJapaneseHoliday.find(month) ?: return false
        val dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK)
        val d = if (dayOfWeek == Calendar.MONDAY) {
            1
        } else {
            8 - (dayOfWeek - 2)
        }

        return date == d + (7 * (targetDay.week - 1))
    }

}