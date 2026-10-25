# JchuComponents PDF reader

`jchucomponents-pdf` is an Android Compose reader for remote PDF URLs. It
downloads the document with `jchucomponents-network`, renders pages with
`PdfRenderer`, and provides page navigation, zoom, rotation and sharing.
The native iOS equivalent is `JchuPdfReaderView` in the
`JchuComponentsSwiftUI` Swift package product.

The library applies `@Screen` to its reader and packages the generated route and
Navigation 3 entry provider. Apps only register the provider and navigate to the
generated route:

```kotlin
dependencies {
    implementation("com.github.jeluchu.jchucomponents:jchucomponents-pdf:<version>")
}
```

```kotlin
import com.jeluchu.jchucomponents.pdf.navigation.PdfReaderRoutes
import com.jeluchu.jchucomponents.pdf.navigation.pdfReaderEntries

entryProvider {
    pdfReaderEntries(backStack)
}

backStack.add(PdfReaderRoutes.Reader(url = remotePdfUrl))
```

The generated provider handles the back callback. The module uses an isolated
FileProvider authority for sharing cached PDFs, so no app-specific provider
declaration is needed.
