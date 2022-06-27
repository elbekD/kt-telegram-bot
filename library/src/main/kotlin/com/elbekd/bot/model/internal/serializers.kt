package com.elbekd.bot.model.internal

import com.elbekd.bot.model.ChatId
import com.elbekd.bot.types.InlineQueryResult
import com.elbekd.bot.types.InlineQueryResultArticle
import com.elbekd.bot.types.InlineQueryResultAudio
import com.elbekd.bot.types.InlineQueryResultCachedAudio
import com.elbekd.bot.types.InlineQueryResultCachedDocument
import com.elbekd.bot.types.InlineQueryResultCachedGif
import com.elbekd.bot.types.InlineQueryResultCachedMpeg4Gif
import com.elbekd.bot.types.InlineQueryResultCachedPhoto
import com.elbekd.bot.types.InlineQueryResultCachedSticker
import com.elbekd.bot.types.InlineQueryResultCachedVideo
import com.elbekd.bot.types.InlineQueryResultCachedVoice
import com.elbekd.bot.types.InlineQueryResultContact
import com.elbekd.bot.types.InlineQueryResultDocument
import com.elbekd.bot.types.InlineQueryResultGame
import com.elbekd.bot.types.InlineQueryResultGif
import com.elbekd.bot.types.InlineQueryResultLocation
import com.elbekd.bot.types.InlineQueryResultMpeg4Gif
import com.elbekd.bot.types.InlineQueryResultPhoto
import com.elbekd.bot.types.InlineQueryResultVenue
import com.elbekd.bot.types.InlineQueryResultVideo
import com.elbekd.bot.types.InlineQueryResultVoice
import com.elbekd.bot.types.InputContactMessageContent
import com.elbekd.bot.types.InputInvoiceMessageContent
import com.elbekd.bot.types.InputLocationMessageContent
import com.elbekd.bot.types.InputMessageContent
import com.elbekd.bot.types.InputTextMessageContent
import com.elbekd.bot.types.InputVenueMessageContent
import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerializationStrategy
import kotlinx.serialization.Serializer
import kotlinx.serialization.descriptors.PolymorphicKind
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.buildSerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

internal class ChatIdSerializer : KSerializer<ChatId> {

    override val descriptor = buildSerialDescriptor("ChatIdSerializer", PolymorphicKind.SEALED)

    override fun deserialize(decoder: Decoder): ChatId {
        try {
            val id = decoder.decodeString()
            return ChatId.StringId(id)
        } catch (ignored: ClassCastException) {
        }

        try {
            val id = decoder.decodeLong()
            return ChatId.IntegerId(id)
        } catch (ignored: ClassCastException) {
        }

        throw RuntimeException("Cannot deserialize chat_id")
    }

    override fun serialize(encoder: Encoder, value: ChatId) {
        when (value) {
            is ChatId.IntegerId -> encoder.encodeLong(value.id)
            is ChatId.StringId -> encoder.encodeString(value.id)
        }
    }
}

@Serializer(forClass = InlineQueryResult::class)
internal object InlineQueryResultSerializer : SerializationStrategy<InlineQueryResult> {

    override val descriptor = buildSerialDescriptor("InlineQueryResultSerializer", PolymorphicKind.SEALED)

    override fun serialize(encoder: Encoder, value: InlineQueryResult) {
        when (value) {
            is InlineQueryResultArticle ->
                encoder.encodeSerializableValue(InlineQueryResultArticle.serializer(), value)
            is InlineQueryResultPhoto ->
                encoder.encodeSerializableValue(InlineQueryResultPhoto.serializer(), value)
            is InlineQueryResultGif ->
                encoder.encodeSerializableValue(InlineQueryResultGif.serializer(), value)
            is InlineQueryResultMpeg4Gif ->
                encoder.encodeSerializableValue(InlineQueryResultMpeg4Gif.serializer(), value)
            is InlineQueryResultVideo ->
                encoder.encodeSerializableValue(InlineQueryResultVideo.serializer(), value)
            is InlineQueryResultAudio ->
                encoder.encodeSerializableValue(InlineQueryResultAudio.serializer(), value)
            is InlineQueryResultVoice ->
                encoder.encodeSerializableValue(InlineQueryResultVoice.serializer(), value)
            is InlineQueryResultDocument ->
                encoder.encodeSerializableValue(InlineQueryResultDocument.serializer(), value)
            is InlineQueryResultLocation ->
                encoder.encodeSerializableValue(InlineQueryResultLocation.serializer(), value)
            is InlineQueryResultVenue ->
                encoder.encodeSerializableValue(InlineQueryResultVenue.serializer(), value)
            is InlineQueryResultContact ->
                encoder.encodeSerializableValue(InlineQueryResultContact.serializer(), value)
            is InlineQueryResultGame ->
                encoder.encodeSerializableValue(InlineQueryResultGame.serializer(), value)
            is InlineQueryResultCachedPhoto ->
                encoder.encodeSerializableValue(InlineQueryResultCachedPhoto.serializer(), value)
            is InlineQueryResultCachedGif ->
                encoder.encodeSerializableValue(InlineQueryResultCachedGif.serializer(), value)
            is InlineQueryResultCachedMpeg4Gif ->
                encoder.encodeSerializableValue(InlineQueryResultCachedMpeg4Gif.serializer(), value)
            is InlineQueryResultCachedSticker ->
                encoder.encodeSerializableValue(InlineQueryResultCachedSticker.serializer(), value)
            is InlineQueryResultCachedDocument ->
                encoder.encodeSerializableValue(InlineQueryResultCachedDocument.serializer(), value)
            is InlineQueryResultCachedVideo ->
                encoder.encodeSerializableValue(InlineQueryResultCachedVideo.serializer(), value)
            is InlineQueryResultCachedVoice ->
                encoder.encodeSerializableValue(InlineQueryResultCachedVoice.serializer(), value)
            is InlineQueryResultCachedAudio ->
                encoder.encodeSerializableValue(InlineQueryResultCachedAudio.serializer(), value)
        }
    }
}

@Serializer(forClass = InputMessageContent::class)
internal object InputMessageContentSerializer : SerializationStrategy<InputMessageContent> {

    override val descriptor: SerialDescriptor =
        buildSerialDescriptor("InputMessageContentSerializer", PolymorphicKind.SEALED)

    override fun serialize(encoder: Encoder, value: InputMessageContent) {
        when (value) {
            is InputTextMessageContent ->
                encoder.encodeSerializableValue(InputTextMessageContent.serializer(), value)
            is InputLocationMessageContent ->
                encoder.encodeSerializableValue(InputLocationMessageContent.serializer(), value)
            is InputVenueMessageContent ->
                encoder.encodeSerializableValue(InputVenueMessageContent.serializer(), value)
            is InputContactMessageContent ->
                encoder.encodeSerializableValue(InputContactMessageContent.serializer(), value)
            is InputInvoiceMessageContent ->
                encoder.encodeSerializableValue(InputInvoiceMessageContent.serializer(), value)
        }
    }
}
