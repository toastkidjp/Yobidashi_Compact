package jp.toastkid.yobidashi.compact.aggregation.service

import jp.toastkid.yobidashi.compact.aggregation.model.Nikkei225AggregationResult
import jp.toastkid.yobidashi.compact.service.ArticlesReaderService
import java.nio.file.Files

class Nikkei225AggregatorService(private val articlesReaderService: ArticlesReaderService = ArticlesReaderService()) {

    operator fun invoke(keyword: String): Nikkei225AggregationResult {
        val result = Nikkei225AggregationResult()

        articlesReaderService.invoke()
                .parallel()
                .map { it.toFile().nameWithoutExtension to Files.readAllLines(it) }
                .filter { it.first.startsWith(keyword) }
                .forEach {
                    extract(result, it)
                }

        return result
    }

    private fun extract(result: Nikkei225AggregationResult, pair: Pair<String, MutableList<String>>) {
        var next = false
        pair.second.forEach { line ->
            if (line.endsWith(TARGET_SUFFIX)) {
                next = true
                return@forEach
            }
            if (next) {
                val split = line.split("円(")
                val target: String = split.get(0)
                if (target.isNotEmpty()) {
                    result.put(pair.first, target, split.get(1).let { it.substring(0, it.length - 1) })
                    return@forEach
                }
                return
            }
        }
    }

    companion object {

        private const val TARGET_SUFFIX = "今日の日経平均株価終値"

    }
}
