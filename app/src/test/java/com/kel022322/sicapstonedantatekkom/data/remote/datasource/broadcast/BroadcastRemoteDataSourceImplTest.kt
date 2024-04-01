package com.kel022322.sicapstonedantatekkom.data.remote.datasource.broadcast

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.kel022322.sicapstonedantatekkom.data.remote.model.broadcast.allbroadcast.AllBroadcastRemoteResponse
import com.kel022322.sicapstonedantatekkom.data.remote.model.broadcast.detail.request.BroadcastDetailRemoteRequestBody
import com.kel022322.sicapstonedantatekkom.data.remote.model.broadcast.detail.response.BroadcastDetailRemoteResponse
import com.kel022322.sicapstonedantatekkom.data.remote.model.broadcast.paginate.BroadcastPaginateRemoteResponse
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

class BroadcastRemoteDataSourceImplTest {

	@get:Rule
	var rule: TestRule = InstantTaskExecutorRule()

	private lateinit var dataSource: BroadcastRemoteDataSource
	private lateinit var apiService: ApiService

	@Before
	fun setUp() {
		apiService = mock()
		dataSource = BroadcastRemoteDataSourceImpl(apiService)
	}

	@Test
	fun `getBroadcast should return correct response`() = runBlocking {
		// Arrange
		val expectedResponse = mockk<AllBroadcastRemoteResponse>()
		Mockito.`when`(apiService.getBroadcast()).thenReturn(expectedResponse)

		// Act
		val actualResponse = dataSource.getBroadcast()

		// Assert
		assertEquals("Returned response should match expected response", expectedResponse, actualResponse)
	}

	@Test
	fun `getBroadcastHome should return correct response`() = runBlocking {
		// Arrange
		val expectedResponse = mockk<BroadcastPaginateRemoteResponse>()
		Mockito.`when`(apiService.getBroadcastHome()).thenReturn(expectedResponse)

		// Act
		val actualResponse = dataSource.getBroadcastHome()

		// Assert
		assertEquals("Returned response should match expected response", expectedResponse, actualResponse)
	}

	@Test
	fun `getBroadcastDetail should return correct response`() = runBlocking {
		// Arrange
		val requestBody = mockk<BroadcastDetailRemoteRequestBody>()
		val expectedResponse = mockk<BroadcastDetailRemoteResponse>()
		Mockito.`when`(apiService.getBroadcastDetail(requestBody)).thenReturn(expectedResponse)

		// Act
		val actualResponse = dataSource.getBroadcastDetail(requestBody)

		// Assert
		assertEquals("Returned response should match expected response", expectedResponse, actualResponse)
	}
}
