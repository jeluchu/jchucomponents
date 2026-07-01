import JchuComponentsCore
import SwiftUI

public struct JchuProgressButton: View {
    private let title: LocalizedStringKey
    private let isLoading: Bool
    private let isEnabled: Bool
    private let action: () -> Void

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
