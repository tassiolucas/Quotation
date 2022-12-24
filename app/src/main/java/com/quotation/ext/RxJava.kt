package com.quotation.ext

import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Observable
import io.reactivex.Single
import timber.log.Timber

fun <T> Observable<T>.applySchedulers(executors: Executors): Observable<T> {
    return subscribeOn(executors.io)
        .observeOn(executors.ui)
}

fun <T> Flowable<T>.applySchedulers(executors: Executors): Flowable<T> {
    return subscribeOn(executors.io)
        .observeOn(executors.ui)
}

fun Completable.applySchedulers(executors: Executors): Completable {
    return subscribeOn(executors.io)
        .observeOn(executors.ui)
}

fun <T> Single<T>.applySchedulers(executors: Executors): Single<T> {
    return subscribeOn(executors.io)
        .observeOn(executors.ui)
}

inline fun w(t: Throwable? = null, message: () -> String) = log { Timber.w(t, message()) }

inline fun w(t: Throwable?) = Timber.w(t)

@PublishedApi
internal inline fun log(block: () -> Unit) {
    if (Timber.treeCount() > 0) block()
}