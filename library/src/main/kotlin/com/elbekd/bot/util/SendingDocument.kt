package com.elbekd.bot.util

import java.io.File

public sealed class SendingDocument {
    public abstract val fileName: String?
}

public class SendingByteArray(
    public val content: ByteArray,
    public override val fileName: String? = null,
) : SendingDocument()

public class SendingString(
    public val content: String,
    public override val fileName: String? = null,
) : SendingDocument()

public class SendingFile(public val file: File) : SendingDocument() {
    override val fileName: String
        get() = file.name
}
