package jp.toastkid.yobidashi.compact.calendar.service

import jp.toastkid.yobidashi.compact.model.Setting

class UserOffDayService(private val userOffDays: List<Pair<Int, Int>> = Setting.userOffDay()) {

    private val targetMonths = userOffDays.map { it.first }.distinct()

    operator fun invoke(month: Int, day: Int) =
            contains(month) && userOffDays.firstOrNull { it.first == month && it.second == day } != null

    private fun contains(month: Int): Boolean = targetMonths.contains(month)

}