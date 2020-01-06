package com.elbekD.bot.types

data class Poll(val id: String,
                val question: String,
                val options: List<PollOption>,
                val is_closed: Boolean)

data class PollOption(val text: String, val voter_count: Int)