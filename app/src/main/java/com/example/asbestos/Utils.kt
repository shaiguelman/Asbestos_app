package com.example.asbestos

import android.content.Context
import java.nio.charset.Charset

fun readTextFromRawFile(context: Context, rawFileName: String): String {
    val ins =  context.resources.openRawResource(
        context.resources.getIdentifier(
            rawFileName,
            "raw", context.packageName
        )
    )
    return ins.readBytes().toString(Charset.defaultCharset())
}