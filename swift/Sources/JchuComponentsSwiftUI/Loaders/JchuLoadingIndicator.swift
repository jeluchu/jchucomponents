import SwiftUI

public struct JchuLoadingIndicator: View {
    private let label: LocalizedStringKey?

    public init(label: LocalizedStringKey? = nil) {
        self.label = label
    }

    public var body: some View {
        VStack(spacing: 12) {
            ProgressView()

            if let label {
                Text(label)
                    .font(.footnote)
                    .foregroundStyle(.secondary)
            }
        }
    }
}

#Preview {
    JchuLoadingIndicator(label: "Loading")
        .padding()
}
