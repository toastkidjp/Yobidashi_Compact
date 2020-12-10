package jp.toastkid.yobidashi.compact.aggregation.model

data class MovieMemo(
        val date: String,
        val title: String
) {

    fun toArray(): Array<Any> = arrayOf(date, title)

    companion object {
        fun header(): Array<Any> {
            return arrayOf("Date", "Title")
        }
    }
}