package com.kel022322.sicapstonedantatekkom.data.remote.datasource.auth.login

import androidx.arch.core.executor.testing.InstantTaskExecutorRule

import com.kel022322.sicapstonedantatekkom.data.remote.model.auth.login.request.AuthLoginRequestBody
import com.kel022322.sicapstonedantatekkom.data.remote.model.auth.login.response.AuthLoginRemoteResponse
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

class AuthLoginRemoteDataSourceImplTest {

	@get:Rule
	var rule: TestRule = InstantTaskExecutorRule()

	private lateinit var dataSource: AuthLoginRemoteDataSource
	private lateinit var apiService: ApiService

	@Before
	fun setUp() {
		apiService = mock()
		dataSource = AuthLoginRemoteDataSourceImpl(apiService)
	}

	@Test
	fun `authLogin should return correct response`() = runBlocking {
		// Arrange
		val requestBody = mockk<AuthLoginRequestBody>()
		val expectedResponse = mockk<AuthLoginRemoteResponse>()
		Mockito.`when`(apiService.authLogin(requestBody)).thenReturn(expectedResponse)

		// Act
		val actualResponse = dataSource.authLogin(requestBody)

		// Assert
		assertEquals("Returned response should match expected response", expectedResponse, actualResponse)
	}
}
