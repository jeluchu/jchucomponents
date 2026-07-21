package com.jeluchu.jchucomponents.supabase.database

data class JchuSupabaseFilter(
    val column: String,
    val value: Any
) {
    companion object {
        fun eq(
            column: String,
            value: Any
        ): JchuSupabaseFilter =
            JchuSupabaseFilter(
                column = column,
                value = value
            )
    }
}
