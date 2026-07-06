public extension AsyncSequence {
    /// Observes each element and routes completion or failure to async callbacks.
    ///
    /// Errors thrown while iterating, including cancellation errors, call
    /// `onFailure`; `onComplete` runs only after normal completion.
    func observe(
        onStart: () async -> Void = {},
        onEach: (Element) async -> Void,
        onComplete: () async -> Void = {},
        onFailure: (Error) async -> Void = { _ in }
    ) async {
        await onStart()

        do {
            for try await element in self {
                await onEach(element)
            }
            await onComplete()
        } catch {
            await onFailure(error)
        }
    }
}

public extension Task where Failure == Error {
    /// Starts a throwing task and routes its result through async callbacks.
    ///
    /// Failure callbacks run before the original error is rethrown by the task.
    @discardableResult
    static func perform(
        priority: TaskPriority? = nil,
        operation: @escaping @Sendable () async throws -> Success,
        onSuccess: @escaping @Sendable (Success) async -> Void = { _ in },
        onFailure: @escaping @Sendable (Error) async -> Void = { _ in }
    ) -> Task {
        Task(priority: priority) {
            do {
                let result = try await operation()
                await onSuccess(result)
                return result
            } catch {
                await onFailure(error)
                throw error
            }
        }
    }
}
