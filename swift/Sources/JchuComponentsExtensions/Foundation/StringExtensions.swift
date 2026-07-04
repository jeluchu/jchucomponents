//
//  JchuLinearProgress.swift
//  JchuComponents
//
//  Created by Jeluchu on 04/07/2026.
//

import Foundation

public extension String {
    static var empty: String {
        ""
    }

    var nilIfBlank: String? {
        let trimmed = trimmingCharacters(in: .whitespacesAndNewlines)
        return trimmed.isEmpty ? nil : trimmed
    }

    var nilIfEmpty: String? {
        isEmpty ? nil : self
    }

    var withoutNewlines: String {
        self
            .replacingOccurrences(of: "\n", with: "")
            .replacingOccurrences(of: "\r", with: "")
    }

    var removingDiacritics: String {
        folding(
            options: [.diacriticInsensitive, .widthInsensitive],
            locale: .current
        )
    }

    var onlyDigits: String {
        filter(\.isNumber)
    }

    var containsLetters: Bool {
        rangeOfCharacter(from: .letters) != nil
    }

    var containsNumbers: Bool {
        rangeOfCharacter(from: .decimalDigits) != nil
    }

    var isNumeric: Bool {
        !isEmpty && allSatisfy(\.isNumber)
    }

    var isAlphabetic: Bool {
        !isEmpty && allSatisfy(\.isLetter)
    }

    var isAlphanumeric: Bool {
        !isEmpty && allSatisfy { $0.isLetter || $0.isNumber }
    }

    var isValidEmail: Bool {
        range(
            of: #"^[A-Z0-9._%+-]+@[A-Z0-9.-]+\.[A-Z]{2,}$"#,
            options: [.regularExpression, .caseInsensitive]
        ) != nil
    }

    var isValidIPv4: Bool {
        let parts = split(separator: ".", omittingEmptySubsequences: false)
        guard parts.count == 4 else {
            return false
        }

        return parts.allSatisfy { part in
            guard !part.isEmpty, part.allSatisfy(\.isNumber), let value = Int(part) else {
                return false
            }

            return (0...255).contains(value)
        }
    }

    var wordCount: Int {
        split(whereSeparator: \.isWhitespace)
            .count
    }

    var lastPathComponentFromURL: String {
        if let url = URL(string: self), !url.lastPathComponent.isEmpty {
            return url.lastPathComponent
        }

        return self
            .split(separator: "?")
            .first?
            .split(separator: "/")
            .last
            .map(String.init) ?? self
    }

    var httpsURLString: String {
        guard lowercased().hasPrefix("http:") else {
            return self
        }

        return "https:" + dropFirst("http:".count)
    }

    var base64Encoded: String {
        Data(utf8).base64EncodedString()
    }

    var base64Decoded: String? {
        guard let data = Data(base64Encoded: self) else {
            return nil
        }

        return String(data: data, encoding: .utf8)
    }

    func truncated(
        to maximumLength: Int,
        trailing: String = "…"
    ) -> String {
        guard maximumLength >= 0, count > maximumLength else {
            return self
        }

        return String(prefix(maximumLength)) + trailing
    }

    func removing(
        _ value: String,
        options: String.CompareOptions = []
    ) -> String {
        replacingOccurrences(of: value, with: "", options: options)
    }

    func replacingDashesWithSpaces() -> String {
        replacingOccurrences(of: "-", with: " ")
    }

    func grouped(every groupSize: Int) -> String {
        guard groupSize > 0 else {
            return self
        }

        return stride(from: 0, to: count, by: groupSize)
            .map { index in
                let start = self.index(startIndex, offsetBy: index)
                let end = self.index(start, offsetBy: groupSize, limitedBy: endIndex) ?? endIndex
                return String(self[start..<end])
            }
            .joined(separator: " ")
    }

    func formatInGroups(
        groupSize: Int = 4,
        separator: String = "-"
    ) -> String {
        guard groupSize > 0 else {
            return self
        }

        return stride(from: 0, to: count, by: groupSize)
            .map { offset in
                let start = index(startIndex, offsetBy: offset)
                let end = index(start, offsetBy: groupSize, limitedBy: endIndex) ?? endIndex
                return String(self[start..<end])
            }
            .joined(separator: separator)
    }

    func replacingFirst(
        _ target: String,
        with replacement: String
    ) -> String {
        guard let range = range(of: target) else {
            return self
        }
        return replacingCharacters(in: range, with: replacement)
    }

    var isHTTPURL: Bool {
        guard let scheme = URLComponents(string: self)?.scheme?.lowercased() else {
            return false
        }
        return scheme == "http" || scheme == "https"
    }

    var isValidURL: Bool {
        guard let components = URLComponents(string: self) else {
            return false
        }
        return components.scheme != nil && components.host != nil
    }

    func capitalizingFirstLetter(locale: Locale = .current) -> String {
        guard let first else {
            return self
        }

        return String(first).uppercased(with: locale) + dropFirst()
    }
}
