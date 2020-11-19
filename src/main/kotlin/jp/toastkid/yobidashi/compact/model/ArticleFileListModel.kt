package jp.toastkid.yobidashi.compact.model

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.swing.ListModel
import javax.swing.event.ListDataListener

class ArticleFileListModel : ListModel<Article> {

    private val items = mutableListOf<Article>()

    private val master = mutableListOf<Article>()

    override fun getElementAt(index: Int): Article {
        return items.get(index)
    }

    override fun getSize(): Int {
        return items.size
    }

    override fun addListDataListener(l: ListDataListener?) = Unit

    override fun removeListDataListener(l: ListDataListener?) = Unit

    fun filter(text: String?) {
        items.clear()
        if (text.isNullOrBlank()) {
            items.addAll(master)
            return
        }
        master.filter { it.getTitle().contains(text) }.forEach { items.add(it) }
    }

    fun add(article: Article) {
        master.add(article)
        items.add(article)
        items.sortBy { it.getTitle() }
    }

    fun addAll(articles: Collection<Article>, onComplete: () -> Unit) {
        CoroutineScope(Dispatchers.IO).launch {
            val sorted = articles.sortedByDescending { it.lastModified() }
            master.addAll(sorted)
            items.addAll(sorted.take(100))
            onComplete()
        }
    }

    fun sortBy(sorting: Sorting) {
        items.sortByDescending {
            when (sorting) {
                Sorting.LAST_MODIFIED -> it.lastModified().toString()
                else -> it.getTitle()
            }
        }
    }

    fun all() = ArrayList<Article>().also { it.addAll(items) }
}