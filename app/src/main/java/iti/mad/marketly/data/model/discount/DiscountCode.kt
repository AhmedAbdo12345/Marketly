package iti.mad.marketly.data.model.discount

data class DiscountCode(
    val code: String,
    val created_at: String,
    val id: Long,
    var price_rule_id: Long,//important
    val updated_at: String,
    val usage_count: Int//important
)