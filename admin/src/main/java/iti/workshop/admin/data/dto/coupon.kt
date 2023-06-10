package iti.workshop.admin.data.dto

// --------- Discount Coupon -------- //
data class DiscountCodeListResponse(
    var discount_codes: List<DiscountCode> = emptyList()
)
data class DiscountCodeRequestAndResponse(
    val discount_code: DiscountCode
)
data class DiscountCode(
    val id: Long = -1,
    val code: String,
    val price_rule_id: Long?=null,
    val usage_count: Int?=null,
    val updated_at: String?=null,
    val created_at: String?=null
    )

// --------- Price Rule ---------- //
data class PriceRuleCodeListResponse(
    var price_rules: List<PriceRule>  = listOf()
)

data class PriceRuleRequestAndResponse(
    val price_rule: PriceRule?=null
)
data class PriceRule(
    val id: Long = -1,
    val allocation_method: String = "each",
    val customer_selection: String = "all",
    val target_selection: String = "entitled",
    val target_type: String = "shipping_line",
    val usage_limit: Int = 10,
    val once_per_customer: Boolean = true,
    val value_type: String ="percentage",

    // Date
    val starts_at: String?=null,
    val ends_at: String?=null,

    val title: String?=null,
    val value: String?=null,

    // Timestamp
    val created_at: String?=null,
    val updated_at: String?=null,

    )