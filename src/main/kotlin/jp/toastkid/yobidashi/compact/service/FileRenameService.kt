package jp.toastkid.yobidashi.compact.service

import java.awt.Dimension
import java.awt.datatransfer.DataFlavor
import java.awt.datatransfer.UnsupportedFlavorException
import java.awt.dnd.DnDConstants
import java.awt.dnd.DropTarget
import java.awt.dnd.DropTargetAdapter
import java.awt.dnd.DropTargetDropEvent
import java.io.File
import java.io.IOException
import java.nio.file.Files
import javax.swing.BoxLayout
import javax.swing.DefaultListModel
import javax.swing.JLabel
import javax.swing.JList
import javax.swing.JOptionPane
import javax.swing.JPanel
import kotlin.io.path.extension

class FileRenameService {

    operator fun invoke() {
        val panel = JPanel()
        panel.layout = BoxLayout(panel, BoxLayout.PAGE_AXIS)
        panel.add(JLabel("Please would you drop any file?"))
        val droppedFileList = JList<File>()
        droppedFileList.preferredSize = Dimension(300, 400)
        panel.add(droppedFileList)
        val defaultListModel = DefaultListModel<File>()
        droppedFileList.model = defaultListModel
        val dropTarget = DropTarget()
        dropTarget.addDropTargetListener(
            object : DropTargetAdapter() {
                override fun drop(dtde: DropTargetDropEvent?) {
                    println(dtde)
                    try {
                        if (dtde!!.isDataFlavorSupported(DataFlavor.javaFileListFlavor)) {
                            dtde!!.acceptDrop(DnDConstants.ACTION_COPY)
                            val transferable = dtde!!.transferable
                            val list = transferable.getTransferData(
                                DataFlavor.javaFileListFlavor
                            ) as List<*>
                            println("list ${list.size}")
                            for (o in list) {
                                if (o is File) {
                                    defaultListModel.addElement(o)
                                }
                            }
                            dtde!!.dropComplete(true)
                            return
                        }
                    } catch (ex: UnsupportedFlavorException) {
                        ex.printStackTrace()
                    } catch (ex: IOException) {
                        ex.printStackTrace()
                    }
                    dtde!!.rejectDrop()
                }
            }
        )
        panel.dropTarget = dropTarget
        panel.add(JLabel("Base file name"))
        val input = JOptionPane.showInputDialog(null, panel)
        if (input.isNullOrBlank() || defaultListModel.isEmpty) {
            return
        }
        defaultListModel.elements().toList().map { it.toPath() }.forEachIndexed { i, p ->
            Files.copy(p, p.resolveSibling("${input}_${i + 1}.${p.extension}"))
        }
    }

}