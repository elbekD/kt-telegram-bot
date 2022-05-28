package com.elbekd.bot.types

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonClassDiscriminator

@Serializable
public data class PassportData(
    @SerialName("data") val data: List<EncryptedPassportElement>,
    @SerialName("credentials") val credentials: EncryptedCredentials
)

@Serializable
public data class PassportFile(
    @SerialName("file_id") val file_id: String,
    @SerialName("file_unique_id") val file_unique_id: String,
    @SerialName("file_size") val file_size: Int,
    @SerialName("file_date") val file_date: Long
)

@Serializable
public data class EncryptedPassportElement(
    @SerialName("type") val type: ElementType,
    @SerialName("data") val data: String? = null,
    @SerialName("phone_number") val phoneNumber: String? = null,
    @SerialName("email") val email: String?,
    @SerialName("files") val files: List<PassportFile>? = null,
    @SerialName("front_side") val frontSide: PassportFile? = null,
    @SerialName("reverse_side") val reverseSide: PassportFile? = null,
    @SerialName("selfie") val selfie: PassportFile? = null,
    @SerialName("translation") val translation: List<PassportFile>? = null,
    @SerialName("hash") val hash: String
)

@Serializable
public data class EncryptedCredentials(
    @SerialName("data") val data: String,
    @SerialName("hash") val hash: String,
    @SerialName("secret") val secret: String
)

@Serializable
@JsonClassDiscriminator("source")
public sealed class PassportElementError(
    @SerialName("source") public val source: String
)

@Serializable
@SerialName("data")
public data class PassportElementErrorDataField(
    @SerialName("type") public val type: String,
    @SerialName("message") public val message: String,
    @SerialName("field_name") public val field_name: String,
    @SerialName("data_hash") public val data_hash: String
) : PassportElementError(source = "data")

@Serializable
@SerialName("front_side")
public data class PassportElementErrorFrontSide(
    @SerialName("type") public val type: String,
    @SerialName("message") public val message: String,
    @SerialName("file_hash") public val file_hash: String
) : PassportElementError(source = "front_side")

@Serializable
@SerialName("reverse_side")
public data class PassportElementErrorReverseSide(
    @SerialName("type") public val type: String,
    @SerialName("message") public val message: String,
    @SerialName("file_hash") public val file_hash: String
) : PassportElementError(source = "reverse_side")

@Serializable
@SerialName("selfie")
public data class PassportElementErrorSelfie(
    @SerialName("type") public val type: String,
    @SerialName("message") public val message: String,
    @SerialName("file_hash") public val file_hash: String
) : PassportElementError(source = "selfie")

@Serializable
@SerialName("file")
public data class PassportElementErrorFile(
    @SerialName("type") public val type: String,
    @SerialName("message") public val message: String,
    @SerialName("file_hash") public val file_hash: String
) : PassportElementError(source = "file")

@Serializable
@SerialName("files")
public data class PassportElementErrorFiles(
    @SerialName("type") public val type: String,
    @SerialName("message") public val message: String,
    @SerialName("file_hashes") public val file_hashes: List<String>
) : PassportElementError(source = "files")

@Serializable
@SerialName("translation_file")
public data class PassportElementErrorTranslationFile(
    @SerialName("type") public val type: String,
    @SerialName("message") public val message: String,
    @SerialName("file_hash") public val file_hash: String
) : PassportElementError(source = "translation_file")

@Serializable
@SerialName("translation_files")
public data class PassportElementErrorTranslationFiles(
    @SerialName("type") public val type: String,
    @SerialName("message") public val message: String,
    @SerialName("file_hashes") public val file_hashes: List<String>
) : PassportElementError(source = "translation_files")

@Serializable
@SerialName("unspecified")
public data class PassportElementErrorUnspecified(
    @SerialName("type") public val type: String,
    @SerialName("message") public val message: String,
    @SerialName("element_hash") public val element_hash: String
) : PassportElementError(source = "unspecified")

@Serializable
public enum class ElementType {
    @SerialName("personal_details")
    PERSONAL_DETAILS,

    @SerialName("passport")
    PASSPORT,

    @SerialName("driver_license")
    DRIVER_LICENSE,

    @SerialName("identity_card")
    IDENTITY_CARD,

    @SerialName("internal_passport")
    INTERNAL_PASSPORT,

    @SerialName("address")
    ADDRESS,

    @SerialName("utility_bill")
    UTILITY_BILL,

    @SerialName("bank_statement")
    BANK_STATEMENT,

    @SerialName("rental_agreement")
    RENTAL_AGREEMENT,

    @SerialName("passport_registration")
    PASSPORT_REGISTRATION,

    @SerialName("temporary_registration")
    TEMPORARY_REGISTRATION,

    @SerialName("phone_number")
    PHONE_NUMBER,

    @SerialName("email")
    EMAIL,
}