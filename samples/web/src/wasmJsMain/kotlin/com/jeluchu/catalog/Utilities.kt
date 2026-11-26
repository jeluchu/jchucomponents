package com.jeluchu.catalog

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.runtime.rememberCoroutineScope
import com.jeluchu.jchucomponents.foundation.coroutines.cancelIfActive
import com.jeluchu.jchucomponents.foundation.coroutines.rethrowIfCancellation
import com.jeluchu.jchucomponents.foundation.coroutines.runCatchingCancellable
import com.jeluchu.jchucomponents.foundation.coroutines.runCatchingExceptions
import com.jeluchu.jchucomponents.ui.extensions.time.clock
import com.jeluchu.jchucomponents.ui.extensions.time.clockTime
import com.jeluchu.jchucomponents.ui.extensions.time.clockWithZoneDay
import com.jeluchu.jchucomponents.ui.foundation.lists.composition.rememberPagerSnapState
import com.jeluchu.jchucomponents.ui.runtime.remember.rememberCountdownTimerState
import com.jeluchu.jchucomponents.ui.runtime.remember.rememberDerivedStateOf
import com.jeluchu.jchucomponents.ui.runtime.remember.rememberMutableDoubleStateOf
import com.jeluchu.jchucomponents.ui.runtime.remember.rememberMutableFloatStateOf
import com.jeluchu.jchucomponents.ui.runtime.remember.rememberMutableIntStateOf
import com.jeluchu.jchucomponents.ui.runtime.remember.rememberMutableLongStateOf
import com.jeluchu.jchucomponents.ui.runtime.remember.rememberMutableStateOf
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import com.jeluchu.catalog.resources.Res
import com.jeluchu.catalog.resources.catalog_title
import com.jeluchu.catalog.resources.greeting
import com.jeluchu.catalog.resources.leaf
import com.jeluchu.jchucomponents.foundation.serialization.convert
import com.jeluchu.jchucomponents.foundation.serialization.decodeListOrNull
import com.jeluchu.jchucomponents.foundation.serialization.decodeOrNull
import com.jeluchu.jchucomponents.foundation.serialization.defaultJson
import com.jeluchu.jchucomponents.foundation.serialization.toJsonObject
import com.jeluchu.jchucomponents.foundation.serialization.toObject
import com.jeluchu.jchucomponents.ui.extensions.toImageVector
import com.jeluchu.jchucomponents.ui.extensions.toPainter
import com.jeluchu.jchucomponents.ui.extensions.toStringRes
import kotlinx.serialization.Serializable
import com.jeluchu.jchucomponents.foundation.crypto.CaesarCipher
import com.jeluchu.jchucomponents.foundation.extensions.isNull
import com.jeluchu.jchucomponents.foundation.extensions.isTrue
import com.jeluchu.jchucomponents.foundation.extensions.orEmpty
import com.jeluchu.jchucomponents.foundation.extensions.orFalse
import com.jeluchu.jchucomponents.foundation.extensions.orZero
import com.jeluchu.jchucomponents.foundation.finance.IbanUtils
import com.jeluchu.jchucomponents.foundation.functional.Either
import com.jeluchu.jchucomponents.foundation.functional.getOrElse
import com.jeluchu.jchucomponents.foundation.functional.left
import com.jeluchu.jchucomponents.foundation.functional.map
import com.jeluchu.jchucomponents.foundation.functional.right
import com.jeluchu.jchucomponents.foundation.location.getDistanceInMeters
import com.jeluchu.jchucomponents.foundation.random.getRandomUUID
import com.jeluchu.jchucomponents.foundation.random.randDouble
import com.jeluchu.jchucomponents.foundation.random.randInt
import com.jeluchu.jchucomponents.foundation.text.addSpaceAfterEvery3Chars
import com.jeluchu.jchucomponents.foundation.text.addSpaceAfterEvery4Chars
import com.jeluchu.jchucomponents.foundation.text.capitalizeFirstLetter
import com.jeluchu.jchucomponents.foundation.text.capitalizeWords
import com.jeluchu.jchucomponents.foundation.text.extractYTId
import com.jeluchu.jchucomponents.foundation.text.formatInGroups
import com.jeluchu.jchucomponents.foundation.text.getCurrencyString
import com.jeluchu.jchucomponents.foundation.text.getLastBitFromUrl
import com.jeluchu.jchucomponents.foundation.text.isBuyNotAvailability
import com.jeluchu.jchucomponents.foundation.text.isNumber
import com.jeluchu.jchucomponents.foundation.text.normalizeForSearch
import com.jeluchu.jchucomponents.foundation.text.onlyDigits
import com.jeluchu.jchucomponents.foundation.text.removeDiacritics
import com.jeluchu.jchucomponents.foundation.text.removeFirstLastChar
import com.jeluchu.jchucomponents.foundation.text.toHttpsUrl
import com.jeluchu.jchucomponents.foundation.text.wordCount
import com.jeluchu.jchucomponents.foundation.validators.CreditCardValidator
import com.jeluchu.jchucomponents.foundation.extensions.bytesToMeg
import com.jeluchu.jchucomponents.foundation.extensions.isNegative
import com.jeluchu.jchucomponents.foundation.extensions.isNegativeOrZero
import com.jeluchu.jchucomponents.foundation.extensions.isPositive
import com.jeluchu.jchucomponents.foundation.extensions.isPositiveOrZero
import com.jeluchu.jchucomponents.foundation.extensions.lists.addAllIfNotExist
import com.jeluchu.jchucomponents.foundation.extensions.lists.concatenateLowercase
import com.jeluchu.jchucomponents.foundation.extensions.roundUpToNearestTen
import com.jeluchu.jchucomponents.foundation.functional.flatMap
import com.jeluchu.jchucomponents.foundation.text.deleteDouble
import com.jeluchu.jchucomponents.foundation.text.getTranslateOrDefault
import com.jeluchu.jchucomponents.foundation.text.isEmptyString
import com.jeluchu.jchucomponents.foundation.text.isNotZero
import com.jeluchu.jchucomponents.foundation.text.removeCharactersWeirds
import com.jeluchu.jchucomponents.foundation.text.returnNullIfEmpty
import com.jeluchu.jchucomponents.foundation.text.safeValue
import com.jeluchu.jchucomponents.foundation.version.compareVersionTo
import com.jeluchu.jchucomponents.ui.extensions.time.fixedDecimalsTime
import com.jeluchu.jchucomponents.foundation.version.compareVersions

@Serializable
data class Anime(val title: String, val episodes: Int = 12)

@Serializable
data class AnimeCard(val title: String, val episodes: Int = 0, val badge: String = "new")

/** One utility card: a group, a name for search and deep links, and a live preview. */
class UtilCard(val group: String, val name: String, val description: String, val body: @Composable () -> Unit) {
    /** Short id for URLs: the first function of a multi-function card, such as toPainter. */
    val slug: String = name.substringBefore(',').trim()
}

@Composable
private fun UtilInput(value: String, onChange: (String) -> Unit, label: String, modifier: Modifier = Modifier) {
    val p = LocalPalette.current
    Column(modifier, verticalArrangement = Arrangement.spacedBy(4.dp)) {
        Text(label, color = p.muted, fontSize = 11.sp, fontWeight = FontWeight.Bold)
        Box(Modifier.fillMaxWidth().height(44.dp).clip(RoundedCornerShape(12.dp)).background(p.bg).border(1.dp, p.line, RoundedCornerShape(12.dp)).padding(horizontal = 12.dp), contentAlignment = Alignment.CenterStart) {
            BasicTextField(
                value = value, onValueChange = onChange, singleLine = true,
                textStyle = TextStyle(color = p.fg, fontSize = 14.sp, fontFamily = FontFamily.Monospace),
                cursorBrush = SolidColor(p.accent), modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Composable
private fun UtilOutput(label: String, value: String) {
    val p = LocalPalette.current
    Column(Modifier.fillMaxWidth().clip(RoundedCornerShape(12.dp)).background(p.stage).padding(horizontal = 14.dp, vertical = 10.dp)) {
        Text(label, color = p.muted, fontSize = 11.sp, fontWeight = FontWeight.Bold)
        Text(value, color = p.fg, fontSize = 16.sp, fontWeight = FontWeight.ExtraBold, fontFamily = FontFamily.Monospace)
    }
}

/** A text-in, text-out utility: edit the input and see the result and the code update. */
@Composable
private fun TextLab(default: String, snippet: (String) -> String, run: (String) -> String) {
    var text by remember { mutableStateOf(default) }
    val result = remember(text) { runCatching { run(text) }.getOrElse { "Error: ${it.message}" } }
    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        UtilInput(text, { text = it }, "Input")
        UtilOutput("Result", result)
        CodeBlock(snippet(text.replace("\"", "\\\"")) + "\n// \"$result\"")
    }
}

private fun textCard(group: String, name: String, description: String, default: String, snippet: (String) -> String, run: (String) -> String) =
    UtilCard(group, name, description) { TextLab(default, snippet, run) }

val utilities: List<UtilCard> by lazy {
    listOf(
        textCard("Text", "capitalizeWords", "Capitalizes the first letter of every word.", "hello wide world", { "\"$it\".capitalizeWords()" }, { it.capitalizeWords() }),
        textCard("Text", "capitalizeFirstLetter", "Capitalizes only the first letter.", "hello wide world", { "\"$it\".capitalizeFirstLetter()" }, { it.capitalizeFirstLetter() }),
        textCard("Text", "formatInGroups", "Splits text into groups with a separator (default 4 and \"-\").", "1234567890123456", { "\"$it\".formatInGroups(groupSize = 4, separator = \"-\")" }, { it.formatInGroups() }),
        textCard("Text", "addSpaceAfterEvery4Chars", "Inserts a space every 4 characters, ideal for card numbers.", "4111111111111111", { "\"$it\".addSpaceAfterEvery4Chars()" }, { it.addSpaceAfterEvery4Chars() }),
        textCard("Text", "addSpaceAfterEvery3Chars", "Inserts a space every 3 characters, ideal for phone numbers.", "612345678", { "\"$it\".addSpaceAfterEvery3Chars()" }, { it.addSpaceAfterEvery3Chars() }),
        textCard("Text", "onlyDigits", "Keeps only the digits.", "+34 612-345-678", { "\"$it\".onlyDigits()" }, { it.onlyDigits() }),
        textCard("Text", "isNumber", "True when the text is a number.", "42.5", { "\"$it\".isNumber()" }, { it.isNumber().toString() }),
        textCard("Text", "wordCount", "Counts the words in a text.", "the quick brown fox", { "\"$it\".wordCount()" }, { it.wordCount().toString() }),
        textCard("Text", "removeDiacritics", "Removes accents and combining marks.", "Canción ñandú", { "\"$it\".removeDiacritics()" }, { it.removeDiacritics() }),
        textCard("Text", "normalizeForSearch", "Lowercases and strips accents, for searches that ignore case.", "Canción ÑANDÚ", { "\"$it\".normalizeForSearch()" }, { it.normalizeForSearch() }),
        textCard("Text", "extractYTId", "Extracts a YouTube video id from a URL.", "https://www.youtube.com/watch?v=dQw4w9WgXcQ", { "\"$it\".extractYTId()" }, { it.extractYTId().toString() }),
        textCard("Text", "getLastBitFromUrl", "Returns the last segment of a URL.", "https://example.com/images/poster.jpg", { "\"$it\".getLastBitFromUrl()" }, { it.getLastBitFromUrl() }),
        textCard("Text", "toHttpsUrl", "Forces an https scheme.", "http://example.com/path", { "\"$it\".toHttpsUrl()" }, { it.toHttpsUrl() }),
        textCard("Text", "removeFirstLastChar", "Removes the first and last characters.", "(hello)", { "\"$it\".removeFirstLastChar()" }, { it.removeFirstLastChar() }),
        textCard("Text", "getCurrencyString", "Currency symbol of an ISO code; unknown codes fall back to the euro sign.", "USD", { "\"$it\".getCurrencyString()" }, { it.getCurrencyString() }),
        textCard("Text", "isBuyNotAvailability", "True for store codes that mean not available (empty, 0, na, nts, NFS).", "NA", { "\"$it\".isBuyNotAvailability()" }, { it.isBuyNotAvailability().toString() }),
        textCard("Validation", "CreditCardValidator", "Detects the card brand and validates the number with the Luhn check.", "4111111111111111", { "CreditCardValidator.getCardType(\"$it\")\nCreditCardValidator.isValidCard(\"$it\")" }, {
            "${CreditCardValidator.getCardType(it).name} · valid=${CreditCardValidator.isValidCard(it)}"
        }),
        textCard("Finance", "IbanUtils", "Returns the two IBAN check digits for a Spanish 20-digit account; the IBAN is ES + check digits + account.", "21000418450200051332", { "IbanUtils.getIban(\"$it\")  // check digits; IBAN = \"ES\" + digits + account" }, { IbanUtils.getIban(it) }),
        textCard("Text", "isEmptyString", "True for empty text and for the literal word null.", "null", { "\"$it\".isEmptyString()" }, { it.isEmptyString().toString() }),
        textCard("Text", "safeValue", "Replaces a null or empty value with a default.", "", { "(if (\"$it\".isEmpty()) null else \"$it\").safeValue(\"n/a\")" }, { (if (it.isEmpty()) null else it).safeValue("n/a") }),
        textCard("Text", "returnNullIfEmpty", "Returns null instead of an empty string.", "", { "\"$it\".returnNullIfEmpty()" }, { it.returnNullIfEmpty().toString() }),
        textCard("Text", "deleteDouble", "Drops the decimals of a numeric string.", "12.75", { "\"$it\".deleteDouble()" }, { it.deleteDouble() }),
        textCard("Text", "getTranslateOrDefault", "Falls back to a default when a translation is empty.", "", { "\"$it\".getTranslateOrDefault(\"Untitled\")" }, { it.getTranslateOrDefault("Untitled") }),
        textCard("Text", "removeCharactersWeirds", "Removes unusual characters from a title.", "Hello_<World>!", { "\"$it\".removeCharactersWeirds()" }, { it.removeCharactersWeirds() }),
        textCard("Text", "isNotZero", "True when a numeric string is not zero.", "0", { "\"$it\".isNotZero()" }, { it.isNotZero().toString() }),
        textCard("Version", "compareVersionTo", "String shortcut for compareVersions against another version.", "2.1.0", { "\"$it\".compareVersionTo(\"2.0.9\")" }, { it.compareVersionTo("2.0.9").toString() }),
        UtilCard("Numbers", "isPositive, isNegative, roundUpToNearestTen, bytesToMeg, fixedDecimalsTime", "Sign checks and number formatting helpers.") {
            var text by remember { mutableStateOf("1234") }
            val n = text.toIntOrNull()
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                UtilInput(text, { text = it }, "Number")
                if (n == null) {
                    UtilOutput("Result", "enter an integer")
                } else {
                    UtilOutput("isPositive() / isNegative()", "${n.isPositive()} / ${n.isNegative()}")
                    UtilOutput("isPositiveOrZero() / isNegativeOrZero()", "${n.isPositiveOrZero()} / ${n.isNegativeOrZero()}")
                    UtilOutput("roundUpToNearestTen()", n.roundUpToNearestTen().toString())
                    UtilOutput("(n * 1_048_576L).bytesToMeg()", (n * 1_048_576L).bytesToMeg())
                    UtilOutput("fixedDecimalsTime()", n.fixedDecimalsTime())
                    CodeBlock("$n.isPositive()\n$n.roundUpToNearestTen()\n${n}L.bytesToMeg()\n$n.fixedDecimalsTime()")
                }
            }
        },
        UtilCard("Collections", "addAllIfNotExist, concatenateLowercase", "Add only new elements to a list, and join strings in lowercase.") {
            val list = remember { mutableStateListOf("Action", "Drama") }
            var joined by remember { mutableStateOf("") }
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Pill("addAllIfNotExist(Drama, Sci-Fi)", false, { list.addAllIfNotExist(listOf("Drama", "Sci-Fi")); joined = list.concatenateLowercase() }, mono = true)
                    Pill("reset", false, { list.clear(); list.addAll(listOf("Action", "Drama")); joined = "" })
                }
                UtilOutput("list", list.joinToString())
                UtilOutput("concatenateLowercase()", joined.ifEmpty { "(press the button)" })
                CodeBlock("val list = mutableListOf(\"Action\", \"Drama\")\nlist.addAllIfNotExist(listOf(\"Drama\", \"Sci-Fi\"))\nlist.concatenateLowercase()")
            }
        },
        UtilCard("State", "rememberMutable*StateOf, rememberDerivedStateOf, rememberCountdownTimerState", "Shorter remember helpers for state, a derived value and a countdown.") {
            val ints = rememberMutableIntStateOf(0)
            val longs = rememberMutableLongStateOf(0L)
            val floats = rememberMutableFloatStateOf(0f)
            val doubles = rememberMutableDoubleStateOf(0.0)
            var label by rememberMutableStateOf("idle")
            val doubled by rememberDerivedStateOf(ints.intValue) { ints.intValue * 2 }
            var round by remember { mutableStateOf(0) }
            val left by rememberCountdownTimerState(initialMillis = if (round % 2 == 0) 10_000L else 10_001L)
            val snap = rememberPagerSnapState()
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Pill("Tick all", false, { ints.intValue++; longs.longValue += 10L; floats.floatValue += 0.5f; doubles.doubleValue += 0.25; label = "ticked" }, height = 44.dp)
                    Pill("Restart countdown", false, { round++ }, height = 44.dp)
                }
                UtilOutput("Int / Long / Float / Double", "${ints.intValue} / ${longs.longValue} / ${floats.floatValue} / ${doubles.doubleValue}")
                UtilOutput("rememberMutableStateOf (String)", label)
                UtilOutput("rememberDerivedStateOf { count * 2 }", doubled.toString())
                UtilOutput("rememberCountdownTimerState(10_000)", "${left / 1000}s left")
                UtilOutput("rememberPagerSnapState().firstVisibleItemIndex", snap.firstVisibleItemIndex.value.toString())
                CodeBlock("val count = rememberMutableIntStateOf(0)\nval doubled by rememberDerivedStateOf(count.intValue) { count.intValue * 2 }\nval timeLeft by rememberCountdownTimerState(initialMillis = 10_000)\nval snap = rememberPagerSnapState()")
            }
        },
        UtilCard("State", "clock, clockTime, clockWithZoneDay", "Composables that return the current time and tick every second.") {
            val c = clock()
            val t = clockTime()
            val z = clockWithZoneDay()
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                UtilOutput("clock()", c)
                UtilOutput("clockTime()", "${t.hours}:${t.minutes} ${t.zoneDay}")
                UtilOutput("clockWithZoneDay()", z)
                CodeBlock("val time: String = clock()\nval parts: Time = clockTime()      // hours, minutes, zoneDay\nval withZone = clockWithZoneDay()")
            }
        },
        UtilCard("Coroutines", "runCatchingCancellable, runCatchingExceptions, rethrowIfCancellation, cancelIfActive", "Result helpers that never swallow cancellation, and a safe Job cancel.") {
            val scope = rememberCoroutineScope()
            var log by remember { mutableStateOf("press a button") }
            var job by remember { mutableStateOf<Job?>(null) }
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Pill("runCatchingCancellable { error }", false, { log = runCatchingCancellable { error("boom") }.fold({ "ok" }, { "failure: ${it.message}" }) }, mono = true)
                    Pill("runCatchingExceptions { 21 * 2 }", false, { log = runCatchingExceptions { 21 * 2 }.fold({ "success: $it" }, { "failure" }) }, mono = true)
                    Pill("start job", false, { job = scope.launch { try { log = "job running…"; delay(60_000); log = "job done" } catch (e: Throwable) { log = "caught ${e::class.simpleName}, rethrowing"; e.rethrowIfCancellation() } } }, mono = true)
                    Pill("cancelIfActive()", false, { job.cancelIfActive() }, mono = true)
                }
                UtilOutput("Result", log)
                CodeBlock("runCatchingCancellable { error(\"boom\") }   // Result.failure, but CancellationException is rethrown\nrunCatchingExceptions { 21 * 2 }\njob.cancelIfActive()\ncatch (e: Throwable) { e.rethrowIfCancellation() }")
            }
        },
        UtilCard("Serialization", "toObject, toJsonObject, convert, decodeOrNull, decodeListOrNull", "JSON helpers on top of kotlinx.serialization with a lenient default Json.") {
            var json by remember { mutableStateOf("""{"title":"Frieren","episodes":28,"extra":true}""") }
            val anime = remember(json) { runCatching { json.toObject<Anime>() }.getOrNull() }
            val asObject = remember(anime) { anime?.toJsonObject() }
            val card = remember(anime) { anime?.convert<Anime, AnimeCard>() }
            val orNull = remember(json) { defaultJson.decodeOrNull<Anime>(json) }
            val list = remember { defaultJson.decodeListOrNull<Anime>("""[{"title":"A"},{"title":"B","episodes":24}]""") }
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                UtilInput(json, { json = it }, "JSON")
                UtilOutput("String.toObject<Anime>()", anime?.toString() ?: "invalid JSON")
                UtilOutput("toJsonObject()", asObject?.toString() ?: "-")
                UtilOutput("convert<Anime, AnimeCard>()", card?.toString() ?: "-")
                UtilOutput("decodeOrNull<Anime>() / invalid", "${orNull != null} / ${defaultJson.decodeOrNull<Anime>("not json")}")
                UtilOutput("decodeListOrNull<Anime>()", list.toString())
                CodeBlock("@Serializable data class Anime(val title: String, val episodes: Int = 12)\n\njson.toObject<Anime>()\nanime.toJsonObject()\nanime.convert<Anime, AnimeCard>()\ndefaultJson.decodeOrNull<Anime>(json)\ndefaultJson.decodeListOrNull<Anime>(listJson)")
            }
        },
        UtilCard("Resources", "toPainter, toImageVector, toStringRes", "Load Compose Multiplatform drawables and strings with one call.") {
            val p = LocalPalette.current
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Row(horizontalArrangement = Arrangement.spacedBy(14.dp), verticalAlignment = Alignment.CenterVertically) {
                    androidx.compose.foundation.Image(Res.drawable.leaf.toPainter(), contentDescription = "toPainter", modifier = Modifier.size(48.dp))
                    androidx.compose.material3.Icon(Res.drawable.leaf.toImageVector(), contentDescription = "toImageVector", tint = p.mint, modifier = Modifier.size(48.dp))
                    Text("painter (left) and ImageVector with a tint (right)", color = p.muted, fontSize = 12.sp)
                }
                UtilOutput("Res.string.catalog_title.toStringRes()", Res.string.catalog_title.toStringRes())
                UtilOutput("Res.string.greeting.toStringRes(\"Jeluchu\", 3)", Res.string.greeting.toStringRes("Jeluchu", 3))
                CodeBlock("Image(Res.drawable.leaf.toPainter(), null)\nIcon(Res.drawable.leaf.toImageVector(), null)\nRes.string.greeting.toStringRes(\"Jeluchu\", 3)")
            }
        },
        UtilCard("Crypto", "CaesarCipher", "Encrypts and decrypts Latin letters by shifting them.") {
            var text by remember { mutableStateOf("Hello World") }
            var key by remember { mutableStateOf(CaesarCipher.DEFAULT_KEY) }
            val enc = remember(text, key) { CaesarCipher.encrypt(text, key) }
            val dec = remember(enc, key) { CaesarCipher.decrypt(enc, key) }
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                UtilInput(text, { text = it }, "Input")
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalAlignment = Alignment.CenterVertically) {
                    Pill("-", false, { key-- }); Text("key = $key", color = LocalPalette.current.fg, fontFamily = FontFamily.Monospace); Pill("+", false, { key++ })
                }
                UtilOutput("encrypt", enc)
                UtilOutput("decrypt", dec)
                CodeBlock("CaesarCipher.encrypt(\"$text\", key = $key)\n// \"$enc\"\nCaesarCipher.decrypt(\"$enc\", key = $key)\n// \"$dec\"")
            }
        },
        UtilCard("Version", "compareVersions", "Compares semantic versions, including pre-release tags.") {
            var a by remember { mutableStateOf("3.0.0-beta04") }
            var b by remember { mutableStateOf("3.0.0") }
            val c = remember(a, b) { runCatching { compareVersions(a, b) }.getOrNull() }
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    UtilInput(a, { a = it }, "First", Modifier.weight(1f)); UtilInput(b, { b = it }, "Second", Modifier.weight(1f))
                }
                UtilOutput("Result", when { c == null -> "invalid version"; c < 0 -> "first is older ($c)"; c > 0 -> "first is newer ($c)"; else -> "equal (0)" })
                CodeBlock("compareVersions(\"$a\", \"$b\")\n// ${c ?: "error"}")
            }
        },
        UtilCard("Location", "getDistanceInMeters", "Ground distance between two coordinates (haversine), with optional altitude.") {
            val places = listOf(
                Triple("Madrid to Barcelona", doubleArrayOf(40.4168, -3.7038), doubleArrayOf(41.3874, 2.1686)),
                Triple("Madrid to Tokyo", doubleArrayOf(40.4168, -3.7038), doubleArrayOf(35.6762, 139.6503)),
                Triple("London to New York", doubleArrayOf(51.5074, -0.1278), doubleArrayOf(40.7128, -74.0060))
            )
            var i by remember { mutableStateOf(0) }
            val (name, from, to) = places[i]
            val meters = remember(i) { getDistanceInMeters(from[0], from[1], to[0], to[1]) }
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    places.forEachIndexed { idx, t -> Pill(t.first, idx == i, { i = idx }) }
                }
                UtilOutput(name, "${(meters / 1000).toInt()} km")
                CodeBlock("getDistanceInMeters(\n    latitudeStart = ${from[0]}, longitudeStart = ${from[1]},\n    latitudeEnd = ${to[0]}, longitudeEnd = ${to[1]}\n)\n// ${meters.toInt()} m")
            }
        },
        UtilCard("Random", "randInt, randDouble, getRandomUUID", "Random numbers in a range and random UUIDs.") {
            var seed by remember { mutableStateOf(0) }
            val n = remember(seed) { randInt(1, 100) }
            val d = remember(seed) { randDouble(0.0, 1.0) }
            val u = remember(seed) { getRandomUUID() }
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Pill("Generate again", false, { seed++ }, height = 44.dp)
                UtilOutput("randInt(1, 100)", n.toString())
                UtilOutput("randDouble(0.0, 1.0)", d.toString().take(8))
                UtilOutput("getRandomUUID()", u)
                CodeBlock("randInt(min = 1, max = 100)\nrandDouble(min = 0.0, max = 1.0)\ngetRandomUUID()")
            }
        },
        UtilCard("Nullables", "orEmpty, orZero, orFalse, isTrue, isNull", "Null-safe defaults for strings, numbers and booleans.") {
            var present by remember { mutableStateOf(false) }
            val s: String? = if (present) "hello" else null
            val i: Int? = if (present) 7 else null
            val b: Boolean? = if (present) true else null
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) { Pill("null", !present, { present = false }, mono = true); Pill("has value", present, { present = true }) }
                UtilOutput("s.orEmpty(\"none\")", s.orEmpty("none"))
                UtilOutput("i.orZero(-1)", i.orZero(-1).toString())
                UtilOutput("b.orFalse()  /  b.isTrue()", "${b.orFalse()}  /  ${b.isTrue()}")
                UtilOutput("s.isNull()", s.isNull().toString())
                CodeBlock("val s: String? = ${if (present) "\"hello\"" else "null"}\ns.orEmpty(\"none\")\ni.orZero(-1)\nb.orFalse()\ns.isNull()")
            }
        },
        UtilCard("Functional", "Either", "A result that is either Left (failure) or Right (success), with map and getOrElse.") {
            var ok by remember { mutableStateOf(true) }
            val r: Either<String, Int> = if (ok) right(21) else left("network error")
            val doubled = r.map { it * 2 }
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) { Pill("right(21)", ok, { ok = true }, mono = true); Pill("left(\"error\")", !ok, { ok = false }, mono = true) }
                UtilOutput("map { it * 2 }", doubled.toString())
                UtilOutput("flatMap { right(it + 1) }", r.flatMap { v -> right(v + 1) }.toString())
                UtilOutput("getOrElse(0)", doubled.getOrElse(0).toString())
                CodeBlock("val r: Either<String, Int> = ${if (ok) "right(21)" else "left(\"network error\")"}\nr.map { it * 2 }.getOrElse(0)\nr.flatMap { right(it + 1) }")
            }
        }
    )
}

@Composable
fun UtilitiesTab(group: String, onGroup: (String) -> Unit, selected: String, onSelect: (String) -> Unit) {
    val groups = remember { utilities.map { it.group }.distinct() }
    val items = utilities.filter { it.group == group }
    val compact = LocalCompact.current
    FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
        groups.forEach { g -> Pill(g, g == group, { onGroup(g); onSelect(utilities.first { it.group == g }.name) }) }
    }
    FlowRow(horizontalArrangement = Arrangement.spacedBy(20.dp), verticalArrangement = Arrangement.spacedBy(20.dp)) {
        items.forEach { u ->
            Section(u.name, u.group, Modifier.widthIn(max = if (compact) 600.dp else 440.dp).fillMaxWidth()) {
                Text(u.description, color = LocalPalette.current.muted, fontSize = 13.sp, lineHeight = 19.sp)
                val uri = androidx.compose.ui.platform.LocalUriHandler.current
                Pill("API reference", false, { uri.openUri(apiUrlFor(u.name)) }, height = 36.dp)
                u.body()
            }
        }
    }
}
