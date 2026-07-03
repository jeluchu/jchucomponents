import Foundation
import RevenueCat

public enum JchuSubscriptionType: Equatable, Sendable {
    case none
    case monthly
    case yearly
    case promo
}

public enum JchuSubscriptionState: Equatable, Sendable {
    case none
    case active
    case inactiveUntilRenewal
}

public struct JchuSubscriptionInfo: Equatable, Sendable {
    public let renewalType: JchuSubscriptionType
    public let expireDate: String
    public let promotional: Bool
    public let state: JchuSubscriptionState
    public let managementUrl: String?

    public static let empty = JchuSubscriptionInfo(
        renewalType: .none,
        expireDate: "",
        promotional: false,
        state: .none,
        managementUrl: nil
    )
}

public struct JchuPaymentProduct: Sendable {
    public let isMonthly: Bool
    public let price: String
    public let priceConversion: String?
    public let saveAmount: String?
    public let package: Package
}

public struct JchuBillingInfo: Sendable {
    public let info: JchuSubscriptionInfo
    public let packages: [Package]
    public let products: [JchuPaymentProduct]

    public static let empty = JchuBillingInfo(
        info: .empty,
        packages: [],
        products: []
    )
}

@MainActor
public final class JchuPayment: ObservableObject {
    public static let shared = JchuPayment()

    @Published public private(set) var billingInfo: JchuBillingInfo = .empty
    @Published public private(set) var billingError: Error?

    public private(set) var subscriptionName = ""

    private let promoProductIdentifiers = [
        "rc_promo_pro_daily",
        "rc_promo_pro_three_day",
        "rc_promo_pro_weekly",
        "rc_promo_pro_monthly",
        "rc_promo_pro_three_month",
        "rc_promo_pro_six_month",
        "rc_promo_pro_yearly",
        "rc_promo_pro_lifetime"
    ]

    public var userId: String {
        Purchases.shared.appUserID
    }

    private init() {}

    public func configure(
        apiKey: String,
        isDebug: Bool,
        subscriptionName: String
    ) {
        if isDebug {
            Purchases.logLevel = .debug
        }

        Purchases.configure(
            with: Configuration.Builder(withAPIKey: apiKey)
                .with(storeKitVersion: .storeKit2)
                .build()
        )
        setSubscriptionName(subscriptionName)
    }

    public func setSubscriptionName(_ name: String) {
        subscriptionName = name
    }

    public func getProducts() {
        Purchases.shared.getOfferings { [weak self] offerings, error in
            Task { @MainActor in
                guard let self else { return }

                if let error {
                    self.billingError = error
                    return
                }

                self.getCustomerInfo { customerInfo in
                    guard
                        let customerInfo,
                        let packages = offerings?.current?.availablePackages,
                        !packages.isEmpty
                    else { return }

                    self.billingInfo = JchuBillingInfo(
                        info: self.buildSubscriptionInfo(
                            entitlement: self.subscriptionName,
                            customerInfo: customerInfo,
                            packages: packages
                        ),
                        packages: packages,
                        products: self.buildSubscriptionProducts(packages)
                    )
                }
            }
        }
    }

    public func purchase(
        type: PackageType,
        onSuccess: @escaping (Bool, String) -> Void = { _, _ in },
        onFailure: @escaping (Error, Bool) -> Void = { _, _ in }
    ) {
        guard let package = billingInfo.packages.first(where: { $0.packageType == type }) else {
            return
        }

        Purchases.shared.purchase(package: package) { [weak self] _, customerInfo, error, userCancelled in
            Task { @MainActor in
                if let error {
                    onFailure(error, userCancelled)
                    return
                }

                guard let customerInfo else { return }
                self?.activateSubscription(customerInfo: customerInfo, onActiveSubscription: onSuccess)
            }
        }
    }

    public func restorePurchases(
        onRestored: @escaping (Bool, String) -> Void = { _, _ in }
    ) {
        Purchases.shared.restorePurchases { [weak self] customerInfo, _ in
            Task { @MainActor in
                guard
                    let self,
                    let entitlementInfo = customerInfo?.entitlements.all[self.subscriptionName]
                else { return }

                if entitlementInfo.isActive {
                    onRestored(true, entitlementInfo.productIdentifier)
                } else {
                    onRestored(false, "")
                }
            }
        }
    }

    public func isSubscriptionActive(
        onChecked: @escaping (Bool, String) -> Void = { _, _ in }
    ) {
        getCustomerInfo { [weak self] customerInfo in
            guard
                let self,
                let customerInfo,
                let entitlementInfo = customerInfo.entitlements.all[self.subscriptionName]
            else { return }

            if entitlementInfo.isActive && self.subscriptionActive(customerInfo: customerInfo) {
                onChecked(true, entitlementInfo.productIdentifier)
            } else {
                onChecked(false, "")
            }
        }
    }

    private func buildSubscriptionProducts(_ packages: [Package]) -> [JchuPaymentProduct] {
        let monthly = packages.first { $0.packageType == .monthly }
        let annual = packages.first { $0.packageType == .annual }

        guard let monthly, let annual else { return [] }

        let annualPrice = annual.storeProduct.priceDecimal
        let monthlyPrice = monthly.storeProduct.priceDecimal
        let annualPricePerMonth = annualPrice / 12
        let savings = monthlyPrice == 0 ? 0 : 100 - ((annualPricePerMonth / monthlyPrice) * 100)

        return [
            JchuPaymentProduct(
                isMonthly: true,
                price: monthly.storeProduct.localizedPriceString,
                priceConversion: nil,
                saveAmount: nil,
                package: monthly
            ),
            JchuPaymentProduct(
                isMonthly: false,
                price: annual.storeProduct.localizedPriceString,
                priceConversion: annualPricePerMonth.roundedString(scale: 2),
                saveAmount: savings.roundedString(scale: 0),
                package: annual
            )
        ]
    }

    private func buildSubscriptionInfo(
        entitlement: String,
        customerInfo: CustomerInfo,
        packages: [Package]
    ) -> JchuSubscriptionInfo {
        let product = packages.first {
            $0.storeProduct.productIdentifier == customerInfo.entitlements.all[entitlement]?.productIdentifier
        }
        var expire = "?"
        var isPromotional = false

        if !isSubscriptionExpired(customerInfo: customerInfo) {
            let formatter = DateFormatter()
            formatter.dateFormat = "dd/MM/yyyy HH:mm aaa"
            formatter.locale = Locale(identifier: "en_US_POSIX")
            expire = formatter.string(
                from: customerInfo.entitlements.all[entitlement]?.expirationDate ?? Date()
            )
        } else {
            expire = ""
        }

        let renewalType: JchuSubscriptionType
        switch product?.packageType {
        case .monthly:
            renewalType = .monthly
        case .annual:
            renewalType = .yearly
        default:
            if promoProductIdentifiers.contains(where: { customerInfo.activeSubscriptions.contains($0) }) {
                isPromotional = true
                renewalType = .promo
            } else {
                renewalType = .none
            }
        }

        return JchuSubscriptionInfo(
            renewalType: renewalType,
            expireDate: expire,
            promotional: isPromotional,
            state: isSubscriptionPaused(entitlement: entitlement, customerInfo: customerInfo) ? .inactiveUntilRenewal : .active,
            managementUrl: customerInfo.managementURL?.absoluteString
        )
    }

    private func activateSubscription(
        customerInfo: CustomerInfo,
        onActiveSubscription: (Bool, String) -> Void
    ) {
        guard let entitlementInfo = customerInfo.entitlements.all[subscriptionName] else { return }

        if entitlementInfo.isActive {
            onActiveSubscription(true, entitlementInfo.productIdentifier)
        }
    }

    private func isSubscriptionExpired(customerInfo: CustomerInfo) -> Bool {
        guard let expirationDate = customerInfo.entitlements.all[subscriptionName]?.expirationDate else {
            return false
        }

        let date = max(Date(), customerInfo.requestDate)
        return expirationDate <= date
    }

    private func subscriptionActive(customerInfo: CustomerInfo) -> Bool {
        customerInfo.entitlements.all[subscriptionName]?.isActive == true && !isSubscriptionExpired(customerInfo: customerInfo)
    }

    private func isSubscriptionPaused(entitlement: String, customerInfo: CustomerInfo) -> Bool {
        !subscriptionActive(customerInfo: customerInfo) && customerInfo.entitlements.all[entitlement]?.willRenew == true
    }

    private func getCustomerInfo(callback: @escaping (CustomerInfo?) -> Void) {
        Purchases.shared.getCustomerInfo { customerInfo, _ in
            Task { @MainActor in
                callback(customerInfo)
            }
        }
    }
}

private extension StoreProduct {
    var priceDecimal: Decimal {
        price as Decimal
    }
}

private extension Decimal {
    func roundedString(scale: Int) -> String {
        var value = self
        var result = Decimal()
        NSDecimalRound(&result, &value, scale, .plain)
        return NSDecimalNumber(decimal: result).stringValue
    }
}
