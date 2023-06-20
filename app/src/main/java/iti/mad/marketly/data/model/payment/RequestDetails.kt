package iti.mad.marketly.data.model.payment

data class RequestDetails(
    val accept_language: String,
    val ip_address: String,
    val user_agent: String
)