package jp.toastkid.yobidashi.compact.editor.service

import jp.toastkid.yobidashi.compact.model.Article
import java.nio.file.Files

class ArticleContentLoaderService(private val lineSeparator: String = System.lineSeparator()) {

    operator fun invoke(article: Article): String {
        return Files.readAllLines(article.path()).reduce { base, item -> "$base$lineSeparator$item" } + lineSeparator
    }

}