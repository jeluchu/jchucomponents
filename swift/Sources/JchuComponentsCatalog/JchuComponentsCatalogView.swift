import JchuComponentsCore
import JchuComponentsSwiftUI
import SwiftUI

public struct JchuComponentsCatalogView: View {
    @State private var interactiveLoading = false
    @State private var selectedChip = false

    public init() {}

    public var body: some View {
        NavigationStack {
            List {
                Section("Buttons") {
                    JchuProgressButton(
                        state: JchuProgressButtonState(
                            title: "Normal",
                            isLoading: false,
                            isEnabled: true
                        )
                    ) {}
                    JchuProgressButton(
                        state: JchuProgressButtonState(
                            title: "Loading",
                            isLoading: true,
                            isEnabled: true
                        )
                    ) {}
                    JchuProgressButton(
                        state: JchuProgressButtonState(
                            title: "Disabled",
                            isLoading: false,
                            isEnabled: false
                        )
                    ) {}
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

                Section("Chips") {
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

                Section("Loading") {
                    JchuLoadingIndicator(label: "Loading")
                        .frame(maxWidth: .infinity)
                }

                Section("Progress") {
                    JchuLinearProgress(
                        state: progressState(
                            title: "Linear",
                            value: 40
                        )
                    )

                    HStack {
                        JchuCircularProgress(
                            state: progressState(
                                title: "Circular",
                                value: 70
                            )
                        )

                        JchuCircularProgress(
                            state: progressState(
                                title: "Indeterminate",
                                value: 0,
                                isIndeterminate: true
                            )
                        )
                    }

                    JchuIconProgress(
                        state: progressState(
                            title: "Downloads",
                            value: 85
                        ),
                        systemImage: "arrow.down.circle"
                    )
                }
            }
            .navigationTitle("JchuComponents")
        }
    }

    private func progressState(
        title: String,
        value: Double,
        isIndeterminate: Bool = false
    ) -> JchuProgressState {
        JchuProgressState(
            title: title,
            value: value,
            maxValue: 100,
            isEnabled: true,
            isIndeterminate: isIndeterminate
        )
    }
}

#Preview {
    JchuComponentsCatalogView()
}
