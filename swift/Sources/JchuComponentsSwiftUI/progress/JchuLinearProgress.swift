//
//  JchuLinearProgress.swift
//  JchuComponents
//
//  Created by Jeluchu on 04/07/2026.
//

import SwiftUI
import JchuComponentsCore

/// A labeled linear representation of determinate or indeterminate progress.
public struct JchuLinearProgress: View {
    private let state: JchuProgressState

    /// Creates linear progress from shared Kotlin Multiplatform state.
    ///
    /// - Parameter state: The shared progress value and presentation state.
    public init(state: JchuProgressState) {
        self.state = state
    }

    /// Creates linear progress from native Swift values.
    ///
    /// - Parameters:
    ///   - title: Text displayed above the progress indicator.
    ///   - value: The completed amount.
    ///   - maxValue: The amount representing completion.
    ///   - isEnabled: Whether the view uses its enabled appearance.
    ///   - isIndeterminate: Whether to ignore `value` and show ongoing work.
    public init(
        _ title: String,
        value: Double = 0,
        maxValue: Double = 1,
        isEnabled: Bool = true,
        isIndeterminate: Bool = false
    ) {
        self.init(
            state: JchuProgressState(
                title: title,
                value: value,
                maxValue: maxValue,
                isEnabled: isEnabled,
                isIndeterminate: isIndeterminate
            )
        )
    }

    public var body: some View {
        VStack(alignment: .leading, spacing: 8) {
            Text(state.title)
                .font(.subheadline)

            if state.isIndeterminate { ProgressView() }
            else { ProgressView(value: state.fraction) }
        }
        .disabled(!state.isEnabled)
        .opacity(state.isEnabled ? 1 : 0.45)
    }
}

#Preview {
    VStack(alignment: .leading, spacing: 16) {
        JchuLinearProgress(
            state: JchuProgressState(
                title: "Downloading",
                value: 45,
                maxValue: 100,
                isEnabled: true,
                isIndeterminate: false
            )
        )

        JchuLinearProgress(
            state: JchuProgressState(
                title: "Preparing",
                value: 0,
                maxValue: 100,
                isEnabled: true,
                isIndeterminate: true
            )
        )
    }
    .padding()
}
