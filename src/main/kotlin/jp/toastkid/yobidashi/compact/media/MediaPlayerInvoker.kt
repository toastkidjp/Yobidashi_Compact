package jp.toastkid.yobidashi.compact.media

import jp.toastkid.yobidashi.compact.model.Setting
import java.nio.file.Path
import kotlin.io.path.absolutePathString

class MediaPlayerInvoker {

    operator fun invoke(mediaFilePath: Path) {
        Runtime.getRuntime().exec(
                arrayOf(
                        Setting.mediaPlayerPath(),
                        mediaFilePath.absolutePathString()
                )
        )
    }

}