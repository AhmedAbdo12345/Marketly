package iti.mad.marketly

import android.content.Context
import iti.mad.marketly.data.repository.authRepository.AuthRepositoryImpl
import iti.mad.marketly.data.source.remote.RemoteDataSource
import iti.mad.marketly.data.source.remote.retrofit.RetrofitInstance

object AppDependencies {
    lateinit var authRepository: AuthRepositoryImpl

    fun initialization(appContext: Context) {
        val api = RetrofitInstance.api

        val remote = RemoteDataSource(api)
        authRepository = AuthRepositoryImpl(remote)
    }
}