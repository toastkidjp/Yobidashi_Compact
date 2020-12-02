package jp.toastkid.yobidashi.compact.calendar.service

import jp.toastkid.yobidashi.compact.calendar.model.MoveableJapaneseHoliday
import java.util.Calendar
import java.util.GregorianCalendar

class MoveableHolidayCalculatorService {

    operator fun invoke(year: Int, month: Int, date: Int): Boolean {
        if (MoveableJapaneseHoliday.isTargetMonth(month).not() && month != 8) {
            return false
        }

        if (year == 2019 && month == 5 && date == 1) {
            return true
        }

        if (year == 2020) {
            if (month == 7) {
                return when (date) {
                    23, 24 -> return true
                    else -> false
                }
            }
            if (month == 8) {
                return date == 10
            }
            if (month == 10) {
                return false
            }
        }

        if (year == 2021) {
            if (month == 7) {
                return when (date) {
                    22, 23 -> return true
                    else -> false
                }
            }
            if (month == 8) {
                return date == 9
            }
            if (month == 10) {
                return false
            }
        }

        if (month == 8 && date == 11) {
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