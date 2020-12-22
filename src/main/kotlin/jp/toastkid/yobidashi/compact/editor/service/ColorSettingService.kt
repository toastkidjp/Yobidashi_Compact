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

        val sample = JLabel("あアA1@亜")
        content.add(sample, constraints)

        constraints.gridx = 1
        val backgroundChooserButton = JButton("Background color").also {
            it.addActionListener {
                val color = ColorChooserService().invoke() ?: return@addActionListener
                Setting.setEditorBackgroundColor(color)
                sample.background = color
            }
        }
        content.add(backgroundChooserButton, constraints)

        JOptionPane.showConfirmDialog(
                null,
                content
        )
    }

}