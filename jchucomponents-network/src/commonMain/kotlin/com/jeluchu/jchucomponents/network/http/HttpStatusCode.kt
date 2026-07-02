package com.jeluchu.jchucomponents.network.http

/**
 * This is a list of Hypertext Transfer Protocol (HTTP) response status codes.
 */
enum class HttpStatusCode(val code: Int, val message: String) {
    Unknown(code = 0, message = "Unknown status code"),

    Continue(code = 100, message = "Continue"),
    SwitchingProtocols(code = 101, message = "Switching Protocols"),
    Processing(code = 102, message = "Processing"),
    EarlyHints(code = 103, message = "Early Hints"),

    OK(code = 200, message = "OK"),
    Created(code = 201, message = "Created"),
    Accepted(code = 202, message = "Accepted"),
    NonAuthoritative(code = 203, message = "Non-Authoritative Information"),
    NoContent(code = 204, message = "No Content"),
    ResetContent(code = 205, message = "Reset Content"),
    PartialContent(code = 206, message = "Partial Content"),
    MultiStatus(code = 207, message = "Multi-Status"),
    AlreadyReported(code = 208, message = "Already Reported"),
    IMUsed(code = 209, message = "IM Used"),

    MultipleChoices(code = 300, message = "Multiple Choices"),
    MovePermanently(code = 301, message = "Moved Permanently"),
    Found(code = 302, message = "Found"),
    SeeOther(code = 303, message = "See Other"),
    NotModified(code = 304, message = "Not Modified"),
    UseProxy(code = 305, message = "Use Proxy"),
    SwitchProxy(code = 306, message = "Switch Proxy"),
    TemporaryRedirect(code = 307, message = "Temporary Redirect"),
    PermanentRedirect(code = 308, message = "Permanent Redirect"),

    BadRequest(code = 400, message = "Bad Request"),
    Unauthorized(code = 401, message = "Unauthorized"),
    PaymentRequired(code = 402, message = "Payment Required"),
    Forbidden(code = 403, message = "Forbidden"),
    NotFound(code = 404, message = "Not Found"),
    MethodNotAllowed(code = 405, message = "Method Not Allowed"),
    NotAcceptable(code = 406, message = "Not Acceptable"),
    ProxyAuthenticationRequired(code = 407, message = "Proxy Authentication Required"),
    RequestTimeout(code = 408, message = "Request Timeout"),
    Conflict(code = 409, message = "Conflict"),
    Gone(code = 410, message = "Gone"),
    LengthRequired(code = 411, message = "Length Required"),
    PreconditionFailed(code = 412, message = "Precondition Failed"),
    PayloadTooLarge(code = 413, message = "Payload Too Large"),
    URITooLong(code = 414, message = "URI Too Long"),
    UnsupportedMediaType(code = 415, message = "Unsupported Media Type"),
    RangeNotSatisfiable(code = 416, message = "Range Not Satisfiable"),
    ExpectationFailed(code = 417, message = "Expectation Failed"),
    IMATeapot(code = 418, message = "I'm a teapot"),
    MisdirectedRequest(code = 421, message = "Misdirected Request"),
    UnProcessableEntity(code = 422, message = "Unprocessable Entity"),
    Locked(code = 423, message = "Locked"),
    FailedDependency(code = 424, message = "Failed Dependency"),
    TooEarly(code = 425, message = "Too Early"),
    UpgradeRequired(code = 426, message = "Upgrade Required"),
    PreconditionRequired(code = 428, message = "Precondition Required"),
    TooManyRequests(code = 429, message = "Too Many Requests"),
    RequestHeaderFieldsTooLarge(code = 431, message = "Request Header Fields Too Large"),
    UnavailableForLegalReasons(code = 451, message = "Unavailable For Legal Reasons"),

    InternalServerError(code = 500, message = "Internal Server Error"),
    NotImplemented(code = 501, message = "Not Implemented"),
    BadGateway(code = 502, message = "Bad Gateway"),
    ServiceUnavailable(code = 503, message = "Service Unavailable"),
    GatewayTimeout(code = 504, message = "Gateway Timeout"),
    HTTPVersionNotSupported(code = 505, message = "HTTP Version Not Supported"),
    NotExtended(code = 510, message = "Not Extended"),
    NetworkAuthenticationRequired(code = 511, message = "Network Authentication Required");

    val isSuccess: Boolean get() = code in 200..299
    val isRedirection: Boolean get() = code in 300..399
    val isClientError: Boolean get() = code in 400..499
    val isServerError: Boolean get() = code in 500..599
    val isInformational: Boolean get() = code in 100..199

    companion object {
        fun fromCode(code: Int): HttpStatusCode =
            entries.firstOrNull { status -> status.code == code } ?: Unknown
    }
}

fun getHttpErrorInfo(code: Int): HttpStatusCode = HttpStatusCode.fromCode(code)
