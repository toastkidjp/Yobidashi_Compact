package jp.toastkid.yobidashi.compact

import jp.toastkid.yobidashi.compact.editor.EditorFrame
import jp.toastkid.yobidashi.compact.model.Setting
import jp.toastkid.yobidashi.compact.view.MainFrame
import java.nio.file.Files
import java.nio.file.Paths

/**
 * Created by toastkidjp on 2020/03/23.
 * @author toastkidjp
 */
fun main(array: Array<String>?) {
    if (array?.isNotEmpty() == true) {
        openFilesWithEditor(array)
        return
    }
    val frame = MainFrame("Yobidashi Compact")
    frame.show()
    Runtime.getRuntime().addShutdownHook(Thread {
        Setting.save()
    })
}

private fun openFilesWithEditor(array: Array<String>) {
    array.mapNotNull { Paths.get(it) }
        .filter { Files.isReadable(it) }
        .forEach {
            val editorFrame = EditorFrame()
            editorFrame.load(it)
            editorFrame.show()
        }
}