package com.jeluchu.jchucomponentscompose.utils.zxing

class ChecksumException : ReaderException {
    companion object {
        private val INSTANCE = ChecksumException()
        val checksumInstance: ChecksumException
            get() = if (isStackTrace) ChecksumException() else INSTANCE

        fun getChecksumInstance(cause: Throwable): ChecksumException =
            if (isStackTrace) ChecksumException(cause) else INSTANCE

        init {
            INSTANCE.stackTrace = NO_TRACE // since it's meaningless
        }
    }

    private constructor()
    private constructor(cause: Throwable) : super(cause)

}