package jp.toastkid.yobidashi.compact.model

class OutgoAggregationResult {

    private val map: MutableMap<String, Int> = mutableMapOf()

    fun put(key: String, value: Int) {
        map.put(key, value)
    }

    fun isEmpty(): Boolean {
        return map.isEmpty()
    }

    fun makeMessage(): String {
        return "${sum()}$LINE_SEPARATOR${detail()}"
    }

    private fun sum(): Int {
        return map.values.sum()
    }

    private fun detail(): String {
        return map.entries.map { "${it.key}: ${it.value}" }.reduce { base, item -> "$base${LINE_SEPARATOR}$item" }
    }

    companion object {
        private val LINE_SEPARATOR = System.lineSeparator()
    }
}