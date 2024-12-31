package jp.toastkid.yobidashi.compact.model

import jp.toastkid.yobidashi.compact.calendar.service.OffDayFinderService
import java.time.DayOfWeek
import java.time.LocalDate

class ArticleTemplate {
    operator fun invoke(header: String) = """# $header
"""

    private fun isNotOffDay(): Boolean {
        val now = LocalDate.now()
        val dayOfWeek = now.dayOfWeek
        return dayOfWeek != DayOfWeek.SATURDAY && dayOfWeek != DayOfWeek.SUNDAY
                && OffDayFinderService().invoke(now.year, now.monthValue, now.dayOfMonth, dayOfWeek.value).not()
    }

    private fun isStockDay(): Boolean {
        val now = LocalDate.now()
        val dayOfWeek = now.dayOfWeek
        return dayOfWeek != DayOfWeek.MONDAY && dayOfWeek != DayOfWeek.SUNDAY
                && OffDayFinderService().invoke(now.year, now.monthValue, now.dayOfMonth, dayOfWeek.value).not()
    }

}
