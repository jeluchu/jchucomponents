import JchuComponentsCore
import SwiftUI

public struct JchuLinearProgress: View {
    private let state: JchuProgressState

    public init(state: JchuProgressState) {
        self.state = state
    }

    public var body: some View {
        VStack(alignment: .leading, spacing: 8) {
            Text(state.title)
                .font(.subheadline)

            if state.isIndeterminate {
                ProgressView()
            } else {
                ProgressView(value: state.fraction)
            }
        }
        .disabled(!state.isEnabled)
        .opacity(state.isEnabled ? 1 : 0.45)
    }
}

public struct JchuCircularProgress: View {
    private let state: JchuProgressState

    public init(state: JchuProgressState) {
        self.state = state
    }

    public var body: some View {
        VStack(spacing: 8) {
            if state.isIndeterminate {
                ProgressView()
                    .controlSize(.large)
            } else {
                ProgressView(value: state.fraction)
                    .progressViewStyle(.circular)
                    .controlSize(.large)
            }

            Text(state.title)
                .font(.caption)
        }
        .disabled(!state.isEnabled)
        .opacity(state.isEnabled ? 1 : 0.45)
    }
}

public struct JchuIconProgress: View {
    private let state: JchuProgressState
    private let systemImage: String

    public init(
        state: JchuProgressState,
        systemImage: String
    ) {
        self.state = state
        self.systemImage = systemImage
    }

    public var body: some View {
        VStack(spacing: 12) {
            Image(systemName: systemImage)
                .font(.title)
                .foregroundStyle(.tint)

            JchuLinearProgress(state: state)
        }
        .padding()
        .background(.fill.quaternary, in: .rect(cornerRadius: 16))
    }
}
