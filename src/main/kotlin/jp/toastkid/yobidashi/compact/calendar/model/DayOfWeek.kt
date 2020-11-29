package jp.toastkid.yobidashi.compact.calendar.model

enum class DayOfWeek {
    SUN, MON, TUE, WED, THU, FRI, SAT;

    companion object {

        fun getName(i: Int): String {
            return values()[i].name
        }

    }
}