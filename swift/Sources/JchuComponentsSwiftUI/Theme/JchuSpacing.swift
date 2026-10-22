//
//  JchuLinearProgress.swift
//  JchuComponents
//
//  Created by Jeluchu on 04/07/2026.
//

import SwiftUI

/// Spacing and sizing tokens shared by JchuComponents views.
///
/// Token names encode their default point value. For example, `dimen24`
/// defaults to 24 points. A custom scale can override only the values that
/// differ from the defaults.
public struct JchuSpacing: Sendable {
    public let dimen00: CGFloat
    public let dimen00_5: CGFloat
    public let dimen00_6: CGFloat
    public let dimen00_8: CGFloat
    public let dimen01: CGFloat
    public let dimen01_2: CGFloat
    public let dimen01_4: CGFloat
    public let dimen01_5: CGFloat
    public let dimen01_8: CGFloat
    public let dimen02: CGFloat
    public let dimen02_2: CGFloat
    public let dimen02_5: CGFloat
    public let dimen03: CGFloat
    public let dimen03_5: CGFloat
    public let dimen04: CGFloat
    public let dimen05: CGFloat
    public let dimen05_5: CGFloat
    public let dimen06: CGFloat
    public let dimen07: CGFloat
    public let dimen08: CGFloat
    public let dimen09: CGFloat
    public let dimen10: CGFloat
    public let dimen11: CGFloat
    public let dimen12: CGFloat
    public let dimen13: CGFloat
    public let dimen14: CGFloat
    public let dimen15: CGFloat
    public let dimen16: CGFloat
    public let dimen17: CGFloat
    public let dimen18: CGFloat
    public let dimen19: CGFloat
    public let dimen20: CGFloat
    public let dimen21: CGFloat
    public let dimen22: CGFloat
    public let dimen23: CGFloat
    public let dimen24: CGFloat
    public let dimen25: CGFloat
    public let dimen26: CGFloat
    public let dimen28: CGFloat
    public let dimen29: CGFloat
    public let dimen30: CGFloat
    public let dimen31: CGFloat
    public let dimen32: CGFloat
    public let dimen33: CGFloat
    public let dimen34: CGFloat
    public let dimen35: CGFloat
    public let dimen36: CGFloat
    public let dimen38: CGFloat
    public let dimen40: CGFloat
    public let dimen42: CGFloat
    public let dimen43: CGFloat
    public let dimen44: CGFloat
    public let dimen45: CGFloat
    public let dimen46: CGFloat
    public let dimen47: CGFloat
    public let dimen48: CGFloat
    public let dimen50: CGFloat
    public let dimen52: CGFloat
    public let dimen54: CGFloat
    public let dimen56: CGFloat
    public let dimen58: CGFloat
    public let dimen60: CGFloat
    public let dimen62: CGFloat
    public let dimen63: CGFloat
    public let dimen64: CGFloat
    public let dimen65: CGFloat
    public let dimen66: CGFloat
    public let dimen68: CGFloat
    public let dimen70: CGFloat
    public let dimen72: CGFloat
    public let dimen74: CGFloat
    public let dimen76: CGFloat
    public let dimen78: CGFloat
    public let dimen80: CGFloat
    public let dimen82: CGFloat
    public let dimen84: CGFloat
    public let dimen85: CGFloat
    public let dimen86: CGFloat
    public let dimen88: CGFloat
    public let dimen90: CGFloat
    public let dimen92: CGFloat
    public let dimen94: CGFloat
    public let dimen96: CGFloat
    public let dimen100: CGFloat
    public let dimen104: CGFloat
    public let dimen106: CGFloat
    public let dimen108: CGFloat
    public let dimen110: CGFloat
    public let dimen112: CGFloat
    public let dimen115: CGFloat
    public let dimen116: CGFloat
    public let dimen118: CGFloat
    public let dimen120: CGFloat
    public let dimen125: CGFloat
    public let dimen126: CGFloat
    public let dimen128: CGFloat
    public let dimen130: CGFloat
    public let dimen132: CGFloat
    public let dimen136: CGFloat
    public let dimen140: CGFloat
    public let dimen144: CGFloat
    public let dimen145: CGFloat
    public let dimen146: CGFloat
    public let dimen148: CGFloat
    public let dimen150: CGFloat
    public let dimen152: CGFloat
    public let dimen156: CGFloat
    public let dimen160: CGFloat
    public let dimen168: CGFloat
    public let dimen170: CGFloat
    public let dimen172: CGFloat
    public let dimen174: CGFloat
    public let dimen176: CGFloat
    public let dimen180: CGFloat
    public let dimen188: CGFloat
    public let dimen190: CGFloat
    public let dimen194: CGFloat
    public let dimen196: CGFloat
    public let dimen200: CGFloat
    public let dimen202: CGFloat
    public let dimen208: CGFloat
    public let dimen210: CGFloat
    public let dimen212: CGFloat
    public let dimen218: CGFloat
    public let dimen220: CGFloat
    public let dimen224: CGFloat
    public let dimen225: CGFloat
    public let dimen228: CGFloat
    public let dimen230: CGFloat
    public let dimen240: CGFloat
    public let dimen246: CGFloat
    public let dimen248: CGFloat
    public let dimen250: CGFloat
    public let dimen260: CGFloat
    public let dimen280: CGFloat
    public let dimen300: CGFloat
    public let dimen320: CGFloat
    public let dimen326: CGFloat
    public let dimen330: CGFloat
    public let dimen340: CGFloat
    public let dimen350: CGFloat
    public let dimen360: CGFloat
    public let dimen390: CGFloat
    public let dimen400: CGFloat
    public let dimen420: CGFloat
    public let dimen430: CGFloat
    public let dimen435: CGFloat
    public let dimen440: CGFloat
    public let dimen480: CGFloat
    public let dimen500: CGFloat
    public let dimen600: CGFloat
    public let dimen520: CGFloat
    public let dimen540: CGFloat
    public let dimen620: CGFloat
    public let dimen720: CGFloat
    public let dimen760: CGFloat
    public let dimen800: CGFloat
    public let dimen900: CGFloat
    public let dimen1000: CGFloat
    public let dimen1100: CGFloat
    public let dimen1250: CGFloat
    public let dimen1280: CGFloat

    public init(
        dimen00: CGFloat = 0,
        dimen00_5: CGFloat = 0.5,
        dimen00_6: CGFloat = 0.6,
        dimen00_8: CGFloat = 0.8,
        dimen01: CGFloat = 1,
        dimen01_2: CGFloat = 1.2,
        dimen01_4: CGFloat = 1.4,
        dimen01_5: CGFloat = 1.5,
        dimen01_8: CGFloat = 1.8,
        dimen02: CGFloat = 2,
        dimen02_2: CGFloat = 2.2,
        dimen02_5: CGFloat = 2.5,
        dimen03: CGFloat = 3,
        dimen03_5: CGFloat = 3.5,
        dimen04: CGFloat = 4,
        dimen05: CGFloat = 5,
        dimen05_5: CGFloat = 5.5,
        dimen06: CGFloat = 6,
        dimen07: CGFloat = 7,
        dimen08: CGFloat = 8,
        dimen09: CGFloat = 9,
        dimen10: CGFloat = 10,
        dimen11: CGFloat = 11,
        dimen12: CGFloat = 12,
        dimen13: CGFloat = 13,
        dimen14: CGFloat = 14,
        dimen15: CGFloat = 15,
        dimen16: CGFloat = 16,
        dimen17: CGFloat = 17,
        dimen18: CGFloat = 18,
        dimen19: CGFloat = 19,
        dimen20: CGFloat = 20,
        dimen21: CGFloat = 21,
        dimen22: CGFloat = 22,
        dimen23: CGFloat = 23,
        dimen24: CGFloat = 24,
        dimen25: CGFloat = 25,
        dimen26: CGFloat = 26,
        dimen28: CGFloat = 28,
        dimen29: CGFloat = 29,
        dimen30: CGFloat = 30,
        dimen31: CGFloat = 31,
        dimen32: CGFloat = 32,
        dimen33: CGFloat = 33,
        dimen34: CGFloat = 34,
        dimen35: CGFloat = 35,
        dimen36: CGFloat = 36,
        dimen38: CGFloat = 38,
        dimen40: CGFloat = 40,
        dimen42: CGFloat = 42,
        dimen43: CGFloat = 43,
        dimen44: CGFloat = 44,
        dimen45: CGFloat = 45,
        dimen46: CGFloat = 46,
        dimen47: CGFloat = 47,
        dimen48: CGFloat = 48,
        dimen50: CGFloat = 50,
        dimen52: CGFloat = 52,
        dimen54: CGFloat = 54,
        dimen56: CGFloat = 56,
        dimen58: CGFloat = 58,
        dimen60: CGFloat = 60,
        dimen62: CGFloat = 62,
        dimen63: CGFloat = 63,
        dimen64: CGFloat = 64,
        dimen65: CGFloat = 65,
        dimen66: CGFloat = 66,
        dimen68: CGFloat = 68,
        dimen70: CGFloat = 70,
        dimen72: CGFloat = 72,
        dimen74: CGFloat = 74,
        dimen76: CGFloat = 76,
        dimen78: CGFloat = 78,
        dimen80: CGFloat = 80,
        dimen82: CGFloat = 82,
        dimen84: CGFloat = 84,
        dimen85: CGFloat = 85,
        dimen86: CGFloat = 86,
        dimen88: CGFloat = 88,
        dimen90: CGFloat = 90,
        dimen92: CGFloat = 92,
        dimen94: CGFloat = 94,
        dimen96: CGFloat = 96,
        dimen100: CGFloat = 100,
        dimen104: CGFloat = 104,
        dimen106: CGFloat = 106,
        dimen108: CGFloat = 108,
        dimen110: CGFloat = 110,
        dimen112: CGFloat = 112,
        dimen115: CGFloat = 115,
        dimen116: CGFloat = 116,
        dimen118: CGFloat = 118,
        dimen120: CGFloat = 120,
        dimen125: CGFloat = 125,
        dimen126: CGFloat = 126,
        dimen128: CGFloat = 128,
        dimen130: CGFloat = 130,
        dimen132: CGFloat = 132,
        dimen136: CGFloat = 136,
        dimen140: CGFloat = 140,
        dimen144: CGFloat = 144,
        dimen145: CGFloat = 145,
        dimen146: CGFloat = 146,
        dimen148: CGFloat = 148,
        dimen150: CGFloat = 150,
        dimen152: CGFloat = 152,
        dimen156: CGFloat = 156,
        dimen160: CGFloat = 160,
        dimen168: CGFloat = 168,
        dimen170: CGFloat = 170,
        dimen172: CGFloat = 172,
        dimen174: CGFloat = 174,
        dimen176: CGFloat = 176,
        dimen180: CGFloat = 180,
        dimen188: CGFloat = 188,
        dimen190: CGFloat = 190,
        dimen194: CGFloat = 194,
        dimen196: CGFloat = 196,
        dimen200: CGFloat = 200,
        dimen202: CGFloat = 202,
        dimen208: CGFloat = 208,
        dimen210: CGFloat = 210,
        dimen212: CGFloat = 212,
        dimen218: CGFloat = 218,
        dimen220: CGFloat = 220,
        dimen224: CGFloat = 224,
        dimen225: CGFloat = 225,
        dimen228: CGFloat = 228,
        dimen230: CGFloat = 230,
        dimen240: CGFloat = 240,
        dimen246: CGFloat = 246,
        dimen248: CGFloat = 248,
        dimen250: CGFloat = 250,
        dimen260: CGFloat = 260,
        dimen280: CGFloat = 280,
        dimen300: CGFloat = 300,
        dimen320: CGFloat = 320,
        dimen326: CGFloat = 326,
        dimen330: CGFloat = 330,
        dimen340: CGFloat = 340,
        dimen350: CGFloat = 350,
        dimen360: CGFloat = 360,
        dimen390: CGFloat = 390,
        dimen400: CGFloat = 400,
        dimen420: CGFloat = 420,
        dimen430: CGFloat = 430,
        dimen435: CGFloat = 435,
        dimen440: CGFloat = 440,
        dimen480: CGFloat = 480,
        dimen500: CGFloat = 500,
        dimen600: CGFloat = 600,
        dimen520: CGFloat = 520,
        dimen540: CGFloat = 540,
        dimen620: CGFloat = 620,
        dimen720: CGFloat = 720,
        dimen760: CGFloat = 760,
        dimen800: CGFloat = 800,
        dimen900: CGFloat = 900,
        dimen1000: CGFloat = 1000,
        dimen1100: CGFloat = 1100,
        dimen1250: CGFloat = 1250,
        dimen1280: CGFloat = 1280
    ) {
        self.dimen00 = dimen00
        self.dimen00_5 = dimen00_5
        self.dimen00_6 = dimen00_6
        self.dimen00_8 = dimen00_8
        self.dimen01 = dimen01
        self.dimen01_2 = dimen01_2
        self.dimen01_4 = dimen01_4
        self.dimen01_5 = dimen01_5
        self.dimen01_8 = dimen01_8
        self.dimen02 = dimen02
        self.dimen02_2 = dimen02_2
        self.dimen02_5 = dimen02_5
        self.dimen03 = dimen03
        self.dimen03_5 = dimen03_5
        self.dimen04 = dimen04
        self.dimen05 = dimen05
        self.dimen05_5 = dimen05_5
        self.dimen06 = dimen06
        self.dimen07 = dimen07
        self.dimen08 = dimen08
        self.dimen09 = dimen09
        self.dimen10 = dimen10
        self.dimen11 = dimen11
        self.dimen12 = dimen12
        self.dimen13 = dimen13
        self.dimen14 = dimen14
        self.dimen15 = dimen15
        self.dimen16 = dimen16
        self.dimen17 = dimen17
        self.dimen18 = dimen18
        self.dimen19 = dimen19
        self.dimen20 = dimen20
        self.dimen21 = dimen21
        self.dimen22 = dimen22
        self.dimen23 = dimen23
        self.dimen24 = dimen24
        self.dimen25 = dimen25
        self.dimen26 = dimen26
        self.dimen28 = dimen28
        self.dimen29 = dimen29
        self.dimen30 = dimen30
        self.dimen31 = dimen31
        self.dimen32 = dimen32
        self.dimen33 = dimen33
        self.dimen34 = dimen34
        self.dimen35 = dimen35
        self.dimen36 = dimen36
        self.dimen38 = dimen38
        self.dimen40 = dimen40
        self.dimen42 = dimen42
        self.dimen43 = dimen43
        self.dimen44 = dimen44
        self.dimen45 = dimen45
        self.dimen46 = dimen46
        self.dimen47 = dimen47
        self.dimen48 = dimen48
        self.dimen50 = dimen50
        self.dimen52 = dimen52
        self.dimen54 = dimen54
        self.dimen56 = dimen56
        self.dimen58 = dimen58
        self.dimen60 = dimen60
        self.dimen62 = dimen62
        self.dimen63 = dimen63
        self.dimen64 = dimen64
        self.dimen65 = dimen65
        self.dimen66 = dimen66
        self.dimen68 = dimen68
        self.dimen70 = dimen70
        self.dimen72 = dimen72
        self.dimen74 = dimen74
        self.dimen76 = dimen76
        self.dimen78 = dimen78
        self.dimen80 = dimen80
        self.dimen82 = dimen82
        self.dimen84 = dimen84
        self.dimen85 = dimen85
        self.dimen86 = dimen86
        self.dimen88 = dimen88
        self.dimen90 = dimen90
        self.dimen92 = dimen92
        self.dimen94 = dimen94
        self.dimen96 = dimen96
        self.dimen100 = dimen100
        self.dimen104 = dimen104
        self.dimen106 = dimen106
        self.dimen108 = dimen108
        self.dimen110 = dimen110
        self.dimen112 = dimen112
        self.dimen115 = dimen115
        self.dimen116 = dimen116
        self.dimen118 = dimen118
        self.dimen120 = dimen120
        self.dimen125 = dimen125
        self.dimen126 = dimen126
        self.dimen128 = dimen128
        self.dimen130 = dimen130
        self.dimen132 = dimen132
        self.dimen136 = dimen136
        self.dimen140 = dimen140
        self.dimen144 = dimen144
        self.dimen145 = dimen145
        self.dimen146 = dimen146
        self.dimen148 = dimen148
        self.dimen150 = dimen150
        self.dimen152 = dimen152
        self.dimen156 = dimen156
        self.dimen160 = dimen160
        self.dimen168 = dimen168
        self.dimen170 = dimen170
        self.dimen172 = dimen172
        self.dimen174 = dimen174
        self.dimen176 = dimen176
        self.dimen180 = dimen180
        self.dimen188 = dimen188
        self.dimen190 = dimen190
        self.dimen194 = dimen194
        self.dimen196 = dimen196
        self.dimen200 = dimen200
        self.dimen202 = dimen202
        self.dimen208 = dimen208
        self.dimen210 = dimen210
        self.dimen212 = dimen212
        self.dimen218 = dimen218
        self.dimen220 = dimen220
        self.dimen224 = dimen224
        self.dimen225 = dimen225
        self.dimen228 = dimen228
        self.dimen230 = dimen230
        self.dimen240 = dimen240
        self.dimen246 = dimen246
        self.dimen248 = dimen248
        self.dimen250 = dimen250
        self.dimen260 = dimen260
        self.dimen280 = dimen280
        self.dimen300 = dimen300
        self.dimen320 = dimen320
        self.dimen326 = dimen326
        self.dimen330 = dimen330
        self.dimen340 = dimen340
        self.dimen350 = dimen350
        self.dimen360 = dimen360
        self.dimen390 = dimen390
        self.dimen400 = dimen400
        self.dimen420 = dimen420
        self.dimen430 = dimen430
        self.dimen435 = dimen435
        self.dimen440 = dimen440
        self.dimen480 = dimen480
        self.dimen500 = dimen500
        self.dimen600 = dimen600
        self.dimen520 = dimen520
        self.dimen540 = dimen540
        self.dimen620 = dimen620
        self.dimen720 = dimen720
        self.dimen760 = dimen760
        self.dimen800 = dimen800
        self.dimen900 = dimen900
        self.dimen1000 = dimen1000
        self.dimen1100 = dimen1100
        self.dimen1250 = dimen1250
        self.dimen1280 = dimen1280
    }
}
