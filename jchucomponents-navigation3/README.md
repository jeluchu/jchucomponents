# JchuComponents Navigation 3

`jchucomponents-navigation3` adds typed, generated navigation for Android
Compose apps that use AndroidX Navigation 3. Annotate composables with
`@Screen`; KSP generates serializable route keys and an entry-provider function
for each graph.

The artifact contains both the Android runtime API and the KSP processor. Use
the same dependency coordinate with `implementation` and `ksp`. It does not
depend on `jchucomponents-ui`.

## Add the dependency

Apply KSP and Kotlin Serialization in the module that contains the annotated
screens. Keep the KSP plugin version compatible with the Kotlin Gradle plugin.

```kotlin
plugins {
    alias(libs.plugins.ksp)
    alias(libs.plugins.kotlinx.serialization)
}

dependencies {
    val jchucomponentsVersion = "<published-version>"

    implementation("io.github.jeluchu:jchucomponents-navigation3:$jchucomponentsVersion")
    ksp("io.github.jeluchu:jchucomponents-navigation3:$jchucomponentsVersion")

    // AndroidX provides NavDisplay. The runtime API is exposed by the
    // jchucomponents-navigation3 dependency above.
    implementation("androidx.navigation3:navigation3-ui:<navigation3-version>")

    // Optional: only when using the ViewModel entry decorator.
    implementation("androidx.lifecycle:lifecycle-viewmodel-navigation3:<lifecycle-version>")
}
```

Use the repository configured for the published JchuComponents version. If the
app is in this repository, the equivalent project dependencies are:

```kotlin
implementation(project(":jchucomponents-navigation3"))
ksp(project(":jchucomponents-navigation3"))
```

Add the optional `jchucomponents-navigation3-di` artifact only when screens use
`@ScreenKoinViewModel`; see [ViewModel injection](#viewmodel-injection).

## Declare screens and routes

Apply `@Screen` to a top-level, non-private `@Composable` function. The `graph`
groups related destinations. The route name defaults to the composable name;
set `route` when you want a different generated name.

```kotlin
import androidx.compose.runtime.Composable
import com.jeluchu.jchucomponents.navigation3.BackStackBack
import com.jeluchu.jchucomponents.navigation3.NavigateTo
import com.jeluchu.jchucomponents.navigation3.Screen
import com.example.books.navigation.BooksRoutes

@Composable
@Screen(graph = "Books")
fun BookListRoute(
    @BackStackBack onBack: () -> Unit,
    @NavigateTo(route = BooksRoutes.BookDetailsRoute::class)
    onOpenBook: (bookId: String, title: String) -> Unit,
) {
    // Call onOpenBook(book.id, book.title) when a book is selected.
}

@Composable
@Screen(graph = "Books")
fun BookDetailsRoute(
    bookId: String,
    title: String,
    @BackStackBack onBack: () -> Unit,
) {
    // Display this book.
}
```

KSP generates `BooksRoutes` with a serializable route for each screen. Ordinary
composable parameters become route arguments. Their types must be supported by
Kotlin Serialization; annotate custom argument types with `@Serializable`.
Generated sources live under the module's `build/generated/ksp` directory and
should not be edited.

### `@Screen`

`@Screen(graph = "Books")` declares the graph for the destination. The graph
name and any custom route name must be PascalCase identifiers. A screen such as
`BookDetailsRoute` generates a route with the same name unless you override it:

```kotlin
@Screen(graph = "Books", route = "Details")
```

For each graph, the processor creates `<Graph>Routes` and a lower-camel-case
`<graph>Entries` extension. The generated package is the common package prefix
of screens in that graph, followed by `.navigation`.

### `@BackStackBack`

Marks a no-argument `() -> Unit` callback. The generated entry supplies
`{ backStack.back() }`. If the stack is already at its root, `back()` does
nothing; the generated API has no root fallback parameter.

### `@NavigateTo`

Marks a callback that navigates to another generated route, within the same
graph or across graphs. The route must be declared with `@Screen` in the same
KSP module. Pass its generated route type as a class literal:

```kotlin
@NavigateTo(route = BooksRoutes.BookDetailsRoute::class)
onOpenBook: (bookId: String, title: String) -> Unit
```

The callback's parameter types and order correspond to the destination's route
arguments. Parameter names are optional, but descriptive names help document
the meaning of repeated types such as two `String` arguments. The generated
entry constructs the target route and calls `backStack.navigateTo(...)`; the
app does not need to wire a navigation lambda for it.

### `@CustomAction`

Marks an application-defined callback that is not a generated route transition,
such as sharing content, opening a platform destination, or invoking
application-specific behavior. The generated entries extension exposes each
distinct custom callback as a required parameter:

```kotlin
@Composable
@Screen(graph = "Books")
fun BookDetailsRoute(
    bookId: String,
    @BackStackBack onBack: () -> Unit,
    @CustomAction onShareBook: (bookId: String) -> Unit,
) { /* ... */ }
```

The app supplies it when registering the graph:

```kotlin
booksEntries(
    backStack = backStack,
    onShareBook = { bookId -> shareBook(bookId) },
)
```

If several screens in one graph use a custom action with the same name, they
must declare the same callback type.

## Register the generated entries

Create one root back stack and one `NavDisplay` for the app. Register each
generated entries extension in the same `entryProvider` and pass it the shared
back stack:

```kotlin
val backStack = rememberNavBackStack(BooksRoutes.BookListRoute)

NavDisplay(
    backStack = backStack,
    onBack = { backStack.back() },
    entryProvider = entryProvider {
        booksEntries(backStack = backStack)
        accountEntries(
            backStack = backStack,
            onOpenSystemSettings = { openSystemSettings() },
        )
    },
)
```

`@NavigateTo` handles generated route transitions, including cross-graph routes.
The app still owns the root `NavDisplay`, the shared back stack, and callbacks
marked with `@CustomAction`.

## ViewModel injection

Koin integration is optional. Add the DI artifact and annotate a ViewModel
parameter with `@ScreenKoinViewModel`:

```kotlin
implementation("io.github.jeluchu:jchucomponents-navigation3-di:<published-version>")
```

```kotlin
@Composable
@Screen(graph = "Anime")
fun AnimeSearchRoute(
    @ScreenKoinViewModel viewModel: AnimeSearchViewModel,
) { /* ... */ }
```

The generated entry calls `inject()` when the route has no arguments and
`injectByParams(key)` when it does. Define the ViewModel binding in the app's
Koin modules. A ViewModel parameter annotated with `@ScreenKoinViewModel` is
not included in the route key.

## Generated API and scope

For `@Screen(graph = "Books")`, generated code includes:

- `BooksRoutes`, a serializable `NavKey` type containing the graph's route
  objects and argument data classes.
- `booksEntries(backStack, ...)`, which registers destinations and wires back,
  route-navigation, custom-action, and optional Koin callbacks.

All screens and cross-graph route targets must be visible to the KSP processor
in the same compilation module. For a complete app example, see the
[Navigation 3 code generation guide](../docs/navigation-codegen.md) and the
[Android catalog app](../app).
