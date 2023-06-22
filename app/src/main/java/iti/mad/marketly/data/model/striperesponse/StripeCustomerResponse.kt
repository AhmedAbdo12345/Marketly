package com.example.shopify.data.stripe.response

import com.google.gson.annotations.SerializedName

data class StripeCustomerResponse(
	@field:SerializedName("id")
	val id: String
)