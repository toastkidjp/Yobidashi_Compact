package jp.toastkid.yobidashi.compact.editor.finder

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.launch
import java.awt.Dimension
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

class FinderFrame(private val channel: Channel<FindOrder>) {

    private val frame = JFrame()

    private val content = JPanel()

    init {
        frame.iconImage = ImageIO.read(javaClass.classLoader.getResourceAsStream("images/icon.png"))
        frame.setBounds(400, 300, 400, 180)

        content.layout = BoxLayout(content, BoxLayout.Y_AXIS)
        content.add(JLabel("Target"))
        val target = JTextField()
        target.preferredSize = Dimension(100, 36)
        content.add(target)
        val replace = JTextField()
        content.add(JLabel("Replace"))
        replace.preferredSize = Dimension(100, 36)
        content.add(replace)

        content.add(makeButtons(target, replace))

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
                CoroutineScope(Dispatchers.Default).launch {
                    channel.send(FindOrder(target.text, replace.text, true))
                }
            }
        }

        val downer = JButton()
        downer.margin = Insets(10, 20, 10, 20)
        downer.action = object : AbstractAction("↓") {
            override fun actionPerformed(e: ActionEvent?) {
                CoroutineScope(Dispatchers.Default).launch {
                    channel.send(FindOrder(target.text, replace.text))
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
                    channel.send(FindOrder(target.text, replace.text, invokeReplace = true))
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