package jp.toastkid.yobidashi.compact.model

class OutgoAggregationResult {

    private val map: MutableList<Outgo> = mutableListOf()

    fun add(date: String, title: String, value: Int) {
        map.add(Outgo(date, title, value))
    }

    fun isEmpty(): Boolean {
        return map.isEmpty()
    }

    fun makeMessage(): String {
        return "${sum()}$LINE_SEPARATOR${detail()}"
    }

    private fun sum(): Int {
        return map.map { it.price }.sum()
    }

    private fun detail(): String {
        return map.map { "${it.date} ${it.title}: ${it.price}" }.reduce { base, item -> "$base${LINE_SEPARATOR}$item" }
    }

    companion object {
        private val LINE_SEPARATOR = System.lineSeparator()
    }
}