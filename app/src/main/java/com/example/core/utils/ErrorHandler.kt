package com.example.core.utils

import android.os.Build
import timber.log.Timber
import java.time.LocalDateTime
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ErrorHandler @Inject constructor(
    private val fileUtils: FileUtils
) : Thread.UncaughtExceptionHandler {

    private val defaultHandler = Thread.getDefaultUncaughtExceptionHandler()

    override fun uncaughtException(thread: Thread, throwable: Throwable) {
        try {
            val errorReport = buildErrorReport(thread, throwable)
            fileUtils.appendToErrorLog(errorReport)
            Timber.e(throwable, "Uncaught exception on thread: ${thread.name}")
        } finally {
            defaultHandler?.uncaughtException(thread, throwable)
        }
    }

    private fun buildErrorReport(thread: Thread, throwable: Throwable): String = buildString {
        appendLine("=".repeat(60))
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            appendLine("CRASH REPORT — ${LocalDateTime.now()}")
        }
        appendLine("Thread  : ${thread.name}")
        appendLine("Device  : ${Build.MODEL} (Android ${Build.VERSION.RELEASE})")
        appendLine("App     : Extension Builder")
        appendLine("-".repeat(60))
        appendLine("Exception: ${throwable::class.qualifiedName}")
        appendLine("Message : ${throwable.message}")
        appendLine("-".repeat(60))
        appendLine("Stack Trace:")
        appendLine(throwable.stackTraceToString())
        appendLine("=".repeat(60))
    }
}
