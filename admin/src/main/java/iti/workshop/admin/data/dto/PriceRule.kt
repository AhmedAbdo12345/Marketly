package iti.workshop.admin.data.dto

data class PriceRuleCodeListResponse(
    val price_rules: List<iti.workshop.admin.data.dto.PriceRule>
)

data class PriceRuleRequestAndResponse(
    val price_rule: iti.workshop.admin.data.dto.PriceRule?=null
)
data class PriceRule(
    val id: Int?=null,
    val allocation_method: String?=null,
    val created_at: String?=null,
    val customer_selection: String?=null,
    val ends_at: String?=null,
    val once_per_customer: Boolean?=null,
    val starts_at: String?=null,
    val target_selection: String?=null,
    val target_type: String?=null,
    val title: String?=null,
    val updated_at: String?=null,
    val value: String?=null,
    val value_type: String?=null
)