package jp.toastkid.yobidashi.compact.viewmodel

import io.reactivex.rxjava3.functions.Consumer
import io.reactivex.rxjava3.subjects.PublishSubject

object ZipViewModel {

    private val zip = PublishSubject.create<Unit>()

    fun zip() {
        zip.onNext(Unit)
    }

    fun observe(onNext: Consumer<Unit>) {
        zip.subscribe(onNext)
    }

}