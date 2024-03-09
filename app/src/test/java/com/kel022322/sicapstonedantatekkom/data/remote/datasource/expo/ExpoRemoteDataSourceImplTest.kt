package com.kel022322.sicapstonedantatekkom.data.remote.datasource.expo

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.kel022322.sicapstonedantatekkom.data.remote.model.expo.daftar.request.DaftarExpoRemoteRequestBody
import com.kel022322.sicapstonedantatekkom.data.remote.model.expo.daftar.response.DaftarExpoRemoteResponse
import com.kel022322.sicapstonedantatekkom.data.remote.model.expo.index.response.ExpoIndexRemoteResponse
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

class ExpoRemoteDataSourceImplTest {

	@get:Rule
	var rule: TestRule = InstantTaskExecutorRule()

	private lateinit var dataSource: ExpoRemoteDataSource
	private lateinit var apiService: ApiService

	@Before
	fun setUp() {
		apiService = mock()
		dataSource = ExpoRemoteDataSourceImpl(apiService)
	}

	@Test
	fun `getDataExpo should return correct response`() = runBlocking {
		// Arrange
		val apiToken = "apiToken"
		val expectedResponse = mockk<ExpoIndexRemoteResponse>()
		Mockito.`when`(apiService.getDataExpo(apiToken)).thenReturn(expectedResponse)

		// Act
		val actualResponse = dataSource.getDataExpo(apiToken)

		// Assert
		assertEquals("Returned response should match expected response", expectedResponse, actualResponse)
	}

	@Test
	fun `daftarExpo should return correct response`() = runBlocking {
		// Arrange
		val apiToken = "apiToken"
		val requestBody = mockk<DaftarExpoRemoteRequestBody>()
		val expectedResponse = mockk<DaftarExpoRemoteResponse>()
		Mockito.`when`(apiService.daftarExpo(apiToken, requestBody)).thenReturn(expectedResponse)

		// Act
		val actualResponse = dataSource.daftarExpo(apiToken, requestBody)

		// Assert
		assertEquals("Returned response should match expected response", expectedResponse, actualResponse)
	}
}
