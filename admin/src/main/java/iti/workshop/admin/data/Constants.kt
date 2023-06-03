package iti.workshop.admin.data

object Constants {
    private const val SHOPIFY_ACCOUNT = "mad-ism-43-3"
    private const val API_VERSION = "2023-04"

    const val BASE_URL = "https://$SHOPIFY_ACCOUNT.myshopify.com/admin/api/$API_VERSION"
    const val API_KEY = "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"

    // Network and Room cash
    const val MAX_AGE = 7
    const val  MAX_AGE_MILLI = MAX_AGE * 24 * 60 * 60 * 1000
}