package iti.mad.marketly.data.repository

import android.content.Context
import iti.mad.marketly.data.source.local.ILocalDataSource
import iti.mad.marketly.data.source.remote.IRemoteDataSource


class RepositoryImpl(
    val local: ILocalDataSource,
    val remote: IRemoteDataSource,
    val context: Context
    ) : IRepository {

}