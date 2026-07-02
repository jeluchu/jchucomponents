package com.jeluchu.jchucomponents.network.http

/**
 * This is a list of Hypertext Transfer Protocol (HTTP) response status codes.
 */
enum class StatusCode(val code: Int, val message: String) {
    Unknown(code = 0, message = ""),

    Continue(code = 100, message = ""),
    SwitchingProtocols(code = 101, message = ""),
    Processing(code = 102, message = ""),
    EarlyHints(code = 103, message = ""),

    OK(code = 200, message = ""),
    Created(code = 201, message = ""),
    Accepted(code = 202, message = ""),
    NonAuthoritative(code = 203, message = ""),
    NoContent(code = 204, message = ""),
    ResetContent(code = 205, message = ""),
    PartialContent(code = 206, message = ""),
    MultiStatus(code = 207, message = ""),
    AlreadyReported(code = 208, message = ""),
    IMUsed(code = 209, message = ""),

    MultipleChoices(code = 300, message = ""),
    MovePermanently(code = 301, message = ""),
    Found(code = 302, message = ""),
    SeeOther(code = 303, message = ""),
    NotModified(code = 304, message = ""),
    UseProxy(code = 305, message = ""),
    SwitchProxy(code = 306, message = ""),
    TemporaryRedirect(code = 307, message = ""),
    PermanentRedirect(code = 308, message = ""),

    BadRequest(code = 400, message = ""),
    Unauthorized(code = 401, message = ""),
    PaymentRequired(code = 402, message = ""),
    Forbidden(code = 403, message = ""),
    NotFound(code = 404, message = ""),
    MethodNotAllowed(code = 405, message = ""),
    NotAcceptable(code = 406, message = ""),
    ProxyAuthenticationRequired(code = 407, message = ""),
    RequestTimeout(code = 408, message = ""),
    Conflict(code = 409, message = ""),
    Gone(code = 410, message = ""),
    LengthRequired(code = 411, message = ""),
    PreconditionFailed(code = 412, message = ""),
    PayloadTooLarge(code = 413, message = ""),
    URITooLong(code = 414, message = ""),
    UnsupportedMediaType(code = 415, message = ""),
    RangeNotSatisfiable(code = 416, message = ""),
    ExpectationFailed(code = 417, message = ""),
    IMATeapot(code = 418, message = ""),
    MisdirectedRequest(code = 421, message = ""),
    UnProcessableEntity(code = 422, message = ""),
    Locked(code = 423, message = ""),
    FailedDependency(code = 424, message = ""),
    TooEarly(code = 425, message = ""),
    UpgradeRequired(code = 426, message = ""),
    PreconditionRequired(code = 428, message = ""),
    TooManyRequests(code = 429, message = ""),
    RequestHeaderFieldsTooLarge(code = 431, message = ""),
    UnavailableForLegalReasons(code = 451, message = ""),

    InternalServerError(code = 500, message = ""),
    NotImplemented(code = 501, message = ""),
    BadGateway(code = 502, message = ""),
    ServiceUnavailable(code = 503, message = ""),
    GatewayTimeout(code = 504, message = ""),
    HTTPVersionNotSupported(code = 505, message = ""),
    NotExtended(code = 510, message = ""),
    NetworkAuthenticationRequired(code = 511, message = "");
}

fun getHttpErrorInfo(code: Int) = when (code) {

    0 -> StatusCode.Unknown

    100 -> StatusCode.Continue
    101 -> StatusCode.SwitchingProtocols
    102 -> StatusCode.Processing
    103 -> StatusCode.EarlyHints

    200 -> StatusCode.OK
    201 -> StatusCode.Created
    202 -> StatusCode.Accepted
    203 -> StatusCode.NonAuthoritative
    204 -> StatusCode.NoContent
    205 -> StatusCode.ResetContent
    206 -> StatusCode.PartialContent
    207 -> StatusCode.MultiStatus
    208 -> StatusCode.AlreadyReported
    209 -> StatusCode.IMUsed

    300 -> StatusCode.MultipleChoices
    301 -> StatusCode.MovePermanently
    302 -> StatusCode.Found
    303 -> StatusCode.SeeOther
    304 -> StatusCode.NotModified
    305 -> StatusCode.UseProxy
    306 -> StatusCode.SwitchProxy
    307 -> StatusCode.TemporaryRedirect
    308 -> StatusCode.PermanentRedirect

    400 -> StatusCode.BadRequest
    401 -> StatusCode.Unauthorized
    402 -> StatusCode.PaymentRequired
    403 -> StatusCode.Forbidden
    404 -> StatusCode.NotFound
    405 -> StatusCode.MethodNotAllowed
    406 -> StatusCode.NotAcceptable
    407 -> StatusCode.ProxyAuthenticationRequired
    408 -> StatusCode.RequestTimeout
    409 -> StatusCode.Conflict
    410 -> StatusCode.Gone
    411 -> StatusCode.LengthRequired
    412 -> StatusCode.PreconditionFailed
    413 -> StatusCode.PayloadTooLarge
    414 -> StatusCode.URITooLong
    415 -> StatusCode.UnsupportedMediaType
    416 -> StatusCode.RangeNotSatisfiable
    417 -> StatusCode.ExpectationFailed
    418 -> StatusCode.IMATeapot
    421 -> StatusCode.MisdirectedRequest
    422 -> StatusCode.UnProcessableEntity
    423 -> StatusCode.Locked
    424 -> StatusCode.FailedDependency
    425 -> StatusCode.TooEarly
    426 -> StatusCode.UpgradeRequired
    428 -> StatusCode.PreconditionRequired
    429 -> StatusCode.TooManyRequests
    431 -> StatusCode.RequestHeaderFieldsTooLarge
    451 -> StatusCode.UnavailableForLegalReasons

    500 -> StatusCode.InternalServerError
    501 -> StatusCode.NotImplemented
    502 -> StatusCode.BadGateway
    503 -> StatusCode.ServiceUnavailable
    504 -> StatusCode.GatewayTimeout
    505 -> StatusCode.HTTPVersionNotSupported
    510 -> StatusCode.NotExtended
    511 -> StatusCode.NetworkAuthenticationRequired

    else -> StatusCode.Unknown

}
