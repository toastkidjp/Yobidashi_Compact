package jp.toastkid.yobidashi.compact.service

import jp.toastkid.yobidashi.compact.model.Setting
import java.nio.file.Files
import java.nio.file.Paths
import java.util.stream.Collectors

class ArticleLengthAggregatorService {

    operator fun invoke(keyword: String): Map<String, Long> {
        return Files.list(Paths.get(Setting.articleFolder()))
                .parallel()
                .map { it.toFile().nameWithoutExtension to Files.readAllBytes(it) }
                .filter { it.first.startsWith(keyword) }
                .map {
                    it.first to String(it.second).trim().codePoints().count()
                }
                .collect(Collectors.toMap(
                        { it.first },
                        { it.second },
                        { key, value -> key },
                        { HashMap<String, Long>() }
                ))
    }

}
