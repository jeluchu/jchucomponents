//
//  JchuLinearProgress.swift
//  JchuComponents
//
//  Created by Jeluchu on 04/07/2026.
//

import Foundation

public extension String {
    /// An empty string convenience value.
    static var empty: String {
        ""
    }

    /// The trimmed string, or `nil` when it contains only whitespace.
    var nilIfBlank: String? {
        let trimmed = trimmingCharacters(in: .whitespacesAndNewlines)
        return trimmed.isEmpty ? nil : trimmed
    }

    /// This string, or `nil` when it contains no characters.
    var nilIfEmpty: String? {
        isEmpty ? nil : self
    }

    /// A copy with carriage-return and newline characters removed.
    var withoutNewlines: String {
        self
            .replacingOccurrences(of: "\n", with: "")
            .replacingOccurrences(of: "\r", with: "")
    }

    /// A locale-aware copy with diacritics and width variants folded.
    var removingDiacritics: String {
        folding(
            options: [.diacriticInsensitive, .widthInsensitive],
            locale: .current
        )
    }

    /// All Unicode numeric characters contained in this string.
    var onlyDigits: String {
        filter(\.isNumber)
    }

    /// Whether the string contains at least one letter.
    var containsLetters: Bool {
        rangeOfCharacter(from: .letters) != nil
    }

    /// Whether the string contains at least one decimal digit.
    var containsNumbers: Bool {
        rangeOfCharacter(from: .decimalDigits) != nil
    }

    /// Whether this non-empty string contains only numeric characters.
    var isNumeric: Bool {
        !isEmpty && allSatisfy(\.isNumber)
    }

    /// Whether this non-empty string contains only letters.
    var isAlphabetic: Bool {
        !isEmpty && allSatisfy(\.isLetter)
    }

    /// Whether this non-empty string contains only letters and numbers.
    var isAlphanumeric: Bool {
        !isEmpty && allSatisfy { $0.isLetter || $0.isNumber }
    }

    /// Whether this string matches a practical, non-exhaustive email pattern.
    var isValidEmail: Bool {
        range(
            of: #"^[A-Z0-9._%+-]+@[A-Z0-9.-]+\.[A-Z]{2,}$"#,
            options: [.regularExpression, .caseInsensitive]
        ) != nil
    }

    /// Whether this string contains exactly four IPv4 octets from 0 through 255.
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

    /// The number of whitespace-separated words.
    var wordCount: Int {
        split(whereSeparator: \.isWhitespace)
            .count
    }

    /// The last path component parsed from a URL-like string.
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

    /// A copy whose `http` scheme is upgraded to `https`.
    var httpsURLString: String {
        guard lowercased().hasPrefix("http:") else {
            return self
        }

        return "https:" + dropFirst("http:".count)
    }

    /// The UTF-8 representation encoded as Base64.
    var base64Encoded: String {
        Data(utf8).base64EncodedString()
    }

    /// The UTF-8 string decoded from Base64, or `nil` for invalid data.
    var base64Decoded: String? {
        guard let data = Data(base64Encoded: self) else {
            return nil
        }

        return String(data: data, encoding: .utf8)
    }

    /// Returns at most `maximumLength` characters followed by trailing text.
    ///
    /// Negative limits and strings already within the limit are unchanged.
    func truncated(
        to maximumLength: Int,
        trailing: String = "…"
    ) -> String {
        guard maximumLength >= 0, count > maximumLength else {
            return self
        }

        return String(prefix(maximumLength)) + trailing
    }

    /// Removes every occurrence of a substring.
    func removing(
        _ value: String,
        options: String.CompareOptions = []
    ) -> String {
        replacingOccurrences(of: value, with: "", options: options)
    }

    /// Replaces hyphens with spaces.
    func replacingDashesWithSpaces() -> String {
        replacingOccurrences(of: "-", with: " ")
    }

    /// Splits this string into space-separated groups.
    ///
    /// Non-positive group sizes return the original string.
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

    /// Splits this string into groups joined by a custom separator.
    ///
    /// Non-positive group sizes return the original string.
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

    /// Replaces only the first occurrence of a substring.
    func replacingFirst(
        _ target: String,
        with replacement: String
    ) -> String {
        guard let range = range(of: target) else {
            return self
        }
        return replacingCharacters(in: range, with: replacement)
    }

    /// Whether this string has an HTTP or HTTPS URL scheme.
    var isHTTPURL: Bool {
        guard let scheme = URLComponents(string: self)?.scheme?.lowercased() else {
            return false
        }
        return scheme == "http" || scheme == "https"
    }

    /// Whether this string contains both a URL scheme and host.
    var isValidURL: Bool {
        guard let components = URLComponents(string: self) else {
            return false
        }
        return components.scheme != nil && components.host != nil
    }

    /// Returns a copy with its first character capitalized for a locale.
    func capitalizingFirstLetter(locale: Locale = .current) -> String {
        guard let first else {
            return self
        }

        return String(first).uppercased(with: locale) + dropFirst()
    }
}
