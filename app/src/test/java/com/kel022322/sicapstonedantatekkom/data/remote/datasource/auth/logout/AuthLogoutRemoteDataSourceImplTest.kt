package com.kel022322.sicapstonedantatekkom.data.remote.datasource.auth.logout

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.kel022322.sicapstonedantatekkom.data.remote.model.auth.logout.response.AuthLogoutRemoteResponse
import com.kel022322.sicapstonedantatekkom.data.remote.service.ApiService
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.mockito.Mockito
import org.mockito.kotlin.mock

class AuthLogoutRemoteDataSourceImplTest {

	@get:Rule
	var rule: TestRule = InstantTaskExecutorRule()

	private lateinit var dataSource: AuthLogoutRemoteDataSource
	private lateinit var apiService: ApiService

	@Before
	fun setUp() {
		apiService = mock()
		dataSource = AuthLogoutRemoteDataSourceImpl(apiService)
	}

	@Test
	fun `authLogout should return correct response`() = runBlocking {
		// Arrange
		val apiToken = "apiToken"
		val expectedResponse = mockk<AuthLogoutRemoteResponse>()
		Mockito.`when`(apiService.authLogout(apiToken)).thenReturn(expectedResponse)

		// Act
		val actualResponse = dataSource.authLogout(apiToken)

		// Assert
		assertEquals("Returned response should match expected response", expectedResponse, actualResponse)
	}
}
