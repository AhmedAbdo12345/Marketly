package iti.workshop.admin.data.dto


data class DiscountCodeRequestAndResponse(
    val discount_code: DiscountCode
)
data class DiscountCode(
    val id: Int,
    val code: String,
    val price_rule_id: Int,
    val usage_count: Int?=null,
    val updated_at: String?=null,
    val created_at: String?=null,

    )