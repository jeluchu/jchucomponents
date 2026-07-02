import JchuComponentsCore
import JchuComponentsSwiftUI
import SwiftUI

public struct JchuComponentsCatalogView: View {
    @Environment(\.jchuTheme) private var theme
    @State private var interactiveLoading = false
    @State private var selectedChip = false

    public init() {}

    public var body: some View {
        NavigationStack {
            List {
                Section(JchuCatalogCategory.buttons.rawValue) {
                    Text("Progress buttons")
                        .font(theme.typography.section)

                    ForEach(JchuCatalogFixtures.progressButtonStateFixtures, id: \.name) { fixture in
                        JchuProgressButton(state: fixture.state) {}
                    }
                    JchuProgressButton(
                        state: JchuProgressButtonState(
                            title: "Interactive",
                            isLoading: interactiveLoading,
                            isEnabled: true
                        )
                    ) {
                        interactiveLoading.toggle()
                    }
                }

                Section("Scenarios") {
                    ForEach(JchuCatalogFixtures.scenarios) { scenario in
                        Text(scenario.rawValue)
                            .font(theme.typography.body)
                            .foregroundStyle(theme.colors.content)
                    }
                }

                Section(JchuCatalogCategory.chips.rawValue) {
                    HStack {
                        JchuChip("Default") {}
                        JchuChip(
                            selectedChip ? "Selected" : "Not selected",
                            isSelected: selectedChip
                        ) {
                            selectedChip.toggle()
                        }
                    }
                }

                Section(JchuCatalogCategory.loaders.rawValue) {
                    JchuLoadingIndicator(label: "Loading")
                        .frame(maxWidth: .infinity)
                        .padding(.vertical, theme.spacing.dimen16)
                }

                Section(JchuCatalogCategory.progress.rawValue) {
                    JchuLinearProgress(
                        state: JchuCatalogFixtures.progressState(
                            title: "Linear",
                            value: 40
                        )
                    )

                    HStack {
                        JchuCircularProgress(
                            state: JchuCatalogFixtures.progressState(
                                title: "Circular",
                                value: 70
                            )
                        )

                        JchuCircularProgress(
                            state: JchuCatalogFixtures.progressState(
                                title: "Indeterminate",
                                value: 0,
                                isIndeterminate: true
                            )
                        )
                    }

                    JchuIconProgress(
                        state: JchuCatalogFixtures.progressState(
                            title: "Downloads",
                            value: 85
                        ),
                        systemImage: "arrow.down.circle"
                    )
                }
            }
            .scrollContentBackground(.hidden)
            .background(theme.colors.background)
            .navigationTitle("JchuComponents")
        }
    }
}

#Preview("Catalog - Light") {
    JchuComponentsCatalogView()
        .jchuTheme(JchuCatalogScenario.light.theme)
        .preferredColorScheme(.light)
}

#Preview("Catalog - Dark") {
    JchuComponentsCatalogView()
        .jchuTheme(JchuCatalogScenario.dark.theme)
        .preferredColorScheme(.dark)
}

#Preview("Catalog - Accessibility") {
    JchuComponentsCatalogView()
        .jchuTheme(JchuCatalogScenario.accessibility.theme)
        .preferredColorScheme(.light)
        .dynamicTypeSize(.accessibility2)
}
