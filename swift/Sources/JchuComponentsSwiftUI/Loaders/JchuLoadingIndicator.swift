//
//  JchuLinearProgress.swift
//  JchuComponents
//
//  Created by Jeluchu on 04/07/2026.
//

import SwiftUI

/// An indeterminate progress indicator with an optional localized label.
public struct JchuLoadingIndicator: View {
    private let label: LocalizedStringKey?

    /// Creates a loading indicator.
    ///
    /// - Parameter label: Optional text displayed below the indicator.
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
