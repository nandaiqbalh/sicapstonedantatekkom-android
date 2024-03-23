package com.kel022322.sicapstonedantatekkom.data.remote.datasource.dosen

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.kel022322.sicapstonedantatekkom.data.remote.model.dosen.getdosen.response.dosbing1.DosenRemoteResponse
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

class DosenRemoteDataSourceImplTest {

	@get:Rule
	var rule: TestRule = InstantTaskExecutorRule()

	private lateinit var dataSource: DosenRemoteDataSource
	private lateinit var apiService: ApiService

	@Before
	fun setUp() {
		apiService = mock()
		dataSource = DosenRemoteDataSourceImpl(apiService)
	}

	@Test
	fun `getDataDosen should return correct response`() = runBlocking {
		// Arrange
		val apiToken = "apiToken"
		val expectedResponse = mockk<DosenRemoteResponse>()
		Mockito.`when`(apiService.getDataDosen(apiToken)).thenReturn(expectedResponse)

		// Act
		val actualResponse = dataSource.getDataDosen(apiToken)

		// Assert
		assertEquals("Returned response should match expected response", expectedResponse, actualResponse)
	}
}
