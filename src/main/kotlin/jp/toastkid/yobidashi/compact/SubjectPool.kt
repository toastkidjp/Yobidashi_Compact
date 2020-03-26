package jp.toastkid.yobidashi.compact

import io.reactivex.rxjava3.functions.Consumer
import io.reactivex.rxjava3.subjects.PublishSubject
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
}