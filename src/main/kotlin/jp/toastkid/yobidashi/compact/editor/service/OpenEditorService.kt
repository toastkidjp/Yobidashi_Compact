package jp.toastkid.yobidashi.compact.editor.service

import jp.toastkid.yobidashi.compact.editor.EditorFrame
import jp.toastkid.yobidashi.compact.model.Article
import jp.toastkid.yobidashi.compact.model.Setting
import java.awt.Desktop
import java.io.IOException

class OpenEditorService(
        private val editorFrame: EditorFrame = EditorFrame(),
        private val desktop: Desktop = Desktop.getDesktop()
) {

    operator fun invoke(article: Article) {
        try {
            if (Setting.useInternalEditor()) {
                editorFrame.load(article)
                editorFrame.show()
                return
            }

            desktop.open(article.path().toFile())
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

}