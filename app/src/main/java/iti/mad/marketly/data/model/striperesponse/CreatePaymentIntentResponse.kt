package com.example.shopify.data.stripe.response

import com.google.gson.annotations.SerializedName

data class CreatePaymentIntentResponse(
	@field:SerializedName("client_secret")
	val clientSecret: String
)