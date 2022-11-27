package jp.toastkid.yobidashi.compact.media

import jp.toastkid.yobidashi.compact.model.Setting
import java.io.IOException
import java.nio.file.Path
import kotlin.io.path.absolutePathString

class MediaPlayerInvoker {

    operator fun invoke(mediaFilePath: Path) {
        try {
            Runtime.getRuntime().exec(
                    arrayOf(
                            Setting.mediaPlayerPath(),
                            mediaFilePath.absolutePathString()
                    )
            )
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

}