package iti.mad.marketly.data.model.draftorder

import iti.mad.marketly.data.model.draftorderresponse.LineItem

data class DraftOrderBody(val line_items: List<LineItems>
,val customer: Customer,val shipping_address: ShippingAddress,val currency: String,val applied_discount: AppliedDiscount) {
}