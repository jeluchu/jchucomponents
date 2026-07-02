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

    func testNativeSwiftExtensionsUseJchuNamespace() {
        XCTAssertNil("  \n".jchu.nilIfBlank)
        XCTAssertEqual("áéí".jchu.removingDiacritics, "aei")
        XCTAssertEqual("JchuComponents".jchu.truncated(to: 4), "Jchu…")
        XCTAssertEqual([1, 2].jchu[safe: 1], 2)
        XCTAssertNil([1, 2].jchu[safe: 4])
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
