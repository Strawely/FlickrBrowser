package ru.solom.flickr.data.network

import com.squareup.moshi.FromJson
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.JsonDataException
import com.squareup.moshi.JsonQualifier
import com.squareup.moshi.JsonReader
import com.squareup.moshi.JsonWriter
import com.squareup.moshi.Moshi
import com.squareup.moshi.ToJson
import com.squareup.moshi.rawType
import java.lang.reflect.Type

/**
 * Use to mark DTO class, that can be replaced with [Error] DTO from server
 */
@JsonQualifier
@Target(AnnotationTarget.CLASS)
annotation class Fallible

/**
 * Need to correctly proceed error DTOs, because on failure we receive absolutely different DTO
 */
class FallibleAdapter<T>(
    private val delegate: JsonAdapter<T>,
    private val errorAdapter: JsonAdapter<Error>
) : JsonAdapter<T>() {
    @FromJson
    @Suppress("SwallowedException")
    override fun fromJson(reader: JsonReader): T? {
        val errorReader = reader.peekJson()
        return try {
            delegate.fromJson(reader)
        } catch (e: JsonDataException) {
            val err = errorAdapter.fromJson(errorReader)
            throw IllegalArgumentException(err?.message ?: e.message)
        }
    }

    @ToJson
    override fun toJson(writer: JsonWriter, value: T?) {
        writer.jsonValue(value)
    }

    companion object {
        val FACTORY = object : Factory {
            override fun create(
                type: Type,
                annotations: MutableSet<out Annotation>,
                moshi: Moshi
            ) = if (type.rawType.isAnnotationPresent(Fallible::class.java)) {
                createAdapter(type.rawType, type, annotations, moshi)
            } else {
                null
            }
        }

        private fun <T> Factory.createAdapter(
            clazz: Class<T>, // used for correct defining generic
            type: Type,
            annotations: Set<Annotation>?,
            moshi: Moshi,
        ): FallibleAdapter<T>? {
            val delegate = moshi.nextAdapter<T>(this, type, annotations)
            val errorAdapter = moshi.nextAdapter<Error>(
                skipPast = this,
                type = Error::class.java.rawType,
                emptySet()
            )
            return if (delegate != null && errorAdapter != null) {
                FallibleAdapter(delegate, errorAdapter)
            } else {
                null
            }
        }

        private fun <T> Moshi.nextAdapter(
            skipPast: Factory,
            type: Type,
            annotations: Set<Annotation>?
        ): JsonAdapter<T>? {
            return nextAdapter(skipPast, type, annotations)
        }
    }
}

data class Error(
    val message: String
)
