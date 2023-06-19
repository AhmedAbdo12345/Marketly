package iti.mad.marketly.data.draftOrderInvoice

data class DraftOrderInvoiceX(
    val bcc: List<String>,
    val custom_message: String,
    val from: String,
    val subject: String,
    val to: String
)