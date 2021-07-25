package jp.toastkid.yobidashi.compact.aggregation.service

import jp.toastkid.yobidashi.compact.aggregation.model.StepsAggregationResult
import jp.toastkid.yobidashi.compact.service.ArticlesReaderService
import java.nio.file.Files
import java.util.regex.Pattern

class StepsAggregatorService(private val articlesReaderService: ArticlesReaderService = ArticlesReaderService()) {

    operator fun invoke(keyword: String): StepsAggregationResult {
        val aggregationResult = StepsAggregationResult()
        articlesReaderService.invoke()
            .parallel()
            .map { it.toFile().nameWithoutExtension to Files.readAllLines(it) }
            .filter { it.first.startsWith(keyword) }
            .forEach {
                it.second.filter { line -> line.contains(TARGET) }
                    .forEach { line ->
                        val matcher = pattern.matcher(line)
                        while (matcher.find()) {
                            aggregationResult.put(
                                it.first,
                                matcher.group(INDEX_STEPS).replace(",", "").toIntOrNull() ?: 0,
                                matcher.group(2).toIntOrNull() ?: 0
                            )
                        }
                    }
            }
        return aggregationResult
    }

    companion object {

        private const val TARGET = "今日の歩数は"

        private const val INDEX_STEPS = 1

        private val pattern = Pattern.compile("歩数は(.+?)、消費カロリーは(.+?)kcal")

    }

}