//
//  JchuLinearProgress.swift
//  JchuComponents
//
//  Created by Jeluchu on 04/07/2026.
//

import SwiftUI
import JchuComponentsCore

/// A circular representation of determinate or indeterminate shared progress.
public struct JchuCircularProgress: View {
    private let state: JchuProgressState

    /// Creates circular progress from shared Kotlin Multiplatform state.
    ///
    /// - Parameter state: The shared progress value and presentation state.
    public init(state: JchuProgressState) {
        self.state = state
    }

    /// Creates circular progress from native Swift values.
    ///
    /// - Parameters:
    ///   - title: Text displayed below the progress indicator.
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

#Preview {
    HStack(spacing: 24) {
        JchuCircularProgress(
            state: JchuProgressState(
                title: "Sync",
                value: 60,
                maxValue: 100,
                isEnabled: true,
                isIndeterminate: false
            )
        )

        JchuCircularProgress(
            state: JchuProgressState(
                title: "Waiting",
                value: 0,
                maxValue: 100,
                isEnabled: true,
                isIndeterminate: true
            )
        )
    }
    .padding()
}
