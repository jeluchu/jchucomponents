/*
 *
 *  Copyright 2022 Jeluchu
 *
 */

package com.jchucomponents.core.extensions.retrofit

/**
 * This is a list of Hypertext Transfer Protocol (HTTP) response status codes.
 */
enum class StatusCode(val code: Int, val message: String) {

    Unknown(0, ""),

    Continue(100, ""),
    SwitchingProtocols(101, ""),
    Processing(102, ""),
    EarlyHints(103, ""),

    OK(200, ""),
    Created(201, ""),
    Accepted(202, ""),
    NonAuthoritative(203, ""),
    NoContent(204, ""),
    ResetContent(205, ""),
    PartialContent(206, ""),
    MultiStatus(207, ""),
    AlreadyReported(208, ""),
    IMUsed(209, ""),

    MultipleChoices(300, ""),
    MovePermanently(301, ""),
    Found(302, ""),
    SeeOther(303, ""),
    NotModified(304, ""),
    UseProxy(305, ""),
    SwitchProxy(306, ""),
    TemporaryRedirect(307, ""),
    PermanentRedirect(308, ""),

    BadRequest(400, ""),
    Unauthorized(401, ""),
    PaymentRequired(402, ""),
    Forbidden(403, ""),
    NotFound(404, ""),
    MethodNotAllowed(405, ""),
    NotAcceptable(406, ""),
    ProxyAuthenticationRequired(407, ""),
    RequestTimeout(408, ""),
    Conflict(409, ""),
    Gone(410, ""),
    LengthRequired(411, ""),
    PreconditionFailed(412, ""),
    PayloadTooLarge(413, ""),
    URITooLong(414, ""),
    UnsupportedMediaType(415, ""),
    RangeNotSatisfiable(416, ""),
    ExpectationFailed(417, ""),
    IMATeapot(418, ""),
    MisdirectedRequest(421, ""),
    UnProcessableEntity(422, ""),
    Locked(423, ""),
    FailedDependency(424, ""),
    TooEarly(425, ""),
    UpgradeRequired(426, ""),
    PreconditionRequired(428, ""),
    TooManyRequests(429, ""),
    RequestHeaderFieldsTooLarge(431, ""),
    UnavailableForLegalReasons(451, ""),

    InternalServerError(500, ""),
    NotImplemented(501, ""),
    BadGateway(502, ""),
    ServiceUnavailable(503, ""),
    GatewayTimeout(504, ""),
    HTTPVersionNotSupported(505, ""),
    NotExtended(510, ""),
    NetworkAuthenticationRequired(511, "");

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