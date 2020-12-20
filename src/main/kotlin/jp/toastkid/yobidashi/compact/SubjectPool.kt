package jp.toastkid.yobidashi.compact

import jp.toastkid.yobidashi.compact.model.Article
import jp.toastkid.yobidashi.compact.model.Sorting
import jp.toastkid.yobidashi.compact.view.ArticleListView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.swing.Swing
import javax.swing.JComponent

object SubjectPool {

    private val pool: Channel<Pair<ArticleListView, String>> = Channel()

    fun next(component: ArticleListView, title: String) {
        CoroutineScope(Dispatchers.Default).launch { pool.send(component to title) }
    }

    fun observeNewSearchResult(send: (ArticleListView, String) -> Unit) {
        CoroutineScope(Dispatchers.Swing).launch {
            pool.receiveAsFlow().collect {
                send(it.first, it.second)
            }
        }
    }

    private val sort: Channel<Sorting> = Channel()

    fun nextSorting(next: Sorting) {
        CoroutineScope(Dispatchers.Default).launch { sort.send(next) }
    }

    fun observeSort(send: (Sorting) -> Unit) {
        CoroutineScope(Dispatchers.Swing).launch {
            sort.receiveAsFlow().collect { send(it) }
        }
    }

    private val closeWindow: Channel<Unit> = Channel()

    fun closeWindow() = CoroutineScope(Dispatchers.Default).launch { closeWindow.send(Unit) }

    fun observeCloseWindow(send: () -> Unit) = CoroutineScope(Dispatchers.Swing).launch {
        closeWindow.receiveAsFlow().collect {
            send()
        }
    }

    private val addToList: Channel<Article> = Channel()

    fun addToList(article: Article) = CoroutineScope(Dispatchers.Default).launch { addToList.send(article) }

    fun observeAddToList(send: (Article) -> Unit) = CoroutineScope(Dispatchers.Swing).launch {
        addToList.receiveAsFlow().collect {
            send(it)
        }
    }

    private val refreshUi: Channel<JComponent> = Channel()

    fun refreshUi(component: JComponent) {
        CoroutineScope(Dispatchers.Default).launch { refreshUi.send(component) }
    }

    fun observeRefreshUi(observer: (JComponent) -> Unit) {
        CoroutineScope(Dispatchers.Swing).launch {
            refreshUi.receiveAsFlow().collect {
                observer(it)
            }
        }
    }

    private val addNewTab: Channel<Pair<JComponent, String>> = Channel()

    fun addNewTab(component: JComponent, title: String) {
        CoroutineScope(Dispatchers.Default).launch { addNewTab.send(component to title) }
    }

    fun observeAddNewTab(observer: (Pair<JComponent, String>) -> Unit) {
        CoroutineScope(Dispatchers.Swing).launch {
            addNewTab.receiveAsFlow().collect {
                observer(it)
            }
        }
    }

    private val countCharacters: Channel<Unit> = Channel()

    fun countCharacters() {
        CoroutineScope(Dispatchers.Default).launch { countCharacters.send(Unit) }
    }

    fun observeCountCharacters(observer: () -> Unit) {
        CoroutineScope(Dispatchers.Swing).launch {
            countCharacters
                    .receiveAsFlow()
                    .collect { observer.invoke()  }
        }
    }

}