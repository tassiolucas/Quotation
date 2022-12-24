package com.quotation.ext

import io.reactivex.Scheduler

interface ExecutionThread {
    val scheduler: Scheduler
}
