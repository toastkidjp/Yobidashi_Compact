package jp.toastkid.yobidashi.compact.editor.service

import jp.toastkid.yobidashi.compact.editor.MenuCommand
import jp.toastkid.yobidashi.compact.model.Setting
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.launch
import java.awt.GridBagConstraints
import java.awt.GridBagLayout
import javax.swing.JButton
import javax.swing.JComponent
import javax.swing.JLabel
import javax.swing.JOptionPane
import javax.swing.JPanel
import javax.swing.JTextField

class ColorSettingService(private val channel: Channel<MenuCommand>) {

    private val sample = JTextField(SAMPLE_TEXT)

    private val contrastRatioCalculatorService = ContrastRatioCalculatorService()

    private val contrastRatioLabel = JLabel()

    operator fun invoke() {
        val content = JPanel()
        content.layout = GridBagLayout()
        val constraints = GridBagConstraints()
        constraints.gridx = 0
        constraints.gridy = 0

        sample.isEditable = false
        content.add(sample, constraints)

        constraints.gridy = 1
        content.add(contrastRatioLabel, constraints)

        applyColorSetting()

        constraints.gridx = 1
        constraints.gridy = 0
        val backgroundChooserButton = JButton("Background color").also {
            it.addActionListener {
                val color = ColorChooserService().invoke() ?: return@addActionListener
                Setting.setEditorBackgroundColor(color)
                Setting.save()
                applyColorSetting()
            }
        }
        content.add(backgroundChooserButton, constraints)

        constraints.gridy = 1
        val button2 = makeFontColorButton(sample, contrastRatioLabel, contrastRatioCalculatorService)
        content.add(button2, constraints)

        constraints.gridx = 1
        constraints.gridy = 2
        val button = JButton("Reset color setting").also {
            it.addActionListener {
                Setting.resetEditorColorSetting()
                applyColorSetting()
            }
        }
        content.add(button, constraints)

        JOptionPane.showConfirmDialog(null, content)
    }

    private fun applyColorSetting() {
        sample.foreground = Setting.editorForegroundColor()
        sample.background = Setting.editorBackgroundColor()
        contrastRatioLabel.text =
                "Contrast ratio: ${contrastRatioCalculatorService(Setting.editorBackgroundColor(), Setting.editorForegroundColor())}"
        CoroutineScope(Dispatchers.Default).launch {
            channel.send(MenuCommand.REFRESH)
        }
    }

    private fun makeFontColorButton(
            sample: JComponent,
            contrastRatioLabel: JLabel,
            contrastRatioCalculatorService: ContrastRatioCalculatorService
    ): JComponent {
        return JButton("Font color").also {
            it.addActionListener {
                val color = ColorChooserService().invoke() ?: return@addActionListener
                Setting.setEditorForegroundColor(color)
                Setting.save()
                sample.foreground = color
                contrastRatioLabel.text =
                        "Contrast ratio: ${contrastRatioCalculatorService(Setting.editorBackgroundColor(), Setting.editorForegroundColor())}"
            }
        }
    }

    companion object {

        private const val SAMPLE_TEXT = "あアA1@亜"

    }
}
