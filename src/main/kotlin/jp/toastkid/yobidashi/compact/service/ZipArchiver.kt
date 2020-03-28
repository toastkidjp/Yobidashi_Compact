package jp.toastkid.yobidashi.compact.service

import java.io.BufferedInputStream
import java.io.BufferedOutputStream
import java.io.IOException
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.util.zip.ZipEntry
import java.util.zip.ZipOutputStream

class ZipArchiver {

    operator fun invoke(paths: Collection<Path>) {
        var zos: ZipOutputStream? = null
        try {
            zos = ZipOutputStream(BufferedOutputStream(Files.newOutputStream(Paths.get(DESTINATION))))
            createZip(zos, paths)
        } catch (e: IOException) {
            e.printStackTrace()
        } finally {
            zos?.close()
        }
    }

    @Throws(IOException::class)
    private fun createZip(zos: ZipOutputStream, paths: Collection<Path>) {
        val buffer = ByteArray(1024)
        paths.forEach { path ->
            val entry = ZipEntry(path.fileName.toString())
            entry.time = Files.getLastModifiedTime(path).toMillis()
            zos.putNextEntry(entry)
            BufferedInputStream(Files.newInputStream(path)).use { stream ->
                var len = 0
                while (stream.read(buffer).also { len = it } != -1) {
                    zos.write(buffer, 0, len)
                }
            }
        }
    }

    companion object {
        private const val DESTINATION = "articles.zip"
    }
}