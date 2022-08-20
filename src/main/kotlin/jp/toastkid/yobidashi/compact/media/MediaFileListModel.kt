package jp.toastkid.yobidashi.compact.media

import jp.toastkid.yobidashi.compact.model.Sorting
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.nio.file.Path
import javax.swing.ListModel
import javax.swing.event.ListDataListener
import kotlin.io.path.getLastModifiedTime
import kotlin.io.path.nameWithoutExtension

class MediaFileListModel : ListModel<Path> {

    private val items = mutableListOf<Path>()

    private val master = mutableListOf<Path>()

    override fun getElementAt(index: Int): Path {
        return items[index]
    }

    override fun getSize(): Int {
        return items.size
    }

    override fun addListDataListener(l: ListDataListener?) = Unit

    override fun removeListDataListener(l: ListDataListener?) = Unit

    fun filter(text: String?) {
        if (text.isNullOrBlank()) {
            return
        }
        items.clear()
        master.filter { it.nameWithoutExtension.contains(text) }.forEach { items.add(it) }
    }

    fun add(article: Path) {
        master.add(article)
        items.add(article)
        items.sortBy { it.nameWithoutExtension }
    }

    fun addAll(articles: Collection<Path>, onComplete: () -> Unit) {
        CoroutineScope(Dispatchers.IO).launch {
            val sorted = articles.sortedByDescending { it.getLastModifiedTime() }
            master.addAll(sorted)
            items.addAll(sorted.take(100))
            onComplete()
        }
    }

    fun sortBy(sorting: Sorting) {
        items.sortByDescending {
            when (sorting) {
                Sorting.LAST_MODIFIED -> it.getLastModifiedTime().toString()
                else -> it.nameWithoutExtension
            }
        }
    }

    fun all() = ArrayList<Path>().also { it.addAll(items) }
}