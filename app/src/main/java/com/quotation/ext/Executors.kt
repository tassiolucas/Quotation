package com.quotation.ext

class Executors constructor(
    executionThread: ExecutionThread,
    postExecutionThread: PostExecutionThread,
    computationThread: ComputationThread
) {
    val io = executionThread.scheduler
    val ui = postExecutionThread.scheduler
    val computation = computationThread.scheduler
}