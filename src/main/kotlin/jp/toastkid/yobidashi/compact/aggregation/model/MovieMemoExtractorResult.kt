package jp.toastkid.yobidashi.compact.aggregation.model

class MovieMemoExtractorResult : AggregationResult {

    private val items = mutableListOf<MovieMemo>()

    override fun header(): Array<Any> {
        return MovieMemo.header()
    }

    override fun itemArrays(): Collection<Array<Any>> {
        return items.map { it.toArray() }
    }

    override fun resultTitleSuffix(): String {
        return "movies ${items.size}"
    }

    override fun isEmpty(): Boolean {
        return items.isEmpty()
    }

    fun add(date: String, title: String) {
        items.add(MovieMemo(date, title))
    }

}