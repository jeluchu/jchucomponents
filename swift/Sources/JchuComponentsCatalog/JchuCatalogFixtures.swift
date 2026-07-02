import JchuComponentsCore
import JchuComponentsSwiftUI
import SwiftUI

enum JchuCatalogCategory: String, CaseIterable, Identifiable {
    case buttons = "Buttons"
    case cards = "Cards"
    case chips = "Chips"
    case lists = "Lists"
    case loaders = "Loaders"
    case progress = "Progress"
    case toolbars = "Toolbars"

    var id: String { rawValue }
}

enum JchuCatalogFixtureKind: String {
    case enabled
    case disabled
    case loading
    case error
    case longContent
}

struct JchuCatalogStateFixture<State> {
    let name: String
    let kind: JchuCatalogFixtureKind
    let state: State
}

enum JchuCatalogScenario: String, CaseIterable, Identifiable {
    case light = "Light"
    case dark = "Dark"
    case accessibility = "Accessibility"

    var id: String { rawValue }

    var theme: JchuTheme {
        switch self {
        case .light:
            return .standard
        case .dark:
            return JchuTheme(
                colors: JchuColors(
                    background: Color(red: 0.06, green: 0.08, blue: 0.07),
                    surface: Color(red: 0.11, green: 0.13, blue: 0.12),
                    primary: Color(red: 0.84, green: 0.95, blue: 0.87),
                    content: Color(red: 0.95, green: 0.97, blue: 0.94),
                    contentSecondary: Color(red: 0.75, green: 0.8, blue: 0.74),
                    error: Color(red: 1, green: 0.71, blue: 0.67)
                )
            )
        case .accessibility:
            return JchuTheme(
                colors: JchuColors(
                    background: .white,
                    surface: .white,
                    primary: .black,
                    content: .black,
                    contentSecondary: .black,
                    error: Color(red: 0.55, green: 0.11, blue: 0.09)
                ),
                spacing: JchuSpacing(dimen16: 20, dimen24: 30, dimen32: 40),
                shapes: JchuShapes(corner16: 12)
            )
        }
    }

    var colorScheme: ColorScheme {
        self == .dark ? .dark : .light
    }

    var dynamicTypeSize: DynamicTypeSize {
        self == .accessibility ? .accessibility2 : .large
    }
}

@MainActor
enum JchuCatalogFixtures {
    static let longContent =
        "JchuComponents catalog fixture with enough content to validate wrapping and scaling."

    static let scenarios = JchuCatalogScenario.allCases

    static let progressButtonStateFixtures = [
        JchuCatalogStateFixture(
            name: "Enabled",
            kind: .enabled,
            state: JchuProgressButtonState(title: "Normal", isLoading: false, isEnabled: true)
        ),
        JchuCatalogStateFixture(
            name: "Loading",
            kind: .loading,
            state: JchuProgressButtonState(title: "Loading", isLoading: true, isEnabled: true)
        ),
        JchuCatalogStateFixture(
            name: "Disabled",
            kind: .disabled,
            state: JchuProgressButtonState(title: "Disabled", isLoading: false, isEnabled: false)
        ),
        JchuCatalogStateFixture(
            name: "Error",
            kind: .error,
            state: JchuProgressButtonState(title: "Error", isLoading: false, isEnabled: true)
        ),
        JchuCatalogStateFixture(
            name: "Long content",
            kind: .longContent,
            state: JchuProgressButtonState(title: longContent, isLoading: false, isEnabled: true)
        ),
    ]

    static let progressButtonStates = progressButtonStateFixtures.map(\.state)

    static let progressStateFixtures = [
        JchuCatalogStateFixture(
            name: "Enabled",
            kind: .enabled,
            state: progressState(title: "Linear", value: 40)
        ),
        JchuCatalogStateFixture(
            name: "Disabled",
            kind: .disabled,
            state: progressState(title: "Disabled", value: 40, isEnabled: false)
        ),
        JchuCatalogStateFixture(
            name: "Loading",
            kind: .loading,
            state: progressState(title: "Indeterminate", value: 0, isIndeterminate: true)
        ),
        JchuCatalogStateFixture(
            name: "Error",
            kind: .error,
            state: progressState(title: "Error", value: 0, isEnabled: false)
        ),
        JchuCatalogStateFixture(
            name: "Long content",
            kind: .longContent,
            state: progressState(title: longContent, value: 65)
        ),
    ]

    static func progressState(
        title: String,
        value: Double,
        isEnabled: Bool = true,
        isIndeterminate: Bool = false
    ) -> JchuProgressState {
        JchuProgressState(
            title: title,
            value: value,
            maxValue: 100,
            isEnabled: isEnabled,
            isIndeterminate: isIndeterminate
        )
    }
}
