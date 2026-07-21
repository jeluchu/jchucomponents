package com.jeluchu.jchucomponents.supabase.database

interface JchuSupabaseTable<T : Any> {
    val name: String
    val primaryKey: String
        get() = JchuSupabaseDatabase.DEFAULT_PRIMARY_KEY
}
