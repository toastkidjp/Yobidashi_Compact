package jp.toastkid.yobidashi.compact.model

class ArticleLengthAggregationResult : AggregationResult {

    private val map: MutableMap<String, Long> = hashMapOf()

    fun put(key: String, value: Long) {
        map.put(key, value)
    }

    fun sum() = map.values.sum()

    override fun header(): Array<Any> =
            arrayOf("Title", "Length")

    override fun itemArrays(): Collection<Array<Any>> =
            map.entries.map { arrayOf(it.key, it.value) }

    override fun resultTitleSuffix(): String {
        return "Total: ${sum()}"
    }

}