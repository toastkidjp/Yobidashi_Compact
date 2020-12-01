package jp.toastkid.yobidashi.compact.calendar.service

import jp.toastkid.yobidashi.compact.calendar.model.EquinoxDayCalculator
import jp.toastkid.yobidashi.compact.calendar.model.FixedJapaneseHoliday
import java.awt.Color

class OffDayFinderUseCase {

    private val equinoxDayCalculator = EquinoxDayCalculator()

    private val userOffDayService = UserOffDayService()

    private val moveableHolidayCalculatorService = MoveableHolidayCalculatorService()

    operator fun invoke(year: Int, month: Int, date: Int): Color {
        if (month == 6) {
            return COLOR_NORMAL_DAY
        }

        if (month == 3 && date == equinoxDayCalculator.calculateVernalEquinoxDay(year)) {
            return COLOR_OFF_DAY
        }

        if (month == 9 && date == equinoxDayCalculator.calculateAutumnalEquinoxDay(year)) {
            return COLOR_OFF_DAY
        }

        if (moveableHolidayCalculatorService(year, month, date)) {
            return COLOR_OFF_DAY
        }

        if (userOffDayService(month, date)) {
            return COLOR_OFF_DAY
        }

        return if (FixedJapaneseHoliday.values()
                        .firstOrNull() { month == it.month && date == it.date } != null) {
            COLOR_OFF_DAY
        } else COLOR_NORMAL_DAY
    }

    companion object {

        private val COLOR_NORMAL_DAY = Color.BLACK

        private val COLOR_OFF_DAY = Color.RED

    }

}