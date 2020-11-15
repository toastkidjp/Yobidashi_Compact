package jp.toastkid.yobidashi.compact.editor

import jp.toastkid.yobidashi.compact.model.Article
import java.io.IOException

class OpenEditorUseCase(private val editorFrame: EditorFrame = EditorFrame()) {

    operator fun invoke(article: Article) {
        try {
            editorFrame.load(article)
            editorFrame.show()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

}