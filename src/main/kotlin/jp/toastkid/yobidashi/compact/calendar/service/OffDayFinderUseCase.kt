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
            return Color.BLACK
        }

        if (month == 3 && date == equinoxDayCalculator.calculateVernalEquinoxDay(year)) {
            return Color.RED
        }

        if (month == 9 && date == equinoxDayCalculator.calculateAutumnalEquinoxDay(year)) {
            return Color.RED
        }

        if (moveableHolidayCalculatorService(year, month, date)) {
            return Color.RED
        }

        if (userOffDayService(month, date)) {
            return Color.RED
        }

        return if (FixedJapaneseHoliday.values()
                        .firstOrNull() { month == it.month && date == it.date } != null) {
            Color.RED
        } else Color.BLACK
    }

}