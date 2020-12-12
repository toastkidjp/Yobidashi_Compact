package jp.toastkid.yobidashi.compact.editor.finder

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.swing.Swing
import java.awt.Dimension
import java.awt.GridBagConstraints
import java.awt.GridBagLayout
import java.awt.Insets
import java.awt.event.ActionEvent
import javax.imageio.ImageIO
import javax.swing.AbstractAction
import javax.swing.BoxLayout
import javax.swing.JButton
import javax.swing.JComponent
import javax.swing.JFrame
import javax.swing.JLabel
import javax.swing.JPanel
import javax.swing.JTextField

class FinderAreaView(
        private val orderChannel: Channel<FindOrder>,
        private val messageChannel: Channel<String>
) {

    private val frame = JFrame()

    private val content = JPanel()

    init {
        frame.iconImage = ImageIO.read(javaClass.classLoader.getResourceAsStream("images/icon.png"))
        frame.setBounds(400, 300, 400, 180)

        content.layout = GridBagLayout()
        val constraints = GridBagConstraints()

        constraints.gridx = 0
        constraints.gridy = 0
        constraints.gridwidth = 1
        content.add(JLabel("Target"), constraints)

        val target = JTextField()
        target.preferredSize = Dimension(200, 36)
        constraints.gridx = 1
        constraints.gridy = 0
        constraints.gridwidth = 2
        content.add(target, constraints)

        constraints.gridx = 0
        constraints.gridy = 1
        constraints.gridwidth = 1
        content.add(JLabel("Replace"), constraints)

        val replace = JTextField()
        replace.preferredSize = Dimension(200, 36)
        constraints.gridx = 1
        constraints.gridy = 1
        constraints.gridwidth = 2
        content.add(replace, constraints)

        constraints.gridx = 3
        constraints.gridy = 0
        content.add(makeButtons(target, replace), constraints)

        val message = JLabel()
        message.preferredSize = Dimension(200, 36)
        message.font = message.font.deriveFont(14f)
        CoroutineScope(Dispatchers.Swing).launch {
            messageChannel.receiveAsFlow().collect {
                message.text = it
            }
        }
        constraints.gridx = 4
        constraints.gridy = 0
        content.add(message)

        frame.add(content)
    }

    private fun makeButtons(target: JTextField, replace: JTextField): JPanel {
        val buttons = JPanel()
        buttons.layout = BoxLayout(buttons, BoxLayout.X_AXIS)
        val upper = JButton()
        upper.preferredSize = Dimension(100, 36)
        upper.margin = Insets(10, 20, 10, 20)
        upper.action = object : AbstractAction("↑") {
            override fun actionPerformed(e: ActionEvent?) {
                if (target.text.isNullOrEmpty()) {
                    return
                }
                CoroutineScope(Dispatchers.Default).launch {
                    orderChannel.send(FindOrder(target.text, replace.text, true))
                }
            }
        }

        val downer = JButton()
        downer.margin = Insets(10, 20, 10, 20)
        downer.action = object : AbstractAction("↓") {
            override fun actionPerformed(e: ActionEvent?) {
                if (target.text.isNullOrEmpty()) {
                    return
                }
                CoroutineScope(Dispatchers.Default).launch {
                    orderChannel.send(FindOrder(target.text, replace.text))
                }
            }
        }

        val all = JButton()
        all.margin = Insets(10, 20, 10, 20)
        all.action = object : AbstractAction("All") {
            override fun actionPerformed(e: ActionEvent?) {
                if (target.text == replace.text) {
                    return
                }

                CoroutineScope(Dispatchers.Default).launch {
                    orderChannel.send(FindOrder(target.text, replace.text, invokeReplace = true))
                }
            }
        }

        buttons.add(upper)
        buttons.add(downer)
        buttons.add(all)
        return buttons
    }

    fun show() {
        frame.isVisible = true
    }

    fun close() {
        frame.isVisible = false
        frame.dispose()
    }

    fun view(): JComponent = content

}