package com.jeluchu.jchucomponents.supabase.database

import com.jeluchu.jchucomponents.network.models.Failure
import com.jeluchu.jchucomponents.network.models.Resource
import com.jeluchu.jchucomponents.supabase.flow.supabaseResource
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.postgrest.from
import kotlinx.coroutines.flow.Flow

class JchuSupabaseDatabase internal constructor(
    @PublishedApi
    internal val client: SupabaseClient
) {
    fun <T : Any> table(
        definition: JchuSupabaseTable<T>
    ): JchuSupabaseTableReference<T> =
        JchuSupabaseTableReference(
            database = this,
            definition = definition
        )

    inline fun <reified T : Any> findAll(
        table: String
    ): Flow<Resource<Failure, List<T>>> =
        supabaseResource {
            client.from(table).select().decodeList<T>()
        }

    inline fun <reified T : Any> findOne(
        table: String,
        filter: JchuSupabaseFilter
    ): Flow<Resource<Failure, T>> =
        findOne(
            table = table,
            filters = listOf(filter)
        )

    inline fun <reified T : Any> findOne(
        table: String,
        filters: List<JchuSupabaseFilter>
    ): Flow<Resource<Failure, T>> {
        requireSupabaseFilters(filters)
        return supabaseResource {
            client.from(table).select {
                filter {
                    filters.forEach { eq(it.column, it.value) }
                }
            }.decodeSingle<T>()
        }
    }

    inline fun <reified T : Any> findById(
        table: String,
        id: Any,
        primaryKey: String = DEFAULT_PRIMARY_KEY
    ): Flow<Resource<Failure, T>> =
        findOne(
            table = table,
            filter = JchuSupabaseFilter.eq(primaryKey, id)
        )

    inline fun <reified T : Any> create(
        table: String,
        value: T
    ): Flow<Resource<Failure, T>> =
        supabaseResource {
            client.from(table).insert(value) {
                select()
            }.decodeSingle<T>()
        }

    inline fun <reified T : Any> createAll(
        table: String,
        values: List<T>
    ): Flow<Resource<Failure, List<T>>> =
        supabaseResource {
            client.from(table).insert(values) {
                select()
            }.decodeList<T>()
        }

    inline fun <reified T : Any> update(
        table: String,
        value: T,
        filter: JchuSupabaseFilter
    ): Flow<Resource<Failure, T>> =
        update(
            table = table,
            value = value,
            filters = listOf(filter)
        )

    inline fun <reified T : Any> update(
        table: String,
        value: T,
        filters: List<JchuSupabaseFilter>
    ): Flow<Resource<Failure, T>> {
        requireSupabaseFilters(filters)
        return supabaseResource {
            client.from(table).update(value) {
                select()
                filter {
                    filters.forEach { eq(it.column, it.value) }
                }
            }.decodeSingle<T>()
        }
    }

    inline fun <reified T : Any> updateById(
        table: String,
        id: Any,
        value: T,
        primaryKey: String = DEFAULT_PRIMARY_KEY
    ): Flow<Resource<Failure, T>> =
        update(
            table = table,
            value = value,
            filter = JchuSupabaseFilter.eq(primaryKey, id)
        )

    fun delete(
        table: String,
        filter: JchuSupabaseFilter
    ): Flow<Resource<Failure, Unit>> =
        delete(
            table = table,
            filters = listOf(filter)
        )

    fun delete(
        table: String,
        filters: List<JchuSupabaseFilter>
    ): Flow<Resource<Failure, Unit>> {
        requireSupabaseFilters(filters)
        return supabaseResource {
            client.from(table).delete {
                filter {
                    filters.forEach { eq(it.column, it.value) }
                }
            }
        }
    }

    fun deleteById(
        table: String,
        id: Any,
        primaryKey: String = DEFAULT_PRIMARY_KEY
    ): Flow<Resource<Failure, Unit>> =
        delete(
            table = table,
            filter = JchuSupabaseFilter.eq(primaryKey, id)
        )

    companion object {
        const val DEFAULT_PRIMARY_KEY: String = "id"
    }
}

@PublishedApi
internal fun requireSupabaseFilters(filters: List<JchuSupabaseFilter>) {
    require(filters.isNotEmpty()) { "At least one Supabase filter is required." }
}
