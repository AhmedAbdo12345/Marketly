package iti.workshop.admin.data.remote

import iti.workshop.admin.data.remote.retrofit.CallApi

class RemoteDataSource(
    private val api: CallApi
) : IRemoteDataSource {

}