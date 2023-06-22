package com.example.shopify.data.stripe.response

import com.google.gson.annotations.SerializedName

data class CreateEphemeralKeyResponse(
    @field:SerializedName("id")
    val id: String
)