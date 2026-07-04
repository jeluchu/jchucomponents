//
//  JchuLinearProgress.swift
//  JchuComponents
//
//  Created by Jeluchu on 04/07/2026.
//

import SwiftUI
import UIKit

public extension View {
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

    func cornerRadius(
        _ radius: CGFloat,
        corners: UIRectCorner
    ) -> some View {
        clipShape(JchuRoundedCorner(radius: radius, corners: corners))
    }

    func eraseToAnyView() -> AnyView {
        AnyView(self)
    }

    @MainActor
    func snapshot(
        scale: CGFloat = 1
    ) -> UIImage? {
        let renderer = ImageRenderer(content: self)
        renderer.scale = scale
        return renderer.uiImage
    }

    func placeholder<Content: View>(
        when shouldShow: Bool,
        alignment: Alignment = .leading,
        @ViewBuilder placeholder: () -> Content) -> some View {

        ZStack(alignment: alignment) {
            placeholder().opacity(shouldShow ? 1 : 0)
            self
        }
    }
    
    func placeholder(
        _ text: String,
        when shouldShow: Bool,
        alignment: Alignment = .leading) -> some View {
            
        placeholder(when: shouldShow, alignment: alignment) { Text(text).foregroundColor(.gray) }
    }
    
    func getSafeArea() -> UIEdgeInsets {
        if let windowScene = UIApplication.shared.connectedScenes.first as? UIWindowScene,
           let window = windowScene.windows.first {
           return window.safeAreaInsets
        } else {
            return UIEdgeInsets.zero
        }
    }
    
    func getSafeAreaTop() -> CGFloat {
        if let windowScene = UIApplication.shared.connectedScenes.first as? UIWindowScene,
           let window = windowScene.windows.first {
            return window.safeAreaInsets.top
        } else {
            return 0
        }
    }
    
    func getSafeAreaBottom() -> CGFloat {
        if let windowScene = UIApplication.shared.connectedScenes.first as? UIWindowScene,
           let window = windowScene.windows.first {
            return window.safeAreaInsets.bottom
        } else {
            return 0
        }
    }
    
    // MARK: VIEW SWIFTUI ELEMENTS
    
    @ViewBuilder
    func alignment(_ alignment: FrameAlignment) -> some View {
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
    
    @ViewBuilder
    func roundBackground(corner: CGFloat, color: Color) -> some View {
        self.background(RoundedRectangle(cornerRadius: corner).fill(color))
    }
    
    @ViewBuilder
    func roundStrokeBackground(corner: CGFloat, color: Color, lineWidth: CGFloat = 1) -> some View {
        self.background(RoundedRectangle(cornerRadius: corner).stroke(color, lineWidth: lineWidth))
    }
    
    @ViewBuilder
    func roundWithStrokeBackground(corner: CGFloat, container: Color, stroke: Color, lineWidth: CGFloat = 1) -> some View {
        self.background(
            RoundedRectangle(cornerRadius: corner)
                .fill(container)
                .stroke(stroke, lineWidth: lineWidth)
        )
    }
}

public struct JchuRoundedCorner: Shape {
    public var radius: CGFloat
    public var corners: UIRectCorner

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

public enum FrameAlignment {
    case top, bottom, leading, trailing, center
}
