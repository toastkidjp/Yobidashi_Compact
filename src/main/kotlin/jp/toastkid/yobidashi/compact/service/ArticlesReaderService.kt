package jp.toastkid.yobidashi.compact.service

import jp.toastkid.yobidashi.compact.model.Setting
import java.nio.file.Files
import java.nio.file.Paths

class ArticlesReaderService {

    operator fun invoke() =
        Files.list(Paths.get(Setting.articleFolder()))

}