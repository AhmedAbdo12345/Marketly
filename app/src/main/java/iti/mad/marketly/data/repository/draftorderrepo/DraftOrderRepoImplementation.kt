package iti.mad.marketly.data.repository.draftorderrepo

import iti.mad.marketly.data.draftOrderInvoice.DraftOrderInvoice
import iti.mad.marketly.data.model.draftorder.DraftOrderBody
import iti.mad.marketly.data.model.draftorderresponse.DraftOrderResponse
import iti.mad.marketly.data.source.remote.IRemoteDataSource
import kotlinx.coroutines.flow.Flow

class DraftOrderRepoImplementation(private val remoteDataSource: IRemoteDataSource):DraftOrderRepoInterface {
    override suspend fun createDraftOrder(draftOrderBody: DraftOrderBody): Flow<DraftOrderResponse> = remoteDataSource.createDraftOrder(draftOrderBody)

    override suspend fun sendInvoice(invoice: DraftOrderInvoice,draftID:String): Flow<DraftOrderInvoice> = remoteDataSource.sendInvoice(invoice,draftID)

    override suspend fun completeOrder(draftID:String): Flow<DraftOrderResponse> = remoteDataSource.completeOrder(draftID)
}