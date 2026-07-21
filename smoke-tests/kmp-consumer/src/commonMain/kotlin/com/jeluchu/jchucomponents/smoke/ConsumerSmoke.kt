package com.jeluchu.jchucomponents.smoke

import com.jeluchu.jchucomponents.foundation.JchuComponents
import com.jeluchu.jchucomponents.foundation.time.YearMonth
import com.jeluchu.jchucomponents.network.api.ApiEndpoint
import com.jeluchu.jchucomponents.network.api.ApiVersion
import com.jeluchu.jchucomponents.network.models.Resource
import com.jeluchu.jchucomponents.prefs.JchuPreferenceKeys
import com.jeluchu.jchucomponents.supabase.JchuSupabaseConfig
import com.jeluchu.jchucomponents.supabase.database.JchuSupabaseTable
import com.jeluchu.pay.revenuecat.models.SubscriptionState
import com.jeluchu.qr.BarcodeFormat

/**
 * Compiles against the artifacts published to Maven Local. This intentionally
 * touches every public KMP module so missing variants or dependencies fail CI.
 */
object ConsumerSmoke {
    val version: String = JchuComponents.VERSION
    val month: YearMonth = YearMonth.of(year = 2026, month = 7)
    val endpoint: ApiEndpoint = SmokeEndpoint
    val resource: Resource<Nothing, String> = Resource.Success("ready")
    val preferenceKey = JchuPreferenceKeys.boolean("smoke-ready")
    val subscriptionState: SubscriptionState = SubscriptionState.ACTIVE
    val barcodeFormat: BarcodeFormat = BarcodeFormat.QR_CODE
    val supabaseConfig = JchuSupabaseConfig(
        url = "https://example.supabase.co",
        publishableKey = "public-anon-key"
    )
    val supabaseTable: JchuSupabaseTable<SmokeSupabaseRow> = SmokeSupabaseTable
}

private object SmokeEndpoint : ApiEndpoint(ApiVersion.V1) {
    override val endpoint: String = "smoke"
}

data class SmokeSupabaseRow(
    val id: String
)

private object SmokeSupabaseTable : JchuSupabaseTable<SmokeSupabaseRow> {
    override val name: String = "smoke_rows"
}
