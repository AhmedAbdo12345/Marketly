package iti.workshop.admin.data

object Constants {
    private const val SHOPIFY_ACCOUNT = "mad-ism-43-3"
    private const val API_VERSION = "2023-04"

    const val BASE_URL = "https://${iti.workshop.admin.data.Constants.SHOPIFY_ACCOUNT}.myshopify.com/admin/api/${iti.workshop.admin.data.Constants.API_VERSION}/"
    const val API_KEY = "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"

    const val STORAGE_PATH = "gs://ecommerce-shopify-bc848.appspot.com"
    //SharedPref Keys
    const val CURRENCY="curr"
    const val DEFAULTADDRESS="defAddress"
    const val EXCHANGERATE = "Rate"

    const val FIREBASE_USER_ID: String="FIREBASE_USER_ID"
    const val USER_ID: String = "USER_ID"
    const val USER_EMAIL: String = "USER_EMAIL"
    const val USER_NAME: String = "USER_NAME"
    const val IS_LOGIN: String = "isLogin"

}