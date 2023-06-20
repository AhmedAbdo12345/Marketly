package iti.mad.marketly.data.model.payment

data class Payment(
    val amount: String,
    val request_details: RequestDetails,
    val session_id: String,
    val unique_token: String
)