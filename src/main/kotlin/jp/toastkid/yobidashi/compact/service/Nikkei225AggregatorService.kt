package jp.toastkid.yobidashi.compact.service

import jp.toastkid.yobidashi.compact.model.Nikkei225AggregationResult
import jp.toastkid.yobidashi.compact.model.Setting
import java.nio.file.Files
import java.nio.file.Paths

class Nikkei225AggregatorService {

    operator fun invoke(keyword: String): Nikkei225AggregationResult {
        val result = Nikkei225AggregationResult()

        Files.list(Paths.get(Setting.articleFolder()))
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
            if (line.endsWith("今日の日経平均株価終値")) {
                next = true
                return@forEach
            }
            if (next) {
                val target: String = line.split("円").get(0)
                if (target.isNotEmpty()) {
                    result.put(pair.first, target)
                    return@forEach
                }
                return
            }
        }
    }

}
