package com.jeluchu.jchucomponents.supabase.database

import com.jeluchu.jchucomponents.network.models.Failure
import com.jeluchu.jchucomponents.network.models.Resource
import kotlinx.coroutines.flow.Flow

class JchuSupabaseTableReference<T : Any> @PublishedApi internal constructor(
    @PublishedApi
    internal val database: JchuSupabaseDatabase,
    val definition: JchuSupabaseTable<T>
)

inline fun <reified T : Any> JchuSupabaseTableReference<T>.findAll(): Flow<Resource<Failure, List<T>>> =
    database.findAll(
        table = definition.name
    )

inline fun <reified T : Any> JchuSupabaseTableReference<T>.findOne(
    filter: JchuSupabaseFilter
): Flow<Resource<Failure, T>> =
    database.findOne(
        table = definition.name,
        filter = filter
    )

inline fun <reified T : Any> JchuSupabaseTableReference<T>.findOne(
    filters: List<JchuSupabaseFilter>
): Flow<Resource<Failure, T>> =
    database.findOne(
        table = definition.name,
        filters = filters
    )

inline fun <reified T : Any> JchuSupabaseTableReference<T>.findById(
    id: Any
): Flow<Resource<Failure, T>> =
    database.findById(
        table = definition.name,
        id = id,
        primaryKey = definition.primaryKey
    )

inline fun <reified T : Any> JchuSupabaseTableReference<T>.create(
    value: T
): Flow<Resource<Failure, T>> =
    database.create(
        table = definition.name,
        value = value
    )

inline fun <reified T : Any> JchuSupabaseTableReference<T>.createAll(
    values: List<T>
): Flow<Resource<Failure, List<T>>> =
    database.createAll(
        table = definition.name,
        values = values
    )

inline fun <reified T : Any> JchuSupabaseTableReference<T>.update(
    value: T,
    filter: JchuSupabaseFilter
): Flow<Resource<Failure, T>> =
    database.update(
        table = definition.name,
        value = value,
        filter = filter
    )

inline fun <reified T : Any> JchuSupabaseTableReference<T>.update(
    value: T,
    filters: List<JchuSupabaseFilter>
): Flow<Resource<Failure, T>> =
    database.update(
        table = definition.name,
        value = value,
        filters = filters
    )

inline fun <reified T : Any> JchuSupabaseTableReference<T>.updateById(
    id: Any,
    value: T
): Flow<Resource<Failure, T>> =
    database.updateById(
        table = definition.name,
        id = id,
        value = value,
        primaryKey = definition.primaryKey
    )

fun <T : Any> JchuSupabaseTableReference<T>.delete(
    filter: JchuSupabaseFilter
): Flow<Resource<Failure, Unit>> =
    database.delete(
        table = definition.name,
        filter = filter
    )

fun <T : Any> JchuSupabaseTableReference<T>.delete(
    filters: List<JchuSupabaseFilter>
): Flow<Resource<Failure, Unit>> =
    database.delete(
        table = definition.name,
        filters = filters
    )

fun <T : Any> JchuSupabaseTableReference<T>.deleteById(
    id: Any
): Flow<Resource<Failure, Unit>> =
    database.deleteById(
        table = definition.name,
        id = id,
        primaryKey = definition.primaryKey
    )
