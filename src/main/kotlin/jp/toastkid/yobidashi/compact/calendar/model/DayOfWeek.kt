package jp.toastkid.yobidashi.compact.calendar.model

enum class DayOfWeek {
    SUN, MON, TUE, WED, THU, FRI, SAT;

    companion object {

        fun getName(i: Int): String {
            if (i < 0 || i >= values().size) {
                return ""
            }
            return values()[i].name
        }

    }
}