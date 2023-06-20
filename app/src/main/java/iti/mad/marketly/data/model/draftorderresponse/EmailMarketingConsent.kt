package iti.mad.marketly.data.model.draftorderresponse

data class EmailMarketingConsent(
    val consent_updated_at: String,
    val opt_in_level: Any,
    val state: String
)