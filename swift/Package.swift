// swift-tools-version: 6.1

import PackageDescription

let package = Package(
    name: "JchuComponents",
    platforms: [
        .macOS(.v10_15),
        .iOS("26.0")
    ],
    products: [
        .library(
            name: "JchuComponents",
            targets: [
                "JchuComponentsCore",
                "JchuComponentsExtensions",
                "JchuComponentsSwiftUI"
            ]
        ),
        .library(
            name: "JchuComponentsPay",
            targets: ["JchuComponentsPay"]
        ),
        .library(
            name: "JchuComponentsCatalog",
            targets: ["JchuComponentsCatalog"]
        )
    ],
    dependencies: [
        .package(url: "https://github.com/onevcat/Kingfisher.git", from: "8.0.0"),
        .package(url: "https://github.com/RevenueCat/purchases-ios.git", from: "5.77.0")
    ],
    targets: [
        .binaryTarget(
            name: "JchuComponentsCore",
            path: "../jchucomponents-foundation/build/XCFrameworks/release/JchuComponentsCore.xcframework"
        ),
        .target(
            name: "JchuComponentsExtensions"
        ),
        .target(
            name: "JchuComponentsSwiftUI",
            dependencies: [
                "JchuComponentsCore",
                "JchuComponentsExtensions",
                .product(name: "Kingfisher", package: "Kingfisher")
            ]
        ),
        .target(
            name: "JchuComponentsPay",
            dependencies: [
                .product(name: "RevenueCat", package: "purchases-ios")
            ]
        ),
        .target(
            name: "JchuComponentsCatalog",
            dependencies: [
                "JchuComponentsCore",
                "JchuComponentsExtensions",
                "JchuComponentsSwiftUI"
            ]
        ),
        .testTarget(
            name: "JchuComponentsSwiftUITests",
            dependencies: [
                "JchuComponentsCore",
                "JchuComponentsExtensions",
                "JchuComponentsSwiftUI"
            ]
        )
    ]
)
