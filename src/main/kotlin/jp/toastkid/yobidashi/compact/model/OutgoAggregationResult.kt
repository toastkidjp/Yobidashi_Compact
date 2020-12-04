package jp.toastkid.yobidashi.compact.model

class OutgoAggregationResult(val target: String): AggregationResult {

    private val map: MutableList<Outgo> = mutableListOf()

    fun add(date: String, title: String, value: Int) {
        map.add(Outgo(date, title, value))
    }

    fun isEmpty(): Boolean {
        return map.isEmpty()
    }

    fun sum(): Int {
        return map.map { it.price }.sum()
    }

    private fun detail(): String {
        return map.map { "${it.date} ${it.title}: ${it.price}" }.reduce { base, item -> "$base${LINE_SEPARATOR}$item" }
    }

    override fun itemArrays() = map.map { arrayOf(it.date, it.title, it.price) }

    override fun header(): Array<Any> = arrayOf("Date", "Item", "Price")

    companion object {
        private val LINE_SEPARATOR = System.lineSeparator()
    }
}