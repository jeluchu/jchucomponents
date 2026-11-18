package com.jeluchu.jchucomponents.supabase.di

import com.jeluchu.jchucomponents.supabase.JchuSupabase
import org.koin.mp.KoinPlatform

/** The Supabase client registered by `JchuConfig { supabase { } }`, or `null` when it was skipped or Koin is not started. */
fun jchuSupabaseOrNull(): JchuSupabase? = KoinPlatform.getKoinOrNull()?.getOrNull<JchuSupabase>()

/** Whether a Supabase client is available, i.e. the `supabase { }` block ran with a usable configuration. */
fun isJchuSupabaseConfigured(): Boolean = jchuSupabaseOrNull() != null
