package jp.toastkid.yobidashi.compact.service

import jp.toastkid.yobidashi.compact.model.Setting
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.util.stream.Stream

class ArticlesReaderService {

    operator fun invoke(): Stream<Path> =
        Files.list(Paths.get(Setting.articleFolder()))

}