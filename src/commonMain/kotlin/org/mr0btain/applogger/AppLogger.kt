package org.mr0btain.applogger


/**
 * Logs to the platform's own log (logcat, the console, java.util.logging, NSLog) and keeps each line in
 * [LogBook], which can also save them to files ([LogBook.attach]). Tags are hierarchical constants, like
 * `"$TAG:Function"`
 */
expect object AppLogger {
    /**
     * Whether [d] writes anything. Off until the app turns it on, for debug builds say
     */
    var debug: Boolean

    /** Exceptions, thrown when something bad has happened */
    fun e(tag: String, message: String, throwable: Throwable? = null)
    fun e(tag: String, message: LogMessage, throwable: Throwable? = null)

    /** Something looked wrong, but it carried on */
    fun w(tag: String, message: String, throwable: Throwable? = null)
    fun w(tag: String, message: LogMessage, throwable: Throwable? = null)

    /** Debug messages, only written when [debug] is on */
    fun d(tag: String, message: String)

    /** Info */
    fun i(tag: String, message: String)
    fun i(tag: String, message: LogMessage)

    /** General message */
    fun m(tag: String, message: String)
    fun m(tag: String, message: LogMessage)
}
