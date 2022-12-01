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
        val panel = JPanel()
        panel.layout = BoxLayout(panel, BoxLayout.PAGE_AXIS)
        val unixTimeInput = JTextField()
        unixTimeInput.text = "東京特許 許可局"
        val dateTime = JTextField()
        dateTime.text = URLEncoder.encode(unixTimeInput.text, StandardCharsets.UTF_8.name())

        unixTimeInput.preferredSize = Dimension(100, 24)
        unixTimeInput.addKeyListener(object : KeyAdapter() {
            override fun keyReleased(e: KeyEvent?) {
                dateTime.text = URLEncoder.encode(unixTimeInput.text, StandardCharsets.UTF_8.name())
            }
        })
        panel.add(JLabel("Please would you input some words."))
        panel.add(JLabel("Raw string"))
        panel.add(unixTimeInput)
        panel.add(JLabel("Encoded string"))
        dateTime.preferredSize = Dimension(100, 24)
        dateTime.addKeyListener(object : KeyAdapter() {
            override fun keyReleased(e: KeyEvent?) {
                unixTimeInput.text = URLDecoder.decode(dateTime.text, StandardCharsets.UTF_8.name())
            }
        })
        panel.add(dateTime)
        JOptionPane.showMessageDialog(null, panel)
    }

}