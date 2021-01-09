package jp.toastkid.yobidashi.compact.model

import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

internal class ArticleTemplateTest {

    private lateinit var articleTemplate: ArticleTemplate

    @BeforeEach
    fun setUp() {
        articleTemplate = ArticleTemplate()
    }

    @Test
    fun test() {
        val content = articleTemplate.invoke("test")
        assertTrue(content.startsWith("# test"))
    }

}