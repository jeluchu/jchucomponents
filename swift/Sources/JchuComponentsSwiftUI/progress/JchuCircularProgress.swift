//
//  JchuLinearProgress.swift
//  JchuComponents
//
//  Created by Jeluchu on 04/07/2026.
//

import SwiftUI
import JchuComponentsCore

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
