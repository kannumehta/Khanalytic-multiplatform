package com.khanalytic.models

import com.khanalytic.models.InstantUtils.toLocalDateTimeUtc
import kotlinx.datetime.Instant
import kotlinx.datetime.toLocalDateTime
import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

class InstantSerializer: KSerializer<Instant> {

    override val descriptor: SerialDescriptor = Instant.serializer().descriptor
    override fun serialize(encoder: Encoder, value: Instant) {
        encoder.encodeString(value.toLocalDateTimeUtc().toString())
    }

    override fun deserialize(decoder: Decoder): Instant {
        return Instant.parse(decoder.decodeString())
    }
}
