//
//  JchuLinearProgress.swift
//  JchuComponents
//
//  Created by Jeluchu on 04/07/2026.
//

import SwiftUI

public struct JchuExpandableSearch: View {
    @Binding private var query: String
    private let defaults: SearchBarDefaults
    private let externalExpansion: Binding<Bool>?
    private let onExpandedChange: (Bool) -> Void
    private let onSearch: (String) -> Void

    @State private var internalExpansion: Bool
    @FocusState private var isFocused: Bool
    @Namespace private var searchAnimation

    public init(
        query: Binding<String>,
        defaults: SearchBarDefaults,
        onExpandedChange: @escaping (Bool) -> Void = { _ in },
        onSearch: @escaping (String) -> Void = { _ in }
    ) {
        _query = query
        self.defaults = defaults
        externalExpansion = nil
        self.onExpandedChange = onExpandedChange
        self.onSearch = onSearch
        _internalExpansion = State(initialValue: defaults.initiallyExpanded)
    }

    public init(
        query: Binding<String>,
        isExpanded: Binding<Bool>,
        defaults: SearchBarDefaults,
        onExpandedChange: @escaping (Bool) -> Void = { _ in },
        onSearch: @escaping (String) -> Void = { _ in }
    ) {
        _query = query
        self.defaults = defaults
        externalExpansion = isExpanded
        self.onExpandedChange = onExpandedChange
        self.onSearch = onSearch
        _internalExpansion = State(initialValue: isExpanded.wrappedValue)
    }

    private var isExpanded: Bool {
        externalExpansion?.wrappedValue ?? internalExpansion
    }

    public var body: some View {
        Group {
            if isExpanded {
                HStack(spacing: 8) {
                    Image(systemName: defaults.searchSystemImage)
                        .accessibilityHidden(true)

                    TextField(defaults.label, text: $query)
                        .focused($isFocused)
                        .submitLabel(.search)
                        .onSubmit {
                            onSearch(query)
                            setExpanded(false)
                        }

                    if !query.isEmpty {
                        Button {
                            withAnimation(.spring(response: 0.25, dampingFraction: 0.75)) {
                                query = ""
                            }
                        } label: {
                            Image(systemName: defaults.clearSystemImage)
                        }
                        .accessibilityLabel(Text("Clear search"))
                        .transition(.scale(scale: 0.7).combined(with: .opacity))
                    }

                    HStack(spacing: 4) {
                        Button {
                            setExpanded(false)
                        } label: {
                            Image(systemName: defaults.closeSystemImage)
                        }
                        .accessibilityLabel(Text("Close search"))
                    }
                    .padding(6)
                    .background(
                        defaults.contentColor.opacity(0.08),
                        in: RoundedRectangle(cornerRadius: 10)
                    )
                }
                .padding(.horizontal, 12)
                .frame(minHeight: 44)
                .frame(maxWidth: .infinity)
                .background(
                    RoundedRectangle(cornerRadius: 18)
                        .fill(defaults.containerColor)
                        .matchedGeometryEffect(id: "searchBackground", in: searchAnimation)
                )
                .transition(
                    .asymmetric(
                        insertion: .scale(scale: 0.96).combined(with: .opacity),
                        removal: .scale(scale: 0.98).combined(with: .opacity)
                    )
                )
            } else {
                Button {
                    setExpanded(true)
                } label: {
                    Label(defaults.label, systemImage: defaults.searchSystemImage)
                        .lineLimit(1)
                        .padding(.horizontal, 16)
                        .frame(minHeight: 44)
                }
                .buttonStyle(.plain)
                .background(
                    RoundedRectangle(cornerRadius: 18)
                        .fill(defaults.containerColor)
                        .matchedGeometryEffect(id: "searchBackground", in: searchAnimation)
                )
                .transition(
                    .asymmetric(
                        insertion: .scale(scale: 0.94).combined(with: .opacity),
                        removal: .scale(scale: 0.98).combined(with: .opacity)
                    )
                )
            }
        }
        .foregroundStyle(defaults.contentColor)
        .animation(.spring(response: 0.42, dampingFraction: 0.78), value: isExpanded)
        .animation(.spring(response: 0.25, dampingFraction: 0.75), value: query.isEmpty)
        .onChange(of: isExpanded) { _, expanded in
            isFocused = expanded
        }
        .onAppear {
            if defaults.initiallyExpanded {
                isFocused = true
            }
        }
    }

    private func setExpanded(_ expanded: Bool) {
        withAnimation(.spring(response: 0.42, dampingFraction: 0.78)) {
            if let externalExpansion {
                externalExpansion.wrappedValue = expanded
            } else {
                internalExpansion = expanded
            }
        }
        isFocused = expanded
        onExpandedChange(expanded)
    }
}

public struct SearchBarDefaults {
    public var label: LocalizedStringKey
    public var initiallyExpanded: Bool
    public var containerColor: Color
    public var contentColor: Color
    public var searchSystemImage: String
    public var clearSystemImage: String
    public var closeSystemImage: String

    public init(
        label: LocalizedStringKey,
        initiallyExpanded: Bool = false,
        containerColor: Color = Color(uiColor: .secondarySystemBackground),
        contentColor: Color = .primary,
        searchSystemImage: String = "magnifyingglass",
        clearSystemImage: String = "xmark.circle.fill",
        closeSystemImage: String = "xmark"
    ) {
        self.label = label
        self.initiallyExpanded = initiallyExpanded
        self.containerColor = containerColor
        self.contentColor = contentColor
        self.searchSystemImage = searchSystemImage
        self.clearSystemImage = clearSystemImage
        self.closeSystemImage = closeSystemImage
    }
}

#Preview(traits: .sizeThatFitsLayout) {
    @Previewable @State var query = ""

    JchuExpandableSearch(
        query: $query,
        defaults: SearchBarDefaults(label: "Search")
    )
    .padding()
}
