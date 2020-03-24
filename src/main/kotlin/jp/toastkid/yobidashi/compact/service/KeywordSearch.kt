package jp.toastkid.yobidashi.compact.service

import jp.toastkid.yobidashi.compact.model.Setting
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.util.stream.Collectors

class KeywordSearch {

    operator fun invoke(keyword: String, fileFilter: String?): MutableList<String> {
        return Files.list(Paths.get(Setting.articleFolder()))
                .parallel()
                .map { it.fileName.toString() to Files.readAllLines(it) }
                .filter { filterByKeyword(fileFilter, it, keyword) }
                .map { it.first }
                .collect(Collectors.toList())
    }


    private fun filterByKeyword(fileFilter: String?, it: Pair<String, MutableList<String>>, keyword: String): Boolean {
        if (fileFilter?.isNotBlank() == true && !it.first.contains(fileFilter)) {
            return false
        }
        return it.second.any { line -> line.contains(keyword) }
    }
}