package jp.toastkid.yobidashi.compact.service

import jp.toastkid.yobidashi.compact.editor.EditorFrame
import java.nio.file.Files
import java.nio.file.Paths

class CommandLineArgumentService {

    operator fun invoke(array: Array<String>) {
        array.mapNotNull { Paths.get(it) }
            .filter { Files.isReadable(it) }
            .forEach {
                val editorFrame = EditorFrame()
                editorFrame.load(it)
                editorFrame.show()
            }
    }

}