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
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
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
import com.jeluchu.jchucomponents.foundation.version.compareVersions

/** One utility card: a group, a name for search and deep links, and a live preview. */
class UtilCard(val group: String, val name: String, val description: String, val body: @Composable () -> Unit)

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
                UtilOutput("getOrElse(0)", doubled.getOrElse(0).toString())
                CodeBlock("val r: Either<String, Int> = ${if (ok) "right(21)" else "left(\"network error\")"}\nr.map { it * 2 }.getOrElse(0)")
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
                u.body()
            }
        }
    }
}
