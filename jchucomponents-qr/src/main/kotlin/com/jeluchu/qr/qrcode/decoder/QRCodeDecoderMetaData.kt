/*
 *
 *  Copyright 2022 Jeluchu
 *
 */

package com.jeluchu.qr.qrcode.decoder

import com.jeluchu.qr.ResultPoint

class QRCodeDecoderMetaData internal constructor(
    private val isMirrored: Boolean
) {

    fun applyMirroredCorrection(points: Array<ResultPoint?>) {
        if (!isMirrored || points.size < 3) return
        val bottomLeft = points[0]
        points[0] = points[2]
        points[2] = bottomLeft
    }

}