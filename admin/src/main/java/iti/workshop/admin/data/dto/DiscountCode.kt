package iti.workshop.admin.data.dto


data class DiscountCodeListResponse(
    val discount_codes: List<DiscountCode>
)
data class DiscountCodeRequestAndResponse(
    val discount_code: DiscountCode
)
data class DiscountCode(
    val id: Int?=null,
    val code: String,
    val price_rule_id: Int?=null,
    val usage_count: Int?=null,
    val updated_at: String?=null,
    val created_at: String?=null,

    )