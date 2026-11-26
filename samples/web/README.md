# JchuComponents web catalog

A Compose Multiplatform (Kotlin/Wasm) app that runs the real library in the browser: components, extensions and
date helpers, each with live controls and the code to use it. It is published to GitHub Pages under `/catalog/`
by `.github/workflows/docs.yml`, next to the Dokka API reference.

## Run it locally

```bash
./gradlew :samples:web:wasmJsBrowserDevelopmentRun
```

Build the production bundle with `./gradlew :samples:web:wasmJsBrowserDistribution`; it lands in
`samples/web/build/dist/wasmJs/productionExecutable`.

Requires a browser with WebAssembly GC (Chrome and Edge 119+, Firefox 120+, Safari 18.2+).

## Features

- **Share a link:** the URL hash follows what you see, for example `#components/Chips/Tag`, `#extensions` or `#dates`.
- **Search:** press `/` to focus the search box, arrows and Enter to pick a result, Esc to close. Spanish keywords work too.
- **Copy:** every code block has a Copy button; each demo has View source and Copy link.
- **Light and dark** themes, and a layout that adapts to phones.

## Adding a demo

Add a `Demo(family, name, tag, description, code) { preview }` entry to `demos` in `Components.kt`, and its source file to
`sourceFiles` so *View source* points at the right place. Extensions and dates live in `Extensions.kt` and `Dates.kt`.
