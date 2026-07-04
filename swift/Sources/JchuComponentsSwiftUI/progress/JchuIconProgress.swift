//
//  JchuLinearProgress.swift
//  JchuComponents
//
//  Created by Jeluchu on 04/07/2026.
//

import SwiftUI
import JchuComponentsCore

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
