package com.elbekD.bot.types

data class Poll(
    val id: String,
    val question: String,
    val options: List<PollOption>,
    val total_voter_count: Int,
    val is_closed: Boolean,
    val is_anonymous: Boolean,
    val type: String,
    val allows_multiple_answers: Boolean,
    val correct_option_id: Int?,
    val explanation: String?,
    val explanation_entities: List<MessageEntity>?,
    val open_period: Int?,
    val close_date: Long?
)

data class PollOption(val text: String, val voter_count: Int)

data class PollAnswer(val poll_id: String, val user: User, val option_ids: List<Int>)
