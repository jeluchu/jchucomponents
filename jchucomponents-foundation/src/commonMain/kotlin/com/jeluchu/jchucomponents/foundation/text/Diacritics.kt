package com.jeluchu.jchucomponents.foundation.text

/**
 * Removes accents and other combining marks: `"Canción ñandú"` becomes `"Cancion nandu"`. Characters that are not
 * a base letter plus a mark (for example `ß` or Cyrillic) are kept. Useful to search text ignoring accents.
 */
expect fun String.removeDiacritics(): String
