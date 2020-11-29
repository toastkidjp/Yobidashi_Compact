package jp.toastkid.yobidashi.compact.calendar.usecase

import jp.toastkid.yobidashi.compact.calendar.model.FixedJapaneseHoliday
import java.awt.Color

class OffDayFinderUseCase {

    operator fun invoke(year: Int, month: Int, date: Int): Color {
        if (month == 6) {
            return Color.BLACK
        }

        return if (FixedJapaneseHoliday.values()
                        .firstOrNull() { month == it.month && date == it.date } != null) {
            Color.RED
        } else Color.BLACK
    }

}