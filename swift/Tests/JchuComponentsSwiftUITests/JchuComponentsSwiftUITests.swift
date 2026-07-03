import JchuComponentsCore
import JchuComponentsExtensions
@testable import JchuComponentsSwiftUI
import XCTest

final class JchuComponentsSwiftUITests: XCTestCase {
    func testPackageExposesKotlinCoreVersion() {
        XCTAssertEqual(JchuComponentsInfo.version, "3.0.0-alpha03")
        XCTAssertEqual(JchuComponents.shared.VERSION, "3.0.0-alpha03")
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
    }

    func testSwiftUIThemeCanBeCustomized() {
        let theme = JchuTheme(
            spacing: JchuSpacing(dimen16: 18),
            shapes: JchuShapes(corner16: 20),
            motion: JchuMotion(durationMedium: 0.3)
        )

        XCTAssertEqual(theme.spacing.dimen16, 18)
        XCTAssertEqual(theme.shapes.corner16, 20)
        XCTAssertEqual(theme.motion.durationMedium, 0.3)
    }

    func testSwiftUIThemeSupportsAccessibilityScaleTokens() {
        let theme = JchuTheme(
            colors: JchuColors(contentSecondary: .primary),
            spacing: JchuSpacing(dimen16: 20, dimen24: 30)
        )

        XCTAssertEqual(theme.spacing.dimen16, 20)
        XCTAssertEqual(theme.spacing.dimen24, 30)
    }
}
