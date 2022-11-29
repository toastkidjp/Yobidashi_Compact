package jp.toastkid.yobidashi.compact.service

import java.awt.Dimension
import java.awt.event.KeyAdapter
import java.awt.event.KeyEvent
import java.time.DateTimeException
import java.time.Instant
import java.time.LocalDateTime
import java.time.OffsetDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import javax.swing.BoxLayout
import javax.swing.JLabel
import javax.swing.JOptionPane
import javax.swing.JPanel
import javax.swing.JTextField

class UnixTimeConverterService {

    operator fun invoke() {
        val panel = JPanel()
        panel.layout = BoxLayout(panel, BoxLayout.PAGE_AXIS)
        val fileFilter = JTextField()
        val offset = OffsetDateTime.now().offset
        fileFilter.text = LocalDateTime.now().toInstant(offset).toEpochMilli().toString()
        val dateTime = JTextField()
        val dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
        dateTime.text = LocalDateTime.now().format(dateFormatter).toString()

        fileFilter.preferredSize = Dimension(100, 24)
        fileFilter.addKeyListener(object : KeyAdapter() {
            override fun keyReleased(e: KeyEvent?) {
                // unixtime -> datetime
                dateTime.text = LocalDateTime
                    .ofInstant(Instant.ofEpochMilli(fileFilter.text.toLong()), ZoneId.systemDefault())
                    .format(dateFormatter)
            }
        })
        panel.add(JLabel("Please would you input UNIX TIME."))
        panel.add(JLabel("UNIX TIME"))
        panel.add(fileFilter)
        panel.add(JLabel("Date time"))
        dateTime.preferredSize = Dimension(100, 24)
        dateTime.addKeyListener(object : KeyAdapter() {
            override fun keyReleased(e: KeyEvent?) {
                // datetime -> unixtime
                try {
                    fileFilter.text = LocalDateTime.parse(dateTime.text, dateFormatter)
                        .toInstant(offset)
                        .toEpochMilli()
                        .toString()
                } catch (e: DateTimeException) {
                    // > /dev/null
                }
            }
        })
        panel.add(dateTime)
        JOptionPane.showMessageDialog(null, panel)
    }

}