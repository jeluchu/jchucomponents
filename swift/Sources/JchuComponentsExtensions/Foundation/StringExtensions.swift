import Foundation

extension String: JchuCompatible {}

public extension JchuExtension where Base == String {
    var nilIfBlank: String? {
        let trimmed = base.trimmingCharacters(in: .whitespacesAndNewlines)
        return trimmed.isEmpty ? nil : trimmed
    }

    var removingDiacritics: String {
        base.folding(
            options: [.diacriticInsensitive, .widthInsensitive],
            locale: .current
        )
    }

    func truncated(
        to maximumLength: Int,
        trailing: String = "…"
    ) -> String {
        guard maximumLength >= 0, base.count > maximumLength else {
            return base
        }

        return String(base.prefix(maximumLength)) + trailing
    }
}
