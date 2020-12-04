package jp.toastkid.yobidashi.compact.model

interface AggregationResult {

    fun header(): Array<Any>

    fun itemArrays(): Collection<Array<Any>>

    fun columnClass(columnIndex: Int) = when (columnIndex) {
        itemArrays().size - 1 -> Integer::class.java
        else -> String::class.java
    }

}