package iti.mad.marketly.data.source.remote

import iti.mad.marketly.data.source.remote.retrofit.CallApi

class RemoteDataSource(
    private val api: CallApi
) : IRemoteDataSource {

}