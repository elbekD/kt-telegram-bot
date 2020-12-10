package com.elbekD.bot.types

public data class PassportData(
    val data: List<EncryptedPassportElement>,
    val credentials: EncryptedCredentials
)

public data class PassportFile(
    val file_id: String,
    val file_unique_id: String,
    val file_size: Int,
    val file_date: Long
)

public data class EncryptedPassportElement(
    val type: String,
    val data: String?,
    val phone_number: String?,
    val email: String?,
    val files: List<PassportFile>?,
    val front_side: PassportFile?,
    val reverse_side: PassportFile?,
    val selfie: PassportFile?,
    val translation: List<PassportFile>?,
    val hash: String
)

public data class EncryptedCredentials(
    val data: String,
    val hash: String,
    val secret: String
)

public sealed class PassportElementError(
    public val source: String,
    public val type: String,
    public val message: String
)

public class PassportElementErrorDataField(
    type: String,
    message: String,
    public val field_name: String,
    public val data_hash: String
) : PassportElementError("data", type, message)

public class PassportElementErrorFrontSide(
    type: String,
    message: String,
    public val file_hash: String
) : PassportElementError("front_side", type, message)

public class PassportElementErrorReverseSide(
    type: String,
    message: String,
    file_hash: String
) : PassportElementError("reverse_side", type, message)

public class PassportElementErrorSelfie(
    type: String,
    message: String,
    public val file_hash: String
) : PassportElementError("selfie", type, message)

public class PassportElementErrorFile(
    type: String,
    message: String,
    public val file_hash: String
) : PassportElementError("file", type, message)

public class PassportElementErrorFiles(
    type: String,
    message: String,
    public val file_hashes: List<String>
) : PassportElementError("files", type, message)

public class PassportElementErrorTranslationFile(
    type: String,
    message: String,
    public val file_hash: String
) : PassportElementError("translation_file", type, message)

public class PassportElementErrorTranslationFiles(
    type: String,
    message: String,
    public val file_hashes: List<String>
) : PassportElementError("translation_files", type, message)

public class PassportElementErrorUnspecified(
    type: String,
    message: String,
    public val element_hash: String
) : PassportElementError("unspecified", type, message)

public enum class ElementTypes(public val type: String) {
    PERSONAL_DETAILS("personal_details"),
    PASSPORT("passport"),
    DRIVER_LICENSE("driver_license"),
    IDENTITY_CARD("identity_card"),
    INTERNAL_PASSPORT("internal_passport"),
    ADDRESS("address"),
    UTILITY_BILL("utility_bill"),
    BANK_STATEMENT("bank_statement"),
    RENTAL_AGREEMENT("rental_agreement"),
    PASSPORT_REGISTRATION("passport_registration"),
    TEMPORARY_REGISTRATION("temporary_registration"),
    PHONE_NUMBER("phone_number"),
    EMAIL("email")
}