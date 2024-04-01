package com.kel022322.sicapstonedantatekkom.data.remote.datasource.sidangta

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.kel022322.sicapstonedantatekkom.data.remote.model.sidangta.daftar.request.DaftarSidangTARemoteRequestBody
import com.kel022322.sicapstonedantatekkom.data.remote.model.sidangta.daftar.response.DaftarSidangTARemoteResponse
import com.kel022322.sicapstonedantatekkom.data.remote.model.sidangta.index.response.SidangTARemoteResponse
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

class SidangTARemoteDataSourceImplTest {

	@get:Rule
	var rule: TestRule = InstantTaskExecutorRule()

	private lateinit var dataSource: SidangTARemoteDataSource
	private lateinit var apiService: ApiService
	private val apiToken = "apiToken"

	@Before
	fun setUp() {
		apiService = mock()
		dataSource = SidangTARemoteDataSourceImpl(apiService)
	}

	@Test
	fun `getJadwalSidangTA should return correct response`() = runBlocking {
		// Arrange
		val expectedResponse = mockk<SidangTARemoteResponse>()
		Mockito.`when`(apiService.getJadwalSidangTA(apiToken)).thenReturn(expectedResponse)

		// Act
		val actualResponse = dataSource.getJadwalSidangTA(apiToken)

		// Assert
		assertEquals("Returned response should match expected response", expectedResponse, actualResponse)
	}

	@Test
	fun `daftarSidangTA should return correct response`() = runBlocking {
		// Arrange
		val requestBody = mockk<DaftarSidangTARemoteRequestBody>()
		val expectedResponse = mockk<DaftarSidangTARemoteResponse>()
		Mockito.`when`(apiService.daftarSidangTA(apiToken, requestBody)).thenReturn(expectedResponse)

		// Act
		val actualResponse = dataSource.daftarSidangTA(apiToken, requestBody)

		// Assert
		assertEquals("Returned response should match expected response", expectedResponse, actualResponse)
	}
}
