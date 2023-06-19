package iti.mad.marketly.data.repository.draftorderrepo

import iti.mad.marketly.data.draftOrderInvoice.DraftOrderInvoice
import iti.mad.marketly.data.model.draftorder.DraftOrderBody
import iti.mad.marketly.data.model.draftorderresponse.DraftOrderResponse
import kotlinx.coroutines.flow.Flow

interface DraftOrderRepoInterface {
    suspend fun createDraftOrder(draftOrderBody: DraftOrderBody): Flow<DraftOrderResponse>
    suspend fun sendInvoice(invoice: DraftOrderInvoice,draftID:String): Flow<DraftOrderInvoice>
    suspend fun completeOrder(draftID:String): Flow<DraftOrderResponse>

}