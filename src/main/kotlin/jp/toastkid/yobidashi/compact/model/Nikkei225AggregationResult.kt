package jp.toastkid.yobidashi.compact.model

class Nikkei225AggregationResult : AggregationResult {

    private val map = mutableMapOf<String, String>()

    override fun header(): Array<Any> {
        return arrayOf("Date", "Price")
    }

    override fun itemArrays(): Collection<Array<Any>> {
        return map.map { arrayOf<Any>(it.key, it.value) }
    }

    override fun resultTitleSuffix(): String {
        return " Nikkei 225"
    }

    override fun isEmpty(): Boolean {
        return map.isEmpty()
    }

    fun put(first: String, count: String) {
        map.put(first, count.toString())
    }
}