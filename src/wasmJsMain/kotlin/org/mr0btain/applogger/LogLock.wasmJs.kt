package org.mr0btain.applogger

// A browser page runs on one thread
internal actual class LogLock actual constructor() {
    actual fun <T> withLock(block: () -> T): T = block()
}
