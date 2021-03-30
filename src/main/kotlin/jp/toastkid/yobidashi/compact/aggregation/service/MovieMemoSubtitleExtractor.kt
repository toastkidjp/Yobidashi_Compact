package jp.toastkid.yobidashi.compact.aggregation.service

import jp.toastkid.yobidashi.compact.aggregation.model.MovieMemoExtractorResult
import jp.toastkid.yobidashi.compact.service.ArticlesReaderService
import java.nio.file.Files
import kotlin.streams.asSequence

class MovieMemoSubtitleExtractor(private val articlesReaderService: ArticlesReaderService = ArticlesReaderService()) {

    operator fun invoke(keyword: String): MovieMemoExtractorResult {
        val result = MovieMemoExtractorResult()
        articlesReaderService.invoke()
                .asSequence()
                .filter { it.fileName.toString().startsWith(keyword) }
                .map {
                    it.fileName.toString() to
                        Files.readAllLines(it)
                                .filter { line -> line.startsWith("##") && line.contains("年、") }
                                .map { line -> line.substring(line.indexOf(" ")) }
                }
                .filter { it.second.isNotEmpty() }
                .forEach {
                   it.second.forEach { line -> result.add(it.first, line) }
                }
        return result
    }
}