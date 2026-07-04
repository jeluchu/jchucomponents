import SwiftUI

public struct JchuRemoteScreenContent<Data, LoadingContent: View, SuccessContent: View, FailureContent: View>: View {
    private let data: Data?
    private let isLoading: Bool
    private let error: String?
    private let loadingContent: LoadingContent
    private let successContent: (Data) -> SuccessContent
    private let failureContent: (String?) -> FailureContent

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
