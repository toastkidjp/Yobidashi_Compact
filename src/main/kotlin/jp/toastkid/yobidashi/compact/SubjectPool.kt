package jp.toastkid.yobidashi.compact

import io.reactivex.rxjava3.functions.Consumer
import io.reactivex.rxjava3.subjects.PublishSubject
import jp.toastkid.yobidashi.compact.model.Article
import jp.toastkid.yobidashi.compact.model.Sorting
import jp.toastkid.yobidashi.compact.view.ArticleListView

object SubjectPool {

    private val pool: PublishSubject<ArticleListView> = PublishSubject.create()

    fun next(next: ArticleListView) {
        pool.onNext(next)
    }

    fun observe(onNext: Consumer<ArticleListView>) {
        pool.subscribe(onNext)
    }

    private val sort: PublishSubject<Sorting> = PublishSubject.create()

    fun nextSorting(next: Sorting) {
        sort.onNext(next)
    }

    fun observeSort(onNext: Consumer<Sorting>) {
        sort.subscribe(onNext)
    }

    private val closeWindow: PublishSubject<Unit> = PublishSubject.create()

    fun closeWindow() = closeWindow.onNext(Unit)

    fun observeCloseWindow(onNext: Consumer<Unit>) = closeWindow.subscribe(onNext)

    private val addToList: PublishSubject<Article> = PublishSubject.create()

    fun addToList(article: Article) = addToList.onNext(article)

    fun observeAddToList(onNext: Consumer<Article>) = addToList.subscribe(onNext)
}