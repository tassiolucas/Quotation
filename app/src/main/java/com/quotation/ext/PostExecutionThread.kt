package com.quotation.ext

import io.reactivex.Scheduler

interface PostExecutionThread {
    val scheduler: Scheduler
}
