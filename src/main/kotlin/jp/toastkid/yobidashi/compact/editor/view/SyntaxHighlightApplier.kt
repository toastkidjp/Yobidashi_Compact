package jp.toastkid.yobidashi.compact.editor.view

import org.fife.ui.rsyntaxtextarea.AbstractTokenMakerFactory
import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea
import org.fife.ui.rsyntaxtextarea.Style
import org.fife.ui.rsyntaxtextarea.Token
import org.fife.ui.rsyntaxtextarea.TokenMakerFactory
import java.awt.Color
import java.awt.Font

class SyntaxHighlightApplier {

    operator fun invoke(editorArea: RSyntaxTextArea) {
        (TokenMakerFactory.getDefaultInstance() as? AbstractTokenMakerFactory)
            ?.putMapping(customStyle, MarkdownTokenMaker::class.java.canonicalName)
        editorArea.syntaxEditingStyle = customStyle
        val boldFont = editorArea.font.deriveFont(Font.BOLD)
        val syntaxScheme = editorArea.syntaxScheme
        syntaxScheme.setStyle(
            Token.COMMENT_EOL,
            Style(Color(0, 128, 0), null, boldFont)
        )
        syntaxScheme.setStyle(
            Token.LITERAL_NUMBER_HEXADECIMAL,
            Style(Color(128, 0, 220), null, boldFont)
        )
    }

}

private val customStyle = "text/plain2"