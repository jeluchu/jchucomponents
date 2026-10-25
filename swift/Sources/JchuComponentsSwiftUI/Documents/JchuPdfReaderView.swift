#if canImport(PDFKit) && canImport(UIKit)
import Foundation
import PDFKit
import SwiftUI
import UIKit

/// Labels displayed by ``JchuPdfReaderView``.
public struct JchuPdfReaderLabels {
    public var back: String
    public var share: String
    public var previousPage: String
    public var nextPage: String
    public var rotateLeft: String
    public var rotateRight: String
    public var retry: String
    public var pageSeparator: String
    public var errorTitle: String
    public var shareErrorTitle: String

    public init(
        back: String = "Back",
        share: String = "Share PDF",
        previousPage: String = "Previous page",
        nextPage: String = "Next page",
        rotateLeft: String = "Rotate left",
        rotateRight: String = "Rotate right",
        retry: String = "Retry",
        pageSeparator: String = "/",
        errorTitle: String = "Unable to open PDF",
        shareErrorTitle: String = "Unable to share PDF"
    ) {
        self.back = back
        self.share = share
        self.previousPage = previousPage
        self.nextPage = nextPage
        self.rotateLeft = rotateLeft
        self.rotateRight = rotateRight
        self.retry = retry
        self.pageSeparator = pageSeparator
        self.errorTitle = errorTitle
        self.shareErrorTitle = shareErrorTitle
    }
}

/// Downloads and displays a remote PDF using PDFKit.
@available(iOS 26.0, *)
public struct JchuPdfReaderView: View {
    private let url: URL
    private let title: String
    private let labels: JchuPdfReaderLabels
    private let onBack: (() -> Void)?

    @State private var document: PDFDocument?
    @State private var loadError: String?
    @State private var retryToken = 0
    @State private var currentPage = 0
    @State private var rotationCommand = 0
    @State private var shareFile: JchuPdfShareFile?
    @State private var shareError: String?
    @Environment(\.dismiss) private var dismiss

    /// Creates a reader for a remote PDF URL.
    ///
    /// - Parameters:
    ///   - url: The remote PDF URL.
    ///   - title: The navigation title used by the view hierarchy.
    ///   - labels: Labels for controls and error alerts.
    ///   - onBack: Optional app-specific back action. If omitted, the view dismisses itself.
    public init(
        url: URL,
        title: String = "PDF",
        labels: JchuPdfReaderLabels = JchuPdfReaderLabels(),
        onBack: (() -> Void)? = nil
    ) {
        self.url = url
        self.title = title
        self.labels = labels
        self.onBack = onBack
        _document = State(initialValue: nil)
        _loadError = State(initialValue: nil)
    }

    private var pageCount: Int { document?.pageCount ?? 0 }

    private var loadTaskID: String {
        "\(url.absoluteString)-\(retryToken)"
    }

    public var body: some View {
        JchuRemoteScreenContent(
            data: document,
            isLoading: document == nil && loadError == nil,
            error: loadError
        ) {
            ProgressView()
                .tint(.white)
                .frame(maxWidth: .infinity, maxHeight: .infinity)
        } successContent: { loadedDocument in
            PDFReaderDocumentView(
                document: loadedDocument,
                currentPage: $currentPage,
                rotationCommand: rotationCommand
            )
        } failureContent: { message in
            failureContent(message: message ?? labels.errorTitle)
        }
        .background(Color.black.ignoresSafeArea())
        .navigationTitle(title)
        .navigationBarTitleDisplayMode(.inline)
        .toolbar(.hidden, for: .navigationBar)
        .overlay(alignment: .top) {
            if document != nil {
                topBar
            }
        }
        .overlay(alignment: .bottom) {
            if document != nil {
                bottomBar
            }
        }
        .task(id: loadTaskID) {
            await loadDocument()
        }
        .sheet(item: $shareFile) { shareFile in
            PDFReaderShareSheet(fileURL: shareFile.url)
        }
        .alert(labels.shareErrorTitle, isPresented: Binding(
            get: { shareError != nil },
            set: { if !$0 { shareError = nil } }
        )) {
            Button("OK", role: .cancel) { shareError = nil }
        } message: {
            Text(shareError ?? "")
        }
    }

    private var topBar: some View {
        HStack(spacing: 8) {
            barButton(systemName: "chevron.left", label: labels.back) {
                if let onBack {
                    onBack()
                } else {
                    dismiss()
                }
            }

            Spacer(minLength: 8)

            Text("\(currentPage + 1) \(labels.pageSeparator) \(pageCount)")
                .font(.system(size: 20, weight: .bold))
                .monospacedDigit()
                .foregroundStyle(.white)
                .padding(10)
                .background(.white.opacity(0.16), in: RoundedRectangle(cornerRadius: 13))
                .accessibilityLabel("\(currentPage + 1) \(labels.pageSeparator) \(pageCount)")

            Spacer(minLength: 8)

            barButton(systemName: "square.and.arrow.up", label: labels.share, action: sharePDF)
        }
        .padding(.horizontal, 8)
        .padding(.vertical, 12)
    }

    private var bottomBar: some View {
        HStack(spacing: 8) {
            barButton(
                systemName: "chevron.left",
                label: labels.previousPage,
                isEnabled: currentPage > 0
            ) {
                currentPage = max(currentPage - 1, 0)
            }

            Spacer(minLength: 8)

            barButton(systemName: "rotate.left", label: labels.rotateLeft) {
                rotationCommand -= 1
            }
            barButton(systemName: "rotate.right", label: labels.rotateRight) {
                rotationCommand += 1
            }

            Spacer(minLength: 8)

            barButton(
                systemName: "chevron.right",
                label: labels.nextPage,
                isEnabled: currentPage < pageCount - 1
            ) {
                currentPage = min(currentPage + 1, pageCount - 1)
            }
        }
        .padding(.horizontal, 8)
        .padding(.vertical, 10)
        .background(.black.opacity(0.72), in: UnevenRoundedRectangle(
            topLeadingRadius: 18,
            topTrailingRadius: 18
        ))
    }

    private func barButton(
        systemName: String,
        label: String,
        isEnabled: Bool = true,
        action: @escaping () -> Void
    ) -> some View {
        Button(action: action) {
            Image(systemName: systemName)
                .font(.system(size: 19, weight: .semibold))
                .foregroundStyle(.white)
                .frame(width: 48, height: 48)
                .background(.white.opacity(isEnabled ? 0.16 : 0.04), in: RoundedRectangle(cornerRadius: 15))
        }
        .buttonStyle(.plain)
        .disabled(!isEnabled)
        .accessibilityLabel(label)
    }

    private func failureContent(message: String) -> some View {
        VStack(spacing: 16) {
            Image(systemName: "exclamationmark.triangle")
                .font(.largeTitle)
                .foregroundStyle(.white)

            Text(labels.errorTitle)
                .font(.headline)
                .foregroundStyle(.white)

            Text(message)
                .multilineTextAlignment(.center)
                .foregroundStyle(.white)

            Button(labels.retry) {
                loadError = nil
                retryToken += 1
            }
            .foregroundStyle(.white)
            .padding(.horizontal, 18)
            .padding(.vertical, 10)
            .background(.white.opacity(0.16), in: Capsule())
        }
        .padding(24)
        .frame(maxWidth: .infinity, maxHeight: .infinity)
    }

    @MainActor
    private func loadDocument() async {
        document = nil
        loadError = nil
        do {
            let (data, response) = try await URLSession.shared.data(from: url)
            if let response = response as? HTTPURLResponse,
               !(200..<300).contains(response.statusCode) {
                throw JchuPdfLoadError.httpStatus(response.statusCode)
            }
            guard let loadedDocument = PDFDocument(data: data), loadedDocument.pageCount > 0 else {
                throw JchuPdfLoadError.invalidDocument
            }
            document = loadedDocument
            currentPage = 0
            rotationCommand = 0
        } catch is CancellationError {
            return
        } catch {
            loadError = error.localizedDescription
        }
    }

    private func sharePDF() {
        guard let data = document?.dataRepresentation() else {
            shareError = "The PDF could not be prepared for sharing."
            return
        }

        let fileURL = FileManager.default.temporaryDirectory
            .appendingPathComponent("jchu-\(UUID().uuidString).pdf")
        do {
            try data.write(to: fileURL, options: .atomic)
            shareFile = JchuPdfShareFile(url: fileURL)
        } catch {
            shareError = error.localizedDescription
        }
    }
}

private enum JchuPdfLoadError: LocalizedError {
    case httpStatus(Int)
    case invalidDocument

    var errorDescription: String? {
        switch self {
        case let .httpStatus(statusCode): "PDF request returned HTTP \(statusCode)."
        case .invalidDocument: "The downloaded file is not a readable PDF."
        }
    }
}

private struct JchuPdfShareFile: Identifiable {
    let url: URL
    var id: String { url.absoluteString }
}

@available(iOS 26.0, *)
private struct PDFReaderDocumentView: UIViewRepresentable {
    let document: PDFDocument
    @Binding var currentPage: Int
    let rotationCommand: Int

    func makeCoordinator() -> Coordinator {
        Coordinator(parent: self)
    }

    func makeUIView(context: Context) -> PDFView {
        let pdfView = PDFView()
        pdfView.backgroundColor = .black
        pdfView.document = document
        pdfView.autoScales = true
        pdfView.displayMode = .singlePage
        pdfView.displayDirection = .horizontal
        pdfView.usePageViewController(true, withViewOptions: nil)
        context.coordinator.attach(to: pdfView)
        context.coordinator.appliedRotationCommand = rotationCommand
        return pdfView
    }

    func updateUIView(_ pdfView: PDFView, context: Context) {
        context.coordinator.parent = self
        if pdfView.document !== document {
            pdfView.document = document
        }

        guard document.pageCount > 0,
              let targetPage = document.page(at: min(max(currentPage, 0), document.pageCount - 1)) else {
            return
        }

        let displayedPageIndex = pdfView.currentPage.map { document.index(for: $0) }
        if displayedPageIndex != currentPage {
            pdfView.go(to: targetPage)
        }

        let rotationDelta = rotationCommand - context.coordinator.appliedRotationCommand
        if rotationDelta != 0 {
            targetPage.rotation = (targetPage.rotation + rotationDelta * 90 % 360 + 360) % 360
            pdfView.go(to: targetPage)
            context.coordinator.appliedRotationCommand = rotationCommand
        }
    }

    @MainActor
    final class Coordinator: NSObject {
        var parent: PDFReaderDocumentView
        var appliedRotationCommand = 0

        init(parent: PDFReaderDocumentView) {
            self.parent = parent
        }

        func attach(to pdfView: PDFView) {
            NotificationCenter.default.addObserver(
                self,
                selector: #selector(pageDidChange(_:)),
                name: .PDFViewPageChanged,
                object: pdfView
            )
        }

        @objc private func pageDidChange(_ notification: Notification) {
            guard let pdfView = notification.object as? PDFView,
                  let page = pdfView.currentPage,
                  let document = pdfView.document else { return }
            let pageIndex = document.index(for: page)
            Task { @MainActor [weak self] in
                self?.parent.currentPage = pageIndex
            }
        }

        deinit {
            NotificationCenter.default.removeObserver(self)
        }
    }
}

@available(iOS 26.0, *)
private struct PDFReaderShareSheet: UIViewControllerRepresentable {
    let fileURL: URL

    func makeUIViewController(context: Context) -> UIActivityViewController {
        UIActivityViewController(activityItems: [fileURL], applicationActivities: nil)
    }

    func updateUIViewController(_ controller: UIActivityViewController, context: Context) {}
}
#endif
