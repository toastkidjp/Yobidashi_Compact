package jp.toastkid.yobidashi.compact.service

import jp.toastkid.yobidashi.compact.model.ArticleLengthAggregationResult
import jp.toastkid.yobidashi.compact.model.Setting
import java.nio.file.Files
import java.nio.file.Paths

class ArticleLengthAggregatorService {

    operator fun invoke(keyword: String): ArticleLengthAggregationResult {
        val result = ArticleLengthAggregationResult()

        Files.list(Paths.get(Setting.articleFolder()))
                .parallel()
                .map { it.toFile().nameWithoutExtension to Files.readAllBytes(it) }
                .filter { it.first.startsWith(keyword) }
                .forEach {
                    result.put(it.first, String(it.second).trim().codePoints().count())
                }

        return result
    }

}
