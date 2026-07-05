import SwiftUI

/// Resolves loading, success and failure content for optional remote data.
///
/// Loading takes precedence over available data. When loading finishes,
/// non-`nil` data selects success; otherwise failure receives the error text.
public struct JchuRemoteScreenContent<Data, LoadingContent: View, SuccessContent: View, FailureContent: View>: View {
    private let data: Data?
    private let isLoading: Bool
    private let error: String?
    private let loadingContent: LoadingContent
    private let successContent: (Data) -> SuccessContent
    private let failureContent: (String?) -> FailureContent

    /// Creates a remote-content state resolver.
    ///
    /// - Parameters:
    ///   - data: Successfully loaded data, when available.
    ///   - isLoading: Whether to display loading content.
    ///   - error: Optional error text passed to failure content.
    ///   - loadingContent: Content displayed while loading.
    ///   - successContent: Content built from loaded data.
    ///   - failureContent: Content built when no data is available.
    public init(
        data: Data?,
        isLoading: Bool,
        error: String?,
        @ViewBuilder loadingContent: () -> LoadingContent,
        @ViewBuilder successContent: @escaping (Data) -> SuccessContent,
        @ViewBuilder failureContent: @escaping (String?) -> FailureContent
    ) {
        self.data = data
        self.isLoading = isLoading
        self.error = error
        self.loadingContent = loadingContent()
        self.successContent = successContent
        self.failureContent = failureContent
    }

    public var body: some View {
        if isLoading {
            loadingContent
        } else if let data {
            successContent(data)
        } else {
            failureContent(error)
        }
    }
}
