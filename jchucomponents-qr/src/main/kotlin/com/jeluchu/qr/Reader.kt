/*
 *
 *  Copyright 2022 Jeluchu
 *
 */

package com.jeluchu.qr

/**
 * Implementations of this interface can decode an image of a barcode in some format into
 * the String it encodes. For example, [com.jeluchu.jchucomponents.utils.zxing.qrcode.QRCodeReader] can
 * decode a QR code. The decoder may optionally receive hints from the caller which may help
 * it decode more quickly or accurately.
 *
 *
 */
interface Reader {
    /**
     * Locates and decodes a barcode in some format within an image.
     *
     * @param image image of barcode to decode
     * @return String which the barcode encodes
     * @throws NotFoundException if no potential barcode is found
     * @throws ChecksumException if a potential barcode is found but does not pass its checksum
     * @throws FormatException if a potential barcode is found but format is invalid
     */
    @Throws(NotFoundException::class, ChecksumException::class, FormatException::class)
    fun decode(image: BinaryBitmap?): Result?

    /**
     * Locates and decodes a barcode in some format within an image. This method also accepts
     * hints, each possibly associated to some data, which may help the implementation decode.
     *
     * @param image image of barcode to decode
     * @param hints passed as a [Map] from [DecodeHintType]
     * to arbitrary data. The
     * meaning of the data depends upon the hint type. The implementation may or may not do
     * anything with these hints.
     * @return String which the barcode encodes
     * @throws NotFoundException if no potential barcode is found
     * @throws ChecksumException if a potential barcode is found but does not pass its checksum
     * @throws FormatException if a potential barcode is found but format is invalid
     */
    @Throws(NotFoundException::class, ChecksumException::class, FormatException::class)
    fun decode(image: BinaryBitmap?, hints: Map<DecodeHintType?, *>?): Result?

    /**
     * Resets any internal state the implementation has after a decode, to prepare it
     * for reuse.
     */
    fun reset()
}