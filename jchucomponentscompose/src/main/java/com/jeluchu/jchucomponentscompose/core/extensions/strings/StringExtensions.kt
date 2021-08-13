package com.jeluchu.jchucomponentscompose.core.extensions.strings

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.util.Base64
import org.intellij.lang.annotations.RegExp
import java.io.File
import java.io.IOException
import java.net.HttpURLConnection
import java.net.URL
import java.text.DateFormat
import java.text.Format
import java.text.SimpleDateFormat
import java.util.*
import java.util.regex.Pattern

fun String.deleteDouble() = this.split(".")[0]

/** ---- DATE ---------------------------------------------------------------------------------- **/

fun String.parserDate(): String {
    val parser = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale("es", "ES"))
    val formatter = SimpleDateFormat("dd/MM/yyyy", Locale("es", "ES"))
    return formatter.format(parser.parse(this))
}

fun String?.compareDate(): Boolean {
    val sdf = SimpleDateFormat("dd/M/yyyy", Locale("es", "ES"))
    val currentDate = sdf.format(Date())
    return if (this.isNullOrEmpty()) false else sdf.parse(this).before(sdf.parse(currentDate))
}

fun Long.bytesToMeg(): String = (this / (1024L * 1024L)).toString()

/** ---- YOUTUBE ------------------------------------------------------------------------------- **/

fun String.extractYTId(): String? {
    var vId: String? = null
    val pattern = Pattern.compile(
        "^https?://.*(?:youtu.be/|v/|u/\\w/|embed/|watch?v=)([^#&?]*).*$",
        Pattern.CASE_INSENSITIVE
    )
    val matcher = pattern.matcher(this)
    if (matcher.matches()) {
        vId = matcher.group(1)
    }
    return vId
}


/** ---- BITMAPS ------------------------------------------------------------------------------- **/

fun String.getBitmapFromURL(): Bitmap? =
    try {
        val connection = URL(this).openConnection() as HttpURLConnection
        connection.doInput = true
        connection.connect()
        BitmapFactory.decodeStream(connection.inputStream)
    } catch (e: IOException) {
        null
    }

fun String.toBitmapDrawable(context: Context): BitmapDrawable =
    BitmapDrawable(context.resources, getBitmapFromURL())

/** ---- ENCODE & DECODE ----------------------------------------------------------------------- **/

fun String.decodeBase64toImage(): Bitmap {
    val imageBytes = Base64.decode(this, Base64.DEFAULT)
    return BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
}

/** ---- MODIFY STRING ------------------------------------------------------------------------- **/

fun String.capitalizeWords(): String =
    split(" ").joinToString(" ") { it.capitalize(Locale.getDefault()) }

fun String.capitalizeFirstLetter(): String =
    substring(0, 1).uppercase(Locale("es", "ES")) + this.substring(1)

/** ---- REMOVE STRING ------------------------------------------------------------------------- **/

fun String.remove(value: String, ignoreCase: Boolean = false): String =
    replace(value, "", ignoreCase)

fun String.remove(@RegExp pattern: String) = remove(Regex(pattern, RegexOption.IGNORE_CASE))
fun String.remove(regex: Regex) = replace(regex, "")


/** ---- CHECKER ------------------------------------------------------------------------------- **/

val String.isPhone: Boolean get() = matches("^1([34578])\\d{9}\$".toRegex())
val String.isEmail: Boolean get() = matches("^(\\w)+(\\.\\w+)*@(\\w)+((\\.\\w+)+)\$".toRegex())
val String.isNumeric: Boolean get() = matches("^[0-9]+$".toRegex())
val String.isAlphanumeric get() = matches("^[a-zA-Z0-9]*$".toRegex())
val String.isAlphabetic get() = matches("^[a-zA-Z]*$".toRegex())
fun String.isLocal() = !isEmptyString() && (startsWith("http://") || startsWith("https://"))

fun CharSequence.isEmptyString(): Boolean = this.isEmpty() || this.toString().equals("null", true)
fun CharSequence.isDigitOnly(): Boolean = (0 until length).any { Character.isDigit(this[it]) }

fun Uri.isMediaUri() = authority.equals("media", true)
fun Uri.isExternalStorageDocument() = authority == "com.android.externalstorage.documents"
fun Uri.isDownloadDocuments() = authority == "com.android.providers.downloads.documents"
fun Uri.isMediaDocument() = authority == "com.android.providers.media.documents"

fun File.getUri() = Uri.fromFile(this)

val String.containsLetters get() = matches(".*[a-zA-Z].*".toRegex())
val String.containsNumbers get() = matches(".*[0-9].*".toRegex())
fun String.noNumbers(): Boolean = matches(Regex(".*\\d.*"))
fun String.onlyNumbers(): Boolean = !matches(Regex("\\d+"))
fun String.allUpperCase(): Boolean = uppercase() != this
fun String.allLowerCase(): Boolean = lowercase() != this
fun String.atLeastOneLowerCase(): Boolean = matches(Regex("[A-Z0-9]+"))
fun String.atLeastOneUpperCase(): Boolean = matches(Regex("[a-z0-9]+"))
fun String.atLeastOneNumber(): Boolean = !matches(Regex(".*\\d.*"))
fun String.startsWithNonNumber(): Boolean = Character.isDigit(this[0])
fun String.noSpecialCharacter(): Boolean = !matches(Regex("[A-Za-z0-9]+"))
fun String.atLeastOneSpecialCharacter(): Boolean = matches(Regex("[A-Za-z0-9]+"))
fun Any.readResourceText(resource: String): String? = this.javaClass.classLoader?.getResource(resource)?.readText()

inline val String.isIp: Boolean
    get() {
        val p = Pattern.compile("([1-9]|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])(\\.(\\d|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])){3}")
        val m = p.matcher(this)
        return m.find()
    }

/** ---- FUNCTION STRING ----------------------------------------------------------------------- **/

fun String.Companion.empty() = ""
fun String?.orEmpty(): String = this ?: String.empty()

fun String.isEmpty(): Boolean = length == 0

fun String.replace(): String = replace("-", " ")

fun String.parseDate(): String? =
    if (this.contains("-")) {
        var formatter: Format = SimpleDateFormat("yyyy-MM-dd", Locale.FRANCE)
        val date = (formatter as DateFormat).parse(this) as Date
        formatter = SimpleDateFormat("dd/MM/yyyy", Locale.FRANCE)
        formatter.format(date)
    } else {
        this
    }

