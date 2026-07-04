import org.jlleitschuh.gradle.ktlint.reporter.ReporterType

plugins {
    id("org.jlleitschuh.gradle.ktlint")
}

ktlint {
    ignoreFailures.set(true)
    outputToConsole.set(false)
    verbose.set(false)

    reporters {
        reporter(ReporterType.PLAIN)
        reporter(ReporterType.HTML)
    }

    filter {
        exclude { fileTreeElement ->
            val path = fileTreeElement.file.invariantSeparatorsPath
            path.contains("/build/") ||
                path.contains("/generated/")
        }
    }
}
