/*
 *
 *  Copyright 2022 Jeluchu
 *
 */

package com.jeluchu.qr.qrcode.detector

class FinderPatternInfo(patternCenters: Array<FinderPattern>) {
    val bottomLeft: FinderPattern = patternCenters[0]
    val topLeft: FinderPattern = patternCenters[1]
    val topRight: FinderPattern = patternCenters[2]
}