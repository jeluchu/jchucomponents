import Foundation

/// Day, month and year display pattern.
public let jchuDateFormatVerbose = "dd/MM/yyyy"
/// Day, month, year, hour and minute display pattern.
public let jchuDateFormatTimestamp = "dd/MM/yyyy HH:mm"
/// Hour and minute display pattern.
public let jchuDateFormatOnlyTime = "HH:mm 'H'"
/// Localized weekday, month and time display pattern.
public let jchuDateFormatWeekAndMonthTime = "EEEE, MMMM d, yyyy - hh:mm:ss a"

public extension DateFormatter {
    /// Creates a formatter with an explicit pattern, locale and time zone.
    static func jchu(
        pattern: String,
        locale: Locale = .current,
        timeZone: TimeZone = .current
    ) -> DateFormatter {
        let formatter = DateFormatter()
        formatter.dateFormat = pattern
        formatter.locale = locale
        formatter.timeZone = timeZone
        return formatter
    }
}

public extension Date {
    /// Formats this date as day, month and year.
    func format(
        locale: Locale = .current,
        timeZone: TimeZone = .current
    ) -> String {
        formatted(pattern: jchuDateFormatVerbose, locale: locale, timeZone: timeZone)
    }

    /// Formats this date as day, month, year, hour and minute.
    func formatWithTime(
        locale: Locale = .current,
        timeZone: TimeZone = .current
    ) -> String {
        formatted(pattern: jchuDateFormatTimestamp, locale: locale, timeZone: timeZone)
    }

    /// Formats this date as hour and minute.
    func formatOnlyTime(
        locale: Locale = .current,
        timeZone: TimeZone = .current
    ) -> String {
        formatted(pattern: jchuDateFormatOnlyTime, locale: locale, timeZone: timeZone)
    }

    /// Formats this date with an explicit Unicode date pattern.
    func formatted(
        pattern: String,
        locale: Locale = .current,
        timeZone: TimeZone = .current
    ) -> String {
        DateFormatter.jchu(
            pattern: pattern,
            locale: locale,
            timeZone: timeZone
        ).string(from: self)
    }

    func formatToServerDateTimeDefaults(
        locale: Locale = .current,
        timeZone: TimeZone = .current
    ) -> String {
        formatted(pattern: "yyyy-MM-dd HH:mm:ss", locale: locale, timeZone: timeZone)
    }

    func formatToServerDateDefaults(
        locale: Locale = .current,
        timeZone: TimeZone = .current
    ) -> String {
        formatted(pattern: "yyyy-MM-dd", locale: locale, timeZone: timeZone)
    }

    func formatToTruncatedDateTime(
        locale: Locale = .current,
        timeZone: TimeZone = .current
    ) -> String {
        formatted(pattern: "yyyyMMddHHmmss", locale: locale, timeZone: timeZone)
    }

    func formatToServerTimeDefaults(
        locale: Locale = .current,
        timeZone: TimeZone = .current
    ) -> String {
        formatted(pattern: "HH:mm:ss", locale: locale, timeZone: timeZone)
    }

    func formatToViewDateTimeDefaults(
        locale: Locale = .current,
        timeZone: TimeZone = .current
    ) -> String {
        formatted(pattern: "dd/MM/yyyy HH:mm:ss", locale: locale, timeZone: timeZone)
    }

    func formatToViewDateDefaults(
        locale: Locale = .current,
        timeZone: TimeZone = .current
    ) -> String {
        formatted(pattern: "dd/MM/yyyy", locale: locale, timeZone: timeZone)
    }

    func formatToViewTimeDefaults(
        locale: Locale = .current,
        timeZone: TimeZone = .current
    ) -> String {
        formatted(pattern: "HH:mm:ss", locale: locale, timeZone: timeZone)
    }

    /// Adds a calendar component, returning this date if calculation fails.
    func adding(
        _ component: Calendar.Component,
        amount: Int,
        calendar: Calendar = .current
    ) -> Date {
        calendar.date(byAdding: component, value: amount, to: self) ?? self
    }

    func addYears(_ years: Int, calendar: Calendar = .current) -> Date {
        adding(.year, amount: years, calendar: calendar)
    }

    func addMonths(_ months: Int, calendar: Calendar = .current) -> Date {
        adding(.month, amount: months, calendar: calendar)
    }

    func addDays(_ days: Int, calendar: Calendar = .current) -> Date {
        adding(.day, amount: days, calendar: calendar)
    }

    func addHours(_ hours: Int, calendar: Calendar = .current) -> Date {
        adding(.hour, amount: hours, calendar: calendar)
    }

    func addMinutes(_ minutes: Int, calendar: Calendar = .current) -> Date {
        adding(.minute, amount: minutes, calendar: calendar)
    }

    func addSeconds(_ seconds: Int, calendar: Calendar = .current) -> Date {
        adding(.second, amount: seconds, calendar: calendar)
    }

    func minusDays(_ days: Int, calendar: Calendar = .current) -> Date {
        addDays(-days, calendar: calendar)
    }

    func plusDays(_ days: Int, calendar: Calendar = .current) -> Date {
        addDays(days, calendar: calendar)
    }

    func plusMinutes(_ minutes: Int, calendar: Calendar = .current) -> Date {
        addMinutes(minutes, calendar: calendar)
    }

    func backInYears(_ years: Int, calendar: Calendar = .current) -> Date {
        addYears(-years, calendar: calendar)
    }

    func forwardInYears(_ years: Int, calendar: Calendar = .current) -> Date {
        addYears(years, calendar: calendar)
    }

    func firstHourOfTheDay(calendar: Calendar = .current) -> Date {
        calendar.startOfDay(for: self)
    }

    func firstDayOfTheMonth(calendar: Calendar = .current) -> Date {
        let components = calendar.dateComponents([.year, .month], from: self)
        return calendar.date(from: components) ?? firstHourOfTheDay(calendar: calendar)
    }

    /// Returns the final representable instant of this date's calendar day.
    func dayEnd(calendar: Calendar = .current) -> Date {
        let start = firstHourOfTheDay(calendar: calendar)
        return calendar.date(
            byAdding: DateComponents(day: 1, second: -1, nanosecond: 999_000_000),
            to: start
        ) ?? self
    }

    func isAfterOrEqualThan(
        numberDaysBeforeToday: Int,
        now: Date = Date(),
        calendar: Calendar = .current
    ) -> Bool {
        let startOfToday = calendar.startOfDay(for: now)
        let backDate = calendar.date(
            byAdding: .day,
            value: -numberDaysBeforeToday,
            to: startOfToday
        ) ?? startOfToday

        return self >= backDate
    }

    func isBeforeThan(
        numberDaysBeforeToday: Int,
        now: Date = Date(),
        calendar: Calendar = .current
    ) -> Bool {
        !isAfterOrEqualThan(
            numberDaysBeforeToday: numberDaysBeforeToday,
            now: now,
            calendar: calendar
        )
    }

    func getDateTime(daysNumber: Int, calendar: Calendar = .current) -> Date {
        addDays(-daysNumber, calendar: calendar)
    }

    /// Returns the absolute number of calendar-day boundaries between dates.
    func diffInDays(to next: Date, calendar: Calendar = .current) -> Int {
        let start = calendar.startOfDay(for: self)
        let end = calendar.startOfDay(for: next)
        return abs(calendar.dateComponents([.day], from: start, to: end).day ?? 0)
    }

    func toAccessibilityDateMMMMYYYY(
        locale: Locale = .current,
        timeZone: TimeZone = .current
    ) -> String {
        formatted(pattern: "MMMM yyyy", locale: locale, timeZone: timeZone)
    }
}

public extension Int {
    /// Formats a duration expressed in seconds as `mm:ss` or `h:mm:ss`.
    var durationText: String {
        if self >= 3600 {
            return String(format: "%d:%02d:%02d", self / 3600, (self % 3600) / 60, self % 60)
        }

        return String(format: "%02d:%02d", self / 60, self % 60)
    }
}

/// Returns the current date.
public func now() -> Date {
    Date()
}

public func currentDayWeekString(calendar: Calendar = .current) -> Int {
    calendar.component(.weekday, from: Date())
}

public func currentDayMonthString(calendar: Calendar = .current) -> Int {
    calendar.component(.day, from: Date())
}

public func currentMonthString(calendar: Calendar = .current) -> Int {
    calendar.component(.month, from: Date()) - 1
}

public func currentYearString(calendar: Calendar = .current) -> String {
    String(calendar.component(.year, from: Date()))
}

/// Formats a Unix timestamp expressed in milliseconds as a display date.
public func getDateTime(
    currentMillis: String,
    locale: Locale = .current,
    timeZone: TimeZone = .current
) -> String? {
    guard let milliseconds = TimeInterval(currentMillis) else {
        return nil
    }

    return Date(timeIntervalSince1970: milliseconds / 1000).formatted(
        pattern: "dd MMM yyyy",
        locale: locale,
        timeZone: timeZone
    )
}

/// Whether a millisecond timestamp belongs to a different calendar day.
public func isNextDay(
    lastFetchTime: TimeInterval,
    now: Date = Date(),
    calendar: Calendar = .current
) -> Bool {
    !calendar.isDate(Date(timeIntervalSince1970: lastFetchTime / 1000), inSameDayAs: now)
}

/// Whether a duration in milliseconds has elapsed since a millisecond timestamp.
public func isCustomTimePassed(
    lastFetchTime: TimeInterval,
    time: TimeInterval = 480 * 60 * 60 * 1000,
    now: Date = Date()
) -> Bool {
    now.timeIntervalSince1970 * 1000 - lastFetchTime >= time
}

public func isFetchFiveMinutes(lastFetchTime: TimeInterval, now: Date = Date()) -> Bool {
    isCustomTimePassed(lastFetchTime: lastFetchTime, time: 5 * 60 * 1000, now: now)
}

public func isFetchThirtyMinutes(lastFetchTime: TimeInterval, now: Date = Date()) -> Bool {
    isCustomTimePassed(lastFetchTime: lastFetchTime, time: 30 * 60 * 1000, now: now)
}

public func isFetchSixHours(lastFetchTime: TimeInterval, now: Date = Date()) -> Bool {
    isCustomTimePassed(lastFetchTime: lastFetchTime, time: 6 * 60 * 60 * 1000, now: now)
}

public func isFetchTwelveHours(lastFetchTime: TimeInterval, now: Date = Date()) -> Bool {
    isCustomTimePassed(lastFetchTime: lastFetchTime, time: 12 * 60 * 60 * 1000, now: now)
}

public func isFetchOneDay(lastFetchTime: TimeInterval, now: Date = Date()) -> Bool {
    isCustomTimePassed(lastFetchTime: lastFetchTime, time: 24 * 60 * 60 * 1000, now: now)
}

public func isFetchTwoDays(lastFetchTime: TimeInterval, now: Date = Date()) -> Bool {
    isCustomTimePassed(lastFetchTime: lastFetchTime, time: 48 * 60 * 60 * 1000, now: now)
}

public func isFetchThreeDays(lastFetchTime: TimeInterval, now: Date = Date()) -> Bool {
    isCustomTimePassed(lastFetchTime: lastFetchTime, time: 72 * 60 * 60 * 1000, now: now)
}

public func isFetchFourDays(lastFetchTime: TimeInterval, now: Date = Date()) -> Bool {
    isCustomTimePassed(lastFetchTime: lastFetchTime, time: 96 * 60 * 60 * 1000, now: now)
}

public func isFetchFiveDays(lastFetchTime: TimeInterval, now: Date = Date()) -> Bool {
    isCustomTimePassed(lastFetchTime: lastFetchTime, time: 120 * 60 * 60 * 1000, now: now)
}

public func isFetchSixDays(lastFetchTime: TimeInterval, now: Date = Date()) -> Bool {
    isCustomTimePassed(lastFetchTime: lastFetchTime, time: 144 * 60 * 60 * 1000, now: now)
}

public func isFetchSevenDays(lastFetchTime: TimeInterval, now: Date = Date()) -> Bool {
    isCustomTimePassed(lastFetchTime: lastFetchTime, time: 168 * 60 * 60 * 1000, now: now)
}

public func isFetchTenDays(lastFetchTime: TimeInterval, now: Date = Date()) -> Bool {
    isCustomTimePassed(lastFetchTime: lastFetchTime, time: 240 * 60 * 60 * 1000, now: now)
}

public func isFetchFifteenDays(lastFetchTime: TimeInterval, now: Date = Date()) -> Bool {
    isCustomTimePassed(lastFetchTime: lastFetchTime, time: 360 * 60 * 60 * 1000, now: now)
}

public func isFetchTwentyDays(lastFetchTime: TimeInterval, now: Date = Date()) -> Bool {
    isCustomTimePassed(lastFetchTime: lastFetchTime, time: 480 * 60 * 60 * 1000, now: now)
}
