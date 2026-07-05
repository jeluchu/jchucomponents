import JchuComponentsCore
import JchuComponentsExtensions
@testable import JchuComponentsSwiftUI
import SwiftUI
import XCTest

final class JchuComponentsSwiftUITests: XCTestCase {
    func testPackageExposesKotlinCoreVersion() {
        XCTAssertEqual(JchuComponentsInfo.version, "3.0.0-alpha07")
        XCTAssertEqual(JchuComponents.shared.VERSION, "3.0.0-alpha07")
    }

    func testSharedProgressButtonStateUsesExpectedDefaults() {
        let state = JchuProgressButtonState(
            title: "Continue",
            isLoading: false,
            isEnabled: true
        )

        XCTAssertEqual(state.title, "Continue")
        XCTAssertFalse(state.isLoading)
        XCTAssertTrue(state.isEnabled)
    }

    func testSharedProgressStateClampsFraction() {
        let state = JchuProgressState(
            title: "Progress",
            value: 120,
            maxValue: 100,
            isEnabled: true,
            isIndeterminate: false
        )

        XCTAssertEqual(state.fraction, 1)
    }

    @MainActor
    func testProgressViewsExposeNativeSwiftInitializers() {
        _ = JchuProgressButton("Continue") {}
        _ = JchuProgressButton(
            state: JchuProgressButtonState(
                title: "Continue",
                isLoading: true,
                isEnabled: true
            )
        ) {}
        _ = JchuChip("Selected", isSelected: true) {}
        _ = JchuLoadingIndicator(label: "Loading")
        _ = JchuLinearProgress("Downloading", value: 45, maxValue: 100)
        _ = JchuCircularProgress("Preparing", isIndeterminate: true)
        _ = JchuIconProgress(
            "Uploading",
            systemImage: "icloud.and.arrow.up",
            value: 72,
            maxValue: 100
        )
    }

    @MainActor
    func testInputViewsExposeControlledAndUncontrolledInitializers() {
        var query = ""
        var isExpanded = false
        var notes = ""

        _ = JchuExpandableSearch(
            query: Binding(get: { query }, set: { query = $0 }),
            defaults: SearchBarDefaults(label: "Search")
        )
        _ = JchuExpandableSearch(
            query: Binding(get: { query }, set: { query = $0 }),
            isExpanded: Binding(
                get: { isExpanded },
                set: { isExpanded = $0 }
            ),
            defaults: SearchBarDefaults(
                label: "Search",
                initiallyExpanded: true
            )
        )
        _ = JchuGrowingTextField(
            value: Binding(get: { notes }, set: { notes = $0 }),
            defaults: GrowingTextFieldDefaults(
                label: "Notes",
                minLines: 2,
                maxLines: 4,
                maxCharacters: 200
            )
        )
    }

    func testNativeSwiftStringExtensionsCanBeUsedDirectly() {
        XCTAssertEqual(String.empty, "")
        XCTAssertNil("  \n".nilIfBlank)
        XCTAssertNil("".nilIfEmpty)
        XCTAssertEqual("a\nb\rc".withoutNewlines, "abc")
        XCTAssertEqual("áéí".removingDiacritics, "aei")
        XCTAssertEqual("JchuComponents".truncated(to: 4), "Jchu…")
        XCTAssertEqual("123456789".grouped(every: 3), "123 456 789")
        XCTAssertEqual("12345678".grouped(every: 4), "1234 5678")
        XCTAssertEqual("hello-world".replacingDashesWithSpaces(), "hello world")
        XCTAssertEqual("jchu".capitalizingFirstLetter(locale: Locale(identifier: "en_US")), "Jchu")
        XCTAssertEqual([1, 2].jchu[safe: 1], 2)
        XCTAssertNil([1, 2].jchu[safe: 4])
    }

    func testNativeSwiftStringParsingHelpers() {
        XCTAssertEqual("A1 B2-C3".onlyDigits, "123")
        XCTAssertTrue("abc".containsLetters)
        XCTAssertTrue("abc123".containsNumbers)
        XCTAssertTrue("12345".isNumeric)
        XCTAssertTrue("abcXYZ".isAlphabetic)
        XCTAssertTrue("abc123".isAlphanumeric)
        XCTAssertFalse("abc-123".isAlphanumeric)
        XCTAssertTrue("hello@example.com".isValidEmail)
        XCTAssertFalse("hello@example".isValidEmail)
        XCTAssertTrue("192.168.1.1".isValidIPv4)
        XCTAssertFalse("192.168.1.300".isValidIPv4)
        XCTAssertEqual("one two\nthree".wordCount, 3)
    }

    func testNativeSwiftStringEncodingAndURLHelpers() {
        XCTAssertEqual("Jchu".base64Encoded, "SmNodQ==")
        XCTAssertEqual("SmNodQ==".base64Decoded, "Jchu")
        XCTAssertNil("***".base64Decoded)
        XCTAssertEqual("https://example.com/assets/logo.png?size=small".lastPathComponentFromURL, "logo.png")
        XCTAssertEqual("http://example.com".httpsURLString, "https://example.com")
        XCTAssertEqual("https://example.com".httpsURLString, "https://example.com")
        XCTAssertEqual("banana".removing("na"), "ba")
        XCTAssertEqual("123456789".formatInGroups(groupSize: 3), "123-456-789")
        XCTAssertEqual("banana".replacingFirst("na", with: "NA"), "baNAna")
        XCTAssertTrue("https://example.com/path".isHTTPURL)
        XCTAssertTrue("https://example.com/path".isValidURL)
        XCTAssertFalse("example.com".isValidURL)
    }

    func testNativeSwiftDataAndSequenceExtensions() {
        let pngHeader = Data([0x89, 0x50, 0x4E, 0x47, 0x0D, 0x0A, 0x1A, 0x0A])
        XCTAssertEqual(pngHeader.detectedImageFileExtension, "png")
        XCTAssertEqual(pngHeader.detectedImageMIMEType, "image/png")
        XCTAssertTrue(pngHeader.dataURI(mimeType: "image/png").hasPrefix("data:image/png;base64,"))
        XCTAssertEqual(Array(1...5).chunked(into: 2), [[1, 2], [3, 4], [5]])
        XCTAssertEqual(Array(1...3).chunked(into: 0), [])
    }

    func testNativeSwiftAsyncSequenceObservation() async {
        var values: [Int] = []
        await AsyncStream { continuation in
            continuation.yield(1)
            continuation.yield(2)
            continuation.finish()
        }.observe { value in
            values.append(value)
        }
        XCTAssertEqual(values, [1, 2])
    }

    func testNativeSwiftBooleanExtensions() {
        let enabled: Bool? = true
        let disabled: Bool? = false
        let unknown: Bool? = nil

        XCTAssertTrue(enabled.isTrue)
        XCTAssertFalse(disabled.isTrue)
        XCTAssertFalse(unknown.isTrue)
        XCTAssertFalse(unknown.orFalse())
        XCTAssertTrue(unknown.orFalse(defaultValue: true))
    }

    func testNativeSwiftNumericExtensions() {
        XCTAssertEqual(Int.empty, 0)
        XCTAssertFalse(0.isNotEmpty)
        XCTAssertTrue(12.isPositive)
        XCTAssertTrue(0.isPositiveOrZero)
        XCTAssertTrue((-2).isNegative)
        XCTAssertTrue(0.isNegativeOrZero)
        XCTAssertEqual(Optional<Int>.none.orEmpty(), 0)
        XCTAssertEqual(Optional<Double>.none.orEmpty(), 0.0)
        XCTAssertEqual(Optional<Float>.none.orEmpty(), 0.0)
        XCTAssertEqual(Optional<Int64>.none.orEmpty(), 0)
        XCTAssertEqual(Int64(5_242_880).bytesToMegabytes, "5")
        XCTAssertEqual(125_000.millisecondsToTimer, "2:05")
        XCTAssertEqual(3_725_000.millisecondsToTimer, "1:2:05")
        XCTAssertEqual(Optional<Int>.none.roundedUpToNearestTen, 10)
        XCTAssertEqual(14.roundedUpToNearestTen, 20)
        XCTAssertEqual(1_234_567.thousandsFormatted, "1.234.567")
    }

    func testNativeSwiftCollectionExtensions() {
        var values = [1, 2]

        values.addAllIfNotExist([2, 3, 4])

        XCTAssertEqual(values, [1, 2, 3, 4])
        XCTAssertEqual(["Jchu", "Components", "iOS"].concatenateLowercase(), "jchucomponentsios")
    }

    func testNativeSwiftCodableJsonExtensions() {
        struct Fixture: Codable, Equatable {
            let name: String
            let count: Int
        }

        let fixture = Fixture(name: "Jchu", count: 3)
        let json = fixture.toJson()

        XCTAssertNotNil(json)
        XCTAssertEqual(json?.fromJson(Fixture.self), fixture)
        XCTAssertNil("".fromJson(Fixture.self))
        XCTAssertNil("{".fromJson(Fixture.self))
    }

    func testNativeSwiftDateFormattingExtensions() {
        let date = Date(timeIntervalSince1970: 1_720_126_920)
        let locale = Locale(identifier: "en_US_POSIX")
        let timeZone = TimeZone(secondsFromGMT: 0)!

        XCTAssertEqual(date.format(locale: locale, timeZone: timeZone), "04/07/2024")
        XCTAssertEqual(date.formatWithTime(locale: locale, timeZone: timeZone), "04/07/2024 21:02")
        XCTAssertEqual(date.formatOnlyTime(locale: locale, timeZone: timeZone), "21:02 H")
        XCTAssertEqual(date.formatToServerDateTimeDefaults(locale: locale, timeZone: timeZone), "2024-07-04 21:02:00")
        XCTAssertEqual(date.formatToServerDateDefaults(locale: locale, timeZone: timeZone), "2024-07-04")
        XCTAssertEqual(date.formatToTruncatedDateTime(locale: locale, timeZone: timeZone), "20240704210200")
        XCTAssertEqual(date.formatToServerTimeDefaults(locale: locale, timeZone: timeZone), "21:02:00")
        XCTAssertEqual(date.formatToViewDateTimeDefaults(locale: locale, timeZone: timeZone), "04/07/2024 21:02:00")
        XCTAssertEqual(date.formatToViewDateDefaults(locale: locale, timeZone: timeZone), "04/07/2024")
        XCTAssertEqual(date.formatToViewTimeDefaults(locale: locale, timeZone: timeZone), "21:02:00")
        XCTAssertEqual(date.toAccessibilityDateMMMMYYYY(locale: locale, timeZone: timeZone), "July 2024")
        XCTAssertEqual(getDateTime(currentMillis: "1720126920000", locale: locale, timeZone: timeZone), "04 Jul 2024")
        XCTAssertNil(getDateTime(currentMillis: "not-a-date", locale: locale, timeZone: timeZone))
    }

    func testNativeSwiftDateArithmeticExtensions() {
        var calendar = Calendar(identifier: .gregorian)
        calendar.timeZone = TimeZone(secondsFromGMT: 0)!

        let date = Date(timeIntervalSince1970: 1_720_126_920)

        XCTAssertEqual(date.addYears(1, calendar: calendar).formatToServerDateDefaults(timeZone: calendar.timeZone), "2025-07-04")
        XCTAssertEqual(date.addMonths(1, calendar: calendar).formatToServerDateDefaults(timeZone: calendar.timeZone), "2024-08-04")
        XCTAssertEqual(date.addDays(2, calendar: calendar).formatToServerDateDefaults(timeZone: calendar.timeZone), "2024-07-06")
        XCTAssertEqual(date.addHours(3, calendar: calendar).formatToServerTimeDefaults(timeZone: calendar.timeZone), "00:02:00")
        XCTAssertEqual(date.addMinutes(15, calendar: calendar).formatToServerTimeDefaults(timeZone: calendar.timeZone), "21:17:00")
        XCTAssertEqual(date.addSeconds(30, calendar: calendar).formatToServerTimeDefaults(timeZone: calendar.timeZone), "21:02:30")
        XCTAssertEqual(date.minusDays(3, calendar: calendar).formatToServerDateDefaults(timeZone: calendar.timeZone), "2024-07-01")
        XCTAssertEqual(date.backInYears(2, calendar: calendar).formatToServerDateDefaults(timeZone: calendar.timeZone), "2022-07-04")
        XCTAssertEqual(date.forwardInYears(2, calendar: calendar).formatToServerDateDefaults(timeZone: calendar.timeZone), "2026-07-04")
        XCTAssertEqual(date.firstHourOfTheDay(calendar: calendar).formatToServerDateTimeDefaults(timeZone: calendar.timeZone), "2024-07-04 00:00:00")
        XCTAssertEqual(date.firstDayOfTheMonth(calendar: calendar).formatToServerDateTimeDefaults(timeZone: calendar.timeZone), "2024-07-01 00:00:00")
        XCTAssertEqual(date.dayEnd(calendar: calendar).formatToServerDateTimeDefaults(timeZone: calendar.timeZone), "2024-07-04 23:59:59")
        XCTAssertEqual(date.diffInDays(to: date.addDays(5, calendar: calendar), calendar: calendar), 5)
        XCTAssertEqual(date.getDateTime(daysNumber: 4, calendar: calendar).formatToServerDateDefaults(timeZone: calendar.timeZone), "2024-06-30")
    }

    func testNativeSwiftDateRefreshChecks() {
        var calendar = Calendar(identifier: .gregorian)
        calendar.timeZone = TimeZone(secondsFromGMT: 0)!

        let nowDate = Date(timeIntervalSince1970: 1_720_126_920)
        let tenMinutesBefore = nowDate.timeIntervalSince1970 * 1000 - 10 * 60 * 1000
        let oneHourBefore = nowDate.timeIntervalSince1970 * 1000 - 60 * 60 * 1000
        let previousDay = nowDate.addDays(-1, calendar: calendar).timeIntervalSince1970 * 1000

        XCTAssertTrue(isFetchFiveMinutes(lastFetchTime: tenMinutesBefore, now: nowDate))
        XCTAssertFalse(isFetchThirtyMinutes(lastFetchTime: tenMinutesBefore, now: nowDate))
        XCTAssertTrue(isFetchThirtyMinutes(lastFetchTime: oneHourBefore, now: nowDate))
        XCTAssertTrue(isNextDay(lastFetchTime: previousDay, now: nowDate, calendar: calendar))
        XCTAssertTrue(nowDate.isAfterOrEqualThan(numberDaysBeforeToday: 1, now: nowDate, calendar: calendar))
        XCTAssertFalse(nowDate.addDays(-3, calendar: calendar).isAfterOrEqualThan(numberDaysBeforeToday: 1, now: nowDate, calendar: calendar))
        XCTAssertTrue(nowDate.addDays(-3, calendar: calendar).isBeforeThan(numberDaysBeforeToday: 1, now: nowDate, calendar: calendar))
        XCTAssertEqual(65.durationText, "01:05")
        XCTAssertEqual(3665.durationText, "1:01:05")
    }

    func testSwiftUIThemeCanBeCustomized() {
        let theme = JchuTheme(
            spacing: JchuSpacing(dimen16: 18),
            shapes: JchuShapes(corner16: 20, corner999: 500),
            motion: JchuMotion(durationMedium: 0.3)
        )

        XCTAssertEqual(theme.spacing.dimen16, 18)
        XCTAssertEqual(theme.shapes.corner16, 20)
        XCTAssertEqual(theme.shapes.corner999, 500)
        XCTAssertEqual(theme.motion.durationMedium, 0.3)
    }

    func testSwiftUIThemeSupportsAccessibilityScaleTokens() {
        let theme = JchuTheme(
            colors: JchuColors(contentSecondary: .primary),
            spacing: JchuSpacing(dimen16: 20, dimen24: 30, dimen1250: 1300)
        )

        XCTAssertEqual(theme.spacing.dimen16, 20)
        XCTAssertEqual(theme.spacing.dimen24, 30)
        XCTAssertEqual(theme.spacing.dimen00, 0)
        XCTAssertEqual(theme.spacing.dimen1250, 1300)
        XCTAssertEqual(theme.shapes.corner00, 0)
        XCTAssertEqual(theme.shapes.corner100, 100)
    }

    @MainActor
    func testSwiftUIThemeExposesStableDefaultsAndModifier() {
        let theme = JchuTheme.standard

        XCTAssertEqual(theme.spacing.dimen16, 16)
        XCTAssertEqual(theme.spacing.dimen24, 24)
        XCTAssertEqual(theme.shapes.corner16, 16)
        XCTAssertEqual(theme.shapes.corner999, 999)
        XCTAssertEqual(theme.motion.durationShort, 0.15)
        XCTAssertEqual(theme.motion.durationMedium, 0.25)
        XCTAssertEqual(theme.motion.durationLong, 0.4)

        _ = EmptyView().jchuTheme(theme)
    }

    @MainActor
    func testNetworkImageCanBeCreatedFromStringURL() {
        let configuration = JchuNetworkImageConfiguration.galleryThumbnail(
            size: CGSize(width: 160, height: 90),
            cornerRadius: 14
        )
        let view = JchuNetworkImage(
            urlString: "https://example.com/image.jpg",
            configuration: configuration
        )

        XCTAssertNotNil(view)
        XCTAssertEqual(configuration.contentMode, .fill)
        XCTAssertEqual(configuration.cornerRadius, 14)
        XCTAssertEqual(configuration.targetSize, CGSize(width: 160, height: 90))
        XCTAssertEqual(configuration.retryCount, 2)

        _ = JchuNetworkImagePlaceholder()
        _ = JchuNetworkImageErrorView()
    }

    @MainActor
    func testNetworkImagePrefetcherAcceptsEmptyInputs() {
        let prefetcher = JchuNetworkImagePrefetcher()

        prefetcher.prefetch(urls: [])
        prefetcher.prefetch(urlStrings: ["not a URL"])
        prefetcher.stop()
    }

    @MainActor
    func testUIImageResizePreservesAspectRatio() {
        let image = UIGraphicsImageRenderer(
            size: CGSize(width: 200, height: 100)
        ).image { context in
            UIColor.red.setFill()
            context.fill(CGRect(x: 0, y: 0, width: 200, height: 100))
        }

        let resized = image.resizedToFit(maxDimension: 50)

        XCTAssertEqual(resized.size.width, 50, accuracy: 0.01)
        XCTAssertEqual(resized.size.height, 25, accuracy: 0.01)
        XCTAssertNotNil(
            resized.optimizedJPEG(
                maxDimension: 50,
                maximumByteCount: 50_000
            )
        )
    }

    @MainActor
    func testGeneralScaffoldsExposeConvenienceInitializers() {
        _ = JchuScaffold {
            Text("Content")
        }
        _ = JchuScrollableScaffold("Library") {
            Text("Scrollable content")
        }
        _ = JchuStateScaffold(
            "Library",
            isLoading: false,
            isEmpty: false
        ) {
            Text("Loaded")
        }
        _ = JchuSettingsScaffold("Settings") {
            Text("Preferences")
        }
        _ = JchuDetailsScaffold("Details", details: "Loaded") { value in
            Text(value)
        }
        _ = JchuRemoteScreenContent(
            data: "Loaded",
            isLoading: false,
            error: nil
        ) {
            ProgressView()
        } successContent: { value in
            Text(value)
        } failureContent: { error in
            Text(error ?? "Error")
        }
    }
}
