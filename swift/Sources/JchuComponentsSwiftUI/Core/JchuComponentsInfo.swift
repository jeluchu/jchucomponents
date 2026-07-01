import JchuComponentsCore

/// Information about the JchuComponents binary used by the Swift package.
public enum JchuComponentsInfo {
    public static var version: String {
        JchuComponents.shared.VERSION
    }
}
