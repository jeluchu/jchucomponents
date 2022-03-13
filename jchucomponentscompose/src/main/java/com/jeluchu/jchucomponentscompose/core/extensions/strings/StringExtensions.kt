package com.jeluchu.jchucomponentscompose.core.extensions.strings

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.util.Base64
import android.util.Log
import android.util.Patterns
import androidx.compose.ui.graphics.Color
import com.jeluchu.jchucomponentscompose.core.extensions.date.DATE_FORMAT_TIMESTAMP
import com.jeluchu.jchucomponentscompose.core.extensions.date.DATE_FORMAT_VERBOSE
import org.intellij.lang.annotations.RegExp
import org.json.JSONObject
import java.io.File
import java.io.FileOutputStream
import java.net.HttpURLConnection
import java.net.URL
import java.net.URLEncoder
import java.text.DateFormat
import java.text.Format
import java.text.SimpleDateFormat
import java.util.*
import java.util.regex.Pattern

/** ---- REGEX AND CONST ----------------------------------------------------------------------- **/

private const val NAME_PATTERN = "^([0-9]`´´¡!¿?<>ºª|/\\·@#$%&,;=\\(\\))_"
private const val NAME_SURNAME_PATTERN =
    "/^(?!.[0-9`´¡!?¿<>\\\\ºª|/\\·@#$%&,;=?¿())_+{}\\[\\]\"^€$£])/"
private const val VALID_PASSWORD_REGEX =
    "^(?!.*[\\s])(?=.*[A-Z])(?=.*[!\\\"#$'()*+,-.\\/:;<=>?@\\[\\]^_`{|}\\]])(?=.*[0-9])(?=.*[a-z]).{8,20}$"

private const val PASSWORD_REGEX = "^[a-zñA-ZÑ0-9!\\\\/\"#$'()*+,;.:\\-_<=>?@\\[\\]^`{}|]*$"
private const val VALID_PHONE_REGEX = "^(00[0-9]{9,20}|(?!00)[0-9]{9,20})"
private const val validCharacters =
    "abcdefghijklmnñopqrstuvwxyzABCDEFGHIJKLMNÑOPQRSTUVWXYZáéíóúÁÉÍÓÚ -àÀáÁâÂãÃäÄåÅæÆçÇèÈéÉêÊëËìÌíÍîÎïÏðÐñÑòÒóÓôÔõÕöÖøØùÙúÚûÛüÜýÝþÞÿß'."

private val LETRAS_NIF = "TRWAGMYFPDXBNJZSQVHLCKE"

/** ---- IMAGES DOWNLOAD URL ------------------------------------------------------------------- **/

fun String.getLastBitFromUrl(): String = replaceFirst(".*/([^/?]+).*".toRegex(), "$1")

fun String.saveImage(destinationFile: File) {
    runCatching {
        Thread {
            val url = URL(this)
            val inputStream = url.openStream()
            val os = FileOutputStream(destinationFile)
            val b = ByteArray(2048)
            var length: Int
            while (inputStream.read(b).also { length = it } != -1) {
                os.write(b, 0, length)
            }
            inputStream?.close()
            os.close()
        }.start()

    }.getOrElse {
        it.printStackTrace()
    }
}

/** ---- COMPOSE FUNCTIONS --------------------------------------------------------------------- **/

fun String.getColor() = Color(android.graphics.Color.parseColor(this))

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
    runCatching {
        val connection = URL(this).openConnection() as HttpURLConnection
        connection.doInput = true
        connection.connect()
        BitmapFactory.decodeStream(connection.inputStream)
    }.getOrElse { null }

fun String.toBitmapDrawable(context: Context): BitmapDrawable =
    BitmapDrawable(context.resources, getBitmapFromURL())


fun String.deleteDouble() = this.split(".")[0]

/** ---- DATE ---------------------------------------------------------------------------------- **/

fun String.parserDate(): String {
    val parser = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale("es", "ES"))
    val formatter = SimpleDateFormat("dd/MM/yyyy", Locale("es", "ES"))
    return formatter.format(parser.parse(this) ?: Date())
}

fun String?.compareDate(): Boolean {
    val sdf = SimpleDateFormat("dd/M/yyyy", Locale("es", "ES"))
    val currentDate = sdf.format(Date())
    return if (this.isNullOrEmpty()) false else (sdf.parse(this) ?: Date()).before(
        sdf.parse(
            currentDate
        )
    )
}

fun String?.toDate(): Date {
    return runCatching {
        val formatter = SimpleDateFormat(DATE_FORMAT_VERBOSE, Locale.getDefault())
        formatter.parse(this.orEmpty()) as Date
    }.getOrElse { Date(0) }
}

fun String?.toDateWithTime(): Date {
    runCatching {
        val formatter = SimpleDateFormat(DATE_FORMAT_TIMESTAMP, Locale.getDefault())
        return formatter.parse(this.orEmpty()) as Date
    }.getOrElse {
        return this.toDate()
    }
}

fun String?.toDateTimeNull(): Date? =
    runCatching {
        SimpleDateFormat(DATE_FORMAT_TIMESTAMP, Locale.getDefault()).parse(this.safeValue())
    }.getOrElse {
        Log.d("error to parse dateTime", this.safeValue())
        null
    }

/** ---- CHECKER ------------------------------------------------------------------------------- **/

fun String?.safeValue(default: String = String.empty()) = this?.let { return@let it } ?: default

fun String?.removeCharactersWeirds() =
    this?.let { return@let it.replace("\n", String.empty()).replace("\r", String.empty()) }
        ?: String.empty()

fun CharSequence.isEmptyString(): Boolean = this.isEmpty() || this.toString().equals("null", true)
fun CharSequence.isDigitOnly(): Boolean = (0 until length).any { Character.isDigit(this[it]) }

fun Char.isAlphabeticCharacter() =
    this == '-' || this == '.' || this == '\'' || Character.isLetter(this) || Character.isSpaceChar(
        this
    )

fun String.isValidString(): Boolean {
    for (c in toCharArray()) {
        if (!validCharacters.contains(String.empty() + c)) return false
    }
    return true
}

fun String.isNumber() =
    String.empty() != this && Pattern.compile("^[0-9]*$").matcher(this).matches()

fun String.isEmail(): Boolean =
    String.empty() != this && Patterns.EMAIL_ADDRESS.matcher(this).matches()

fun String.isPassword() =
    String.empty() != this && Pattern.compile(PASSWORD_REGEX).matcher(this).matches()

fun String.isValidPassword() =
    String.empty() != this && Pattern.compile(VALID_PASSWORD_REGEX).matcher(this).matches()

fun String.isValidPhone() =
    String.empty() != this && Pattern.compile(VALID_PHONE_REGEX).matcher(this).matches()

fun isValidDNI(documentNumber: String?): Boolean {
    if (documentNumber.isNullOrEmpty()) return false
    else {
        val nif = documentNumber.uppercase()
        if (nif.matches("[0-9]{8}[$LETRAS_NIF]".toRegex())) {
            val dni = Integer.parseInt(nif.substring(0, 8))
            val letter = LETRAS_NIF[dni % 23]
            if (letter == nif[8]) return true
        }
        return false
    }
}

fun isValidNIE(documentNumber: String?) =
    if (documentNumber.isNullOrEmpty()) false
    else {
        val nie = documentNumber.uppercase()
        when (nie[0]) {
            'X' -> isValidDNI(nie.replaceFirst("X", "0"))
            'Y' -> isValidDNI(nie.replaceFirst("Y", "1"))
            'Z' -> isValidDNI(nie.replaceFirst("Z", "2"))
            else -> false
        }
    }

fun Uri.isMediaUri() = authority.equals("media", true)
fun Uri.isExternalStorageDocument() = authority == "com.android.externalstorage.documents"
fun Uri.isDownloadDocuments() = authority == "com.android.providers.downloads.documents"
fun Uri.isMediaDocument() = authority == "com.android.providers.media.documents"

fun File.getUri(): Uri = Uri.fromFile(this)

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
fun Any.readResourceText(resource: String): String? =
    this.javaClass.classLoader?.getResource(resource)?.readText()

inline val String.isIp: Boolean
    get() {
        val p =
            Pattern.compile("([1-9]|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])(\\.(\\d|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])){3}")
        val m = p.matcher(this)
        return m.find()
    }

fun String?.returnNullIfEmpty() =
    if (this != null && this.isEmpty()) null
    else this

/** ---- ENCODE & DECODE ----------------------------------------------------------------------- **/

fun String.fromNormalBase64ToImage(): Bitmap {
    val decodedString: ByteArray = Base64.decode(this, Base64.DEFAULT)
    return BitmapFactory.decodeByteArray(decodedString, 0, decodedString.size)
}

fun String.encodeToBase64(): String = Base64.encodeToString(toByteArray(), Base64.NO_WRAP)

fun String.decodeBase64(): String = String(Base64.decode(toByteArray(), Base64.NO_WRAP))

fun String.decodeBase64toImage(): Bitmap {
    val imageBytes = Base64.decode(this, Base64.DEFAULT)
    return BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
}

fun encodeMapToUTF8Form(data: Map<String, String?>): String {

    var form = String.empty()

    data.forEach {

        if (it.value != null)
            form = form + it.key + "=" +
                    URLEncoder.encode(it.value, "UTF-8") + "&"
    }

    return form.dropLast(1)

}

fun String?.decodeQRBase64(): ByteArray =
    if (this.isNullOrEmpty()) byteArrayOf()
    else Base64.decode(this, Base64.DEFAULT)

/** ---- CONVERTERS AND MODIFIERS -------------------------------------------------------------- **/

fun String.capitalizeWords(): String =
    split(" ").joinToString(" ") { it.uppercase(Locale.getDefault()) }

fun String.capitalizeFirstLetter(): String =
    substring(0, 1).uppercase(Locale.getDefault()) + this.substring(1)

fun String.remove(value: String, ignoreCase: Boolean = false): String =
    replace(value, String.empty(), ignoreCase)

fun String.remove(@RegExp pattern: String) = remove(Regex(pattern, RegexOption.IGNORE_CASE))
fun String.remove(regex: Regex) = replace(regex, String.empty())

fun String.Companion.empty() = ""
fun CharSequence.isEmpty(): Boolean = length == 0

fun String.isEmpty(): Boolean = length == 0
fun String.replace(): String = replace("-", " ")

fun String.parseDate(): String? =
    if (this.contains("-")) {
        var formatter: Format = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val date = (formatter as DateFormat).parse(this) as Date
        formatter = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        formatter.format(date)
    } else this

const val DOT = '.'
fun String.jwtBodyToJsonObject(): JSONObject? {
    var result: JSONObject? = null
    if (this.contains(DOT)) {
        val base64EncodedBody = this.split(DOT)[1]
        runCatching {
            val base64DecodedBody = Base64.decode(base64EncodedBody, Base64.NO_WRAP)
            if (base64DecodedBody != null) {
                val body = String(base64DecodedBody, Charsets.UTF_8)
                runCatching {
                    result = JSONObject(body)
                }.getOrElse {
                    Log.e("ERROR_JCHU", "Error while parsing JWT JSON body")
                }
            }
        }.getOrElse {
            Log.e("ERROR_JCHU", "Error while parsing JWT JSON body")
        }
    }
    return result
}


/** ---- CURRENCIES ---------------------------------------------------------------------------- **/

fun String.getCurrencyString() =
    when (this) {
        "EUR" -> "€"
        "GBP" -> "£"
        "USD" -> "$"
        "CHF" -> "Fr"
        "SEK" -> "kr"
        "DKK" -> "kr"
        "RUB" -> "₽"
        else -> "€"
    }
