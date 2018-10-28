package com.github.elbekD.bot.types

data class PassportData(val data: List<EncryptedPassportElement>,
                        val credentials: EncryptedCredentials)

data class PassportFile(val file_id: String,
                        val file_size: Int,
                        val file_date: Long)

data class EncryptedPassportElement(val type: String,
                                    val data: String?,
                                    val phone_number: String?,
                                    val email: String?,
                                    val files: List<PassportFile>?,
                                    val front_side: PassportFile?,
                                    val reverse_side: PassportFile?,
                                    val selfie: PassportFile?,
                                    val translation: List<PassportFile>?,
                                    val hash: String)

data class EncryptedCredentials(val data: String,
                                val hash: String,
                                val secret: String)

sealed class PassportElementError(val source: String,
                                  val type: String,
                                  val message: String)

class PassportElementErrorDataField(type: String,
                                    message: String,
                                    val field_name: String,
                                    val data_hash: String) : PassportElementError("data", type, message)

class PassportElementErrorFrontSide(type: String,
                                    message: String,
                                    val file_hash: String) : PassportElementError("front_side", type, message)

class PassportElementErrorReverseSide(type: String,
                                      message: String,
                                      file_hash: String) : PassportElementError("reverse_side", type, message)

class PassportElementErrorSelfie(type: String,
                                 message: String,
                                 val file_hash: String) : PassportElementError("selfie", type, message)

class PassportElementErrorFile(type: String,
                               message: String,
                               val file_hash: String) : PassportElementError("file", type, message)

class PassportElementErrorFiles(type: String,
                                message: String,
                                val file_hashes: List<String>) : PassportElementError("files", type, message)

class PassportElementErrorTranslationFile(type: String,
                                          message: String,
                                          val file_hash: String) : PassportElementError("translation_file", type, message)

class PassportElementErrorTranslationFiles(type: String,
                                           message: String,
                                           val file_hashes: List<String>) : PassportElementError("translation_files", type, message)

class PassportElementErrorUnspecified(type: String,
                                      message: String,
                                      val element_hash: String) : PassportElementError("unspecified", type, message)

enum class ElementTypes(val type: String) {
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