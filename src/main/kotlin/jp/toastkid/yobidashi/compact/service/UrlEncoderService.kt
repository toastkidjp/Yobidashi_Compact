package jp.toastkid.yobidashi.compact.service

import java.awt.Dimension
import java.awt.event.KeyAdapter
import java.awt.event.KeyEvent
import java.net.URLDecoder
import java.net.URLEncoder
import java.nio.charset.StandardCharsets
import javax.swing.BoxLayout
import javax.swing.JLabel
import javax.swing.JOptionPane
import javax.swing.JPanel
import javax.swing.JTextField

class UrlEncoderService {

    operator fun invoke() {
        val rawWordInput = JTextField()
        rawWordInput.text = "東京特許 許可局"
        val encodedInput = JTextField()
        encodedInput.text = URLEncoder.encode(rawWordInput.text, StandardCharsets.UTF_8.name())

        rawWordInput.preferredSize = Dimension(100, 24)
        rawWordInput.addKeyListener(object : KeyAdapter() {
            override fun keyReleased(e: KeyEvent?) {
                encodedInput.text = URLEncoder.encode(rawWordInput.text, StandardCharsets.UTF_8.name())
            }
        })
        encodedInput.preferredSize = Dimension(100, 24)
        encodedInput.addKeyListener(object : KeyAdapter() {
            override fun keyReleased(e: KeyEvent?) {
                rawWordInput.text = URLDecoder.decode(encodedInput.text, StandardCharsets.UTF_8.name())
            }
        })
        val panel = JPanel()
        panel.layout = BoxLayout(panel, BoxLayout.PAGE_AXIS)
        panel.add(JLabel("Please would you input some words."))
        panel.add(JLabel("Raw string"))
        panel.add(rawWordInput)
        panel.add(JLabel("Encoded string"))
        panel.add(encodedInput)
        JOptionPane.showMessageDialog(null, panel)
    }

}