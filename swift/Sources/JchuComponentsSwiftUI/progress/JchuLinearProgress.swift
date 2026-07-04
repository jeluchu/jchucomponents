//
//  JchuLinearProgress.swift
//  JchuComponents
//
//  Created by Jeluchu on 04/07/2026.
//

import SwiftUI
import JchuComponentsCore

public struct JchuLinearProgress: View {
    private let state: JchuProgressState

    public init(state: JchuProgressState) {
        self.state = state
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
