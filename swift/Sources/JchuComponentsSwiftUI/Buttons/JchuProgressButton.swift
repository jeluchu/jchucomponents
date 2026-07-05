import JchuComponentsCore
import SwiftUI

/// A prominent button that replaces its title with a progress indicator while
/// work is in progress.
///
/// The button disables itself while `isLoading` is `true`, preventing duplicate
/// actions without requiring additional state in the caller.
public struct JchuProgressButton: View {
    private let title: LocalizedStringKey
    private let isLoading: Bool
    private let isEnabled: Bool
    private let action: () -> Void

    /// Creates a progress button from native Swift values.
    ///
    /// - Parameters:
    ///   - title: The localized button title.
    ///   - isLoading: Whether to show progress and disable interaction.
    ///   - isEnabled: Whether the button accepts interaction.
    ///   - action: The action invoked when the user activates the button.
    public init(
        _ title: LocalizedStringKey,
        isLoading: Bool = false,
        isEnabled: Bool = true,
        action: @escaping () -> Void
    ) {
        self.title = title
        self.isLoading = isLoading
        self.isEnabled = isEnabled
        self.action = action
    }

    /// Creates a progress button from shared Kotlin Multiplatform state.
    ///
    /// - Parameters:
    ///   - state: The shared title, loading and enabled state.
    ///   - action: The action invoked when the user activates the button.
    public init(
        state: JchuProgressButtonState,
        action: @escaping () -> Void
    ) {
        self.title = LocalizedStringKey(state.title)
        self.isLoading = state.isLoading
        self.isEnabled = state.isEnabled
        self.action = action
    }

    public var body: some View {
        Button(action: action) {
            ZStack {
                Text(title)
                    .opacity(isLoading ? 0 : 1)

                if isLoading {
                    ProgressView()
                        .tint(.white)
                }
            }
            .frame(maxWidth: .infinity)
        }
        .buttonStyle(.borderedProminent)
        .controlSize(.large)
        .disabled(!isEnabled || isLoading)
        .accessibilityValue(isLoading ? Text("Loading") : Text(""))
    }
}

#Preview {
    VStack {
        JchuProgressButton("Continue") {}
        JchuProgressButton("Continue", isLoading: true) {}
        JchuProgressButton("Continue", isEnabled: false) {}
    }
    .padding()
}
