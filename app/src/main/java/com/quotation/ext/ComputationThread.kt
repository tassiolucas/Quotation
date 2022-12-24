package com.quotation.ext

import io.reactivex.Scheduler

interface ComputationThread {
    val scheduler: Scheduler
}
