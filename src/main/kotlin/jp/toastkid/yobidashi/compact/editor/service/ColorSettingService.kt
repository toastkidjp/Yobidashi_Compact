package jp.toastkid.yobidashi.compact.editor.service

import jp.toastkid.yobidashi.compact.model.Setting
import java.awt.GridBagConstraints
import java.awt.GridBagLayout
import javax.swing.JButton
import javax.swing.JLabel
import javax.swing.JOptionPane
import javax.swing.JPanel

class ColorSettingService {

    operator fun invoke() {
        val content = JPanel()
        content.layout = GridBagLayout()
        val constraints = GridBagConstraints()
        constraints.gridx = 0
        constraints.gridy = 0

        val sample = JLabel(SAMPLE_TEXT)
        val sampleBackground = JPanel().also { it.add(sample) }
        content.add(sampleBackground, constraints)

        constraints.gridy = 1
        val contrastRatioLabel = JLabel()
        content.add(contrastRatioLabel, constraints)

        val contrastRatioCalculatorService = ContrastRatioCalculatorService()
        contrastRatioLabel.text =
                "Contrast ratio: ${contrastRatioCalculatorService(Setting.editorBackgroundColor(), Setting.editorForegroundColor())}"

        constraints.gridx = 1
        constraints.gridy = 0
        val backgroundChooserButton = JButton("Background color").also {
            it.addActionListener {
                val color = ColorChooserService().invoke() ?: return@addActionListener
                Setting.setEditorBackgroundColor(color)
                Setting.save()
                sampleBackground.background = color
                contrastRatioLabel.text =
                        "Contrast ratio: ${contrastRatioCalculatorService(Setting.editorBackgroundColor(), Setting.editorForegroundColor())}"
            }
        }
        content.add(backgroundChooserButton, constraints)

        constraints.gridy = 1
        val button2 = JButton("Font color").also {
            it.addActionListener {
                val color = ColorChooserService().invoke() ?: return@addActionListener
                Setting.setEditorForegroundColor(color)
                Setting.save()
                sampleBackground.foreground = color
                contrastRatioLabel.text =
                        "Contrast ratio: ${contrastRatioCalculatorService(Setting.editorBackgroundColor(), Setting.editorForegroundColor())}"
            }
        }
        content.add(button2, constraints)

        JOptionPane.showConfirmDialog(null, content)
    }

    companion object {

        private const val SAMPLE_TEXT = "あアA1@亜"

    }
}
