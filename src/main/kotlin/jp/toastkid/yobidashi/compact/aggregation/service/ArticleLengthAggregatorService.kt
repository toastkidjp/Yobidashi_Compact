package jp.toastkid.yobidashi.compact.aggregation.service

import jp.toastkid.yobidashi.compact.aggregation.model.ArticleLengthAggregationResult
import jp.toastkid.yobidashi.compact.service.ArticlesReaderService
import java.nio.file.Files

class ArticleLengthAggregatorService(
    private val articlesReaderService: ArticlesReaderService = ArticlesReaderService()
) {

    operator fun invoke(keyword: String): ArticleLengthAggregationResult {
        val result = ArticleLengthAggregationResult()

        articlesReaderService.invoke()
                .parallel()
                .map { it.toFile().nameWithoutExtension to Files.readAllBytes(it) }
                .filter { it.first.startsWith(keyword) }
                .forEach {
                    result.put(it.first, String(it.second).trim().codePoints().count())
                }

        return result
    }

}
