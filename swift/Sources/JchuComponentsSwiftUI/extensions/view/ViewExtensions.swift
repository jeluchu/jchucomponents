//
//  JchuLinearProgress.swift
//  JchuComponents
//
//  Created by Jeluchu on 04/07/2026.
//

import SwiftUI
import UIKit

public extension View {
    /// Applies a transform only when a condition is true.
    @ViewBuilder
    func `if`<TransformedContent: View>(
        _ condition: Bool,
        transform: (Self) -> TransformedContent
    ) -> some View {
        if condition {
            transform(self)
        } else {
            self
        }
    }

    /// Selects one of two transforms based on a condition.
    @ViewBuilder
    func `if`<TrueContent: View, FalseContent: View>(
        _ condition: Bool,
        transform: (Self) -> TrueContent,
        else elseTransform: (Self) -> FalseContent
    ) -> some View {
        if condition {
            transform(self)
        } else {
            elseTransform(self)
        }
    }

    /// Clips only the selected UIKit corners with a radius.
    func cornerRadius(
        _ radius: CGFloat,
        corners: UIRectCorner
    ) -> some View {
        clipShape(JchuRoundedCorner(radius: radius, corners: corners))
    }

    /// Erases this view's concrete type.
    func eraseToAnyView() -> AnyView {
        AnyView(self)
    }

    /// Renders this view into a UIKit image.
    ///
    /// - Parameter scale: Output scale. Use the destination display scale when
    ///   pixel density matters.
    @MainActor
    func snapshot(
        scale: CGFloat = 1
    ) -> UIImage? {
        let renderer = ImageRenderer(content: self)
        renderer.scale = scale
        return renderer.uiImage
    }

    /// Overlays placeholder content while retaining this view in the hierarchy.
    func placeholder<Content: View>(
        when shouldShow: Bool,
        alignment: Alignment = .leading,
        @ViewBuilder placeholder: () -> Content
    ) -> some View {
        ZStack(alignment: alignment) {
            placeholder().opacity(shouldShow ? 1 : 0)
            self
        }
    }
    
    /// Overlays gray placeholder text while retaining this view.
    func placeholder(
        _ text: String,
        when shouldShow: Bool,
        alignment: Alignment = .leading
    ) -> some View {
        placeholder(when: shouldShow, alignment: alignment) {
            Text(text).foregroundColor(.gray)
        }
    }

    /// Returns the safe-area insets of the first connected application window.
    @MainActor
    func getSafeArea() -> UIEdgeInsets {
        if let windowScene = UIApplication.shared.connectedScenes.first as? UIWindowScene,
           let window = windowScene.windows.first {
            return window.safeAreaInsets
        } else {
            return UIEdgeInsets.zero
        }
    }

    /// Returns the top safe-area inset of the first connected window.
    @MainActor
    func getSafeAreaTop() -> CGFloat {
        if let windowScene = UIApplication.shared.connectedScenes.first as? UIWindowScene,
           let window = windowScene.windows.first {
            return window.safeAreaInsets.top
        } else {
            return 0
        }
    }

    /// Returns the bottom safe-area inset of the first connected window.
    @MainActor
    func getSafeAreaBottom() -> CGFloat {
        if let windowScene = UIApplication.shared.connectedScenes.first as? UIWindowScene,
           let window = windowScene.windows.first {
            return window.safeAreaInsets.bottom
        } else {
            return 0
        }
    }

    /// Expands this view toward an edge or across the horizontal center.
    @ViewBuilder
    func alignment(_ alignment: JchuFrameAlignment) -> some View {
        switch alignment {
        case .top:
            self.frame(maxHeight: .infinity, alignment: .top)
        case .bottom:
            self.frame(maxHeight: .infinity, alignment: .bottom)
        case .leading:
            self.frame(maxWidth: .infinity, alignment: .leading)
        case .trailing:
            self.frame(maxWidth: .infinity, alignment: .trailing)
        case .center:
            self.frame(maxWidth: .infinity, alignment: .center)
        }
    }

    /// Draws a filled rounded rectangle behind this view.
    @ViewBuilder
    func roundBackground(corner: CGFloat, color: Color) -> some View {
        self.background(RoundedRectangle(cornerRadius: corner).fill(color))
    }

    /// Draws a stroked rounded rectangle behind this view.
    @ViewBuilder
    func roundStrokeBackground(corner: CGFloat, color: Color, lineWidth: CGFloat = 1) -> some View {
        self.background(RoundedRectangle(cornerRadius: corner).stroke(color, lineWidth: lineWidth))
    }

    /// Draws a filled and stroked rounded rectangle behind this view.
    @ViewBuilder
    func roundWithStrokeBackground(corner: CGFloat, container: Color, stroke: Color, lineWidth: CGFloat = 1) -> some View {
        self.background(
            RoundedRectangle(cornerRadius: corner)
                .fill(container)
                .stroke(stroke, lineWidth: lineWidth)
        )
    }
}

/// A shape that rounds a selected set of UIKit corners.
public struct JchuRoundedCorner: Shape {
    public var radius: CGFloat
    public var corners: UIRectCorner

    /// Creates a selective rounded-corner shape.
    public init(
        radius: CGFloat,
        corners: UIRectCorner = .allCorners
    ) {
        self.radius = radius
        self.corners = corners
    }

    public func path(in rect: CGRect) -> Path {
        Path(
            UIBezierPath(
                roundedRect: rect,
                byRoundingCorners: corners,
                cornerRadii: CGSize(width: radius, height: radius)
            ).cgPath
        )
    }
}

/// Frame expansion directions used by the `View.alignment(_:)` helper.
public enum JchuFrameAlignment {
    case top, bottom, leading, trailing, center
}

@available(*, deprecated, renamed: "JchuFrameAlignment")
public typealias FrameAlignment = JchuFrameAlignment
