//
//  JchuLinearProgress.swift
//  JchuComponents
//
//  Created by Jeluchu on 04/07/2026.
//

import SwiftUI
import JchuComponentsCore

/// Labeled linear progress presented in a card with an SF Symbol.
public struct JchuIconProgress: View {
    private let state: JchuProgressState
    private let systemImage: String

    /// Creates icon progress from shared Kotlin Multiplatform state.
    ///
    /// - Parameters:
    ///   - state: The shared progress value and presentation state.
    ///   - systemImage: The SF Symbol displayed above the progress.
    public init(
        state: JchuProgressState,
        systemImage: String
    ) {
        self.state = state
        self.systemImage = systemImage
    }

    /// Creates icon progress from native Swift values.
    ///
    /// - Parameters:
    ///   - title: Text displayed beside the progress indicator.
    ///   - systemImage: The SF Symbol displayed above the progress.
    ///   - value: The completed amount.
    ///   - maxValue: The amount representing completion.
    ///   - isEnabled: Whether the view uses its enabled appearance.
    ///   - isIndeterminate: Whether to ignore `value` and show ongoing work.
    public init(
        _ title: String,
        systemImage: String,
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
            ),
            systemImage: systemImage
        )
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

#Preview {
    JchuIconProgress(
        state: JchuProgressState(
            title: "Uploading",
            value: 72,
            maxValue: 100,
            isEnabled: true,
            isIndeterminate: false
        ),
        systemImage: "icloud.and.arrow.up"
    )
    .padding()
}
