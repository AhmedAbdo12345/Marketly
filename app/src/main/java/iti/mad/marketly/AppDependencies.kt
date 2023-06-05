package iti.mad.marketly

import android.content.Context
import iti.mad.marketly.data.repository.authRepository.AuthRepositoryImpl
import iti.mad.marketly.data.repository.category.CategoryRepo
import iti.mad.marketly.data.repository.category.CategoryRepoImpl
import iti.mad.marketly.data.source.remote.RemoteDataSource
import iti.mad.marketly.data.source.remote.retrofit.RetrofitInstance

object AppDependencies {
    lateinit var authRepository: AuthRepositoryImpl
    lateinit var categoryRepo: CategoryRepo
    fun initialization(appContext: Context) {
        val api = RetrofitInstance.api
        val remote = RemoteDataSource(api)
        authRepository = AuthRepositoryImpl(remote)
        categoryRepo = CategoryRepoImpl(api)
    }
}