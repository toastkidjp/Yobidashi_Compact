package jp.toastkid.yobidashi.compact.service

import jp.toastkid.yobidashi.compact.model.Setting
import java.nio.file.Files
import java.nio.file.Paths
import kotlin.streams.asSequence

class MovieMemoSubtitleExtractor {

    private val lineSeparator = System.lineSeparator()

    operator fun invoke() {
        val map = Files.list(Paths.get(Setting.articleFolder()))
                //.parallel()
                .asSequence()
                .filter { it.fileName.toString().startsWith("2020") }
                .map {
                    it.fileName.toString() to
                        Files.readAllLines(it)
                                .filter { line -> line.startsWith("##") && line.contains("年、") }
                }
                .filter { it.second.isNotEmpty() }
                .map {
                    val filtered = it.second
                    val reduced = filtered.reduce { base, item -> "$base$lineSeparator$item" }
                    it.first to reduced
                }
                .toMap()
        map.forEach {
            it.value.split(lineSeparator).forEach { line ->
                println("${it.key}\t${line}")
            }
        }
    }
}

fun main(args: Array<String>) {
    MovieMemoSubtitleExtractor().invoke()
}