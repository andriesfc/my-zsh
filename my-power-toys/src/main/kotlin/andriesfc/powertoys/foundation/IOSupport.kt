package andriesfc.powertoys.foundation

import java.io.OutputStream

sealed interface FlushOnClose {
    val flushOnClose: Boolean
    fun flushOnClose(enabled: Boolean)
    fun flushOnClose() = flushOnClose(true)
    fun reallyClose()
    val flushPending: Boolean
}

fun OutputStream.withFlushOnCloseInstead(): OutputStream {
    return when (this) {
        is FlushOnClose -> this
        else -> OutputStreamWithFlushOnClose(this)
    }
}

private class OutputStreamWithFlushOnClose(
    private val underlyingOutputStream: OutputStream,
) : OutputStream(), FlushOnClose {

    override var flushOnClose: Boolean = true; private set
    override var flushPending = false; private set

    override fun write(b: Int) = underlyingOutputStream.write(b).also { postOutput(1) }
    override fun write(b: ByteArray, off: Int, len: Int) =
        underlyingOutputStream.write(b, off, len).also { postOutput(len) }

    override fun write(b: ByteArray) = underlyingOutputStream.write(b).also { postOutput(b.size) }

    override fun reallyClose() {
        flush()
        underlyingOutputStream.close()
    }

    private fun postOutput(bytesOut: Int) {
        if (bytesOut > 0 && flushOnClose && !flushPending)
            flushPending = true
    }

    override fun flushOnClose(enabled: Boolean) {
        if (flushOnClose != enabled) {
            if (!flushOnClose && flushPending)
                flush()
            flushOnClose = enabled
        }
    }

    override fun flush() {
        underlyingOutputStream.flush()
        flushPending = false
    }

    override fun close() {
        flush()
        if (!flushOnClose)
            underlyingOutputStream.close()
    }
}
