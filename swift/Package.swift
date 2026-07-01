// swift-tools-version: 6.1

import PackageDescription

let package = Package(
    name: "JchuComponents",
    platforms: [
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
            name: "JchuComponentsCatalog",
            targets: ["JchuComponentsCatalog"]
        )
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
                "JchuComponentsExtensions"
            ]
        ),
        .target(
            name: "JchuComponentsCatalog",
            dependencies: ["JchuComponentsSwiftUI"]
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
