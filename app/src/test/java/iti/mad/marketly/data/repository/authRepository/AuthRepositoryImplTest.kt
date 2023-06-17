package iti.mad.marketly.data.repository.authRepository

import iti.mad.marketly.data.model.customer.Customer
import iti.mad.marketly.data.model.customer.CustomerBody
import iti.mad.marketly.data.model.customer.CustomerResponse
import iti.mad.marketly.data.source.remote.FakeRemoteDataSource
import iti.mad.marketly.data.source.remote.IRemoteDataSource
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class AuthRepositoryImplTest {

    lateinit var repository: IAuthRepository
    lateinit var remoteDataSource: IRemoteDataSource

    @Before
    fun setUp() {
        remoteDataSource = FakeRemoteDataSource()
        repository = AuthRepositoryImpl(remoteDataSource)
    }

    @After
    fun tearDown() {
    }


    @Test
    fun registerUser() {
        lateinit var actual: CustomerBody
        val customer = CustomerBody(
            Customer(
                id = 1569788,
                first_name = "mahmoudsayed",
                email = "mahmoudsayed@gmail.com",
                currency = "EGP"
            )
        )
        runTest {
            repository.registerUser(customer).collect {
                actual = it
            }
        }
        Assert.assertEquals(customer, actual)
    }

    @Test
    fun loginWithEmail() {
        lateinit var actual: CustomerResponse
        val expected = CustomerResponse(
            listOf(
                Customer(
                    id = 1569788,
                    first_name = "mahmoudsayed",
                    email = "mahmoudsayed@gmail.com",
                    currency = "EGP"
                )
            )
        )
        runTest {
            repository.loginWithEmail("mahmoudsayed@gmail.com").collect {
                actual = it
            }
        }
        Assert.assertEquals(expected, actual)
    }

}