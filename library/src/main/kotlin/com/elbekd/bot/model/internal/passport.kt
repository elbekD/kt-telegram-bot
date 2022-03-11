package com.elbekd.bot.model.internal

import com.elbekd.bot.internal.ApiConstants
import com.elbekd.bot.types.PassportElementError
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal class SetPassportDataErrors(
    @SerialName(ApiConstants.USER_ID) val userId: Long,
    @SerialName(ApiConstants.ERRORS) val errors: List<PassportElementError>
)