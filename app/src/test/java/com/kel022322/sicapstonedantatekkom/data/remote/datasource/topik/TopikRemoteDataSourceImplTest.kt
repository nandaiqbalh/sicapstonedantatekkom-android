package com.kel022322.sicapstonedantatekkom.data.remote.datasource.topik

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.kel022322.sicapstonedantatekkom.data.remote.model.topik.response.TopikRemoteResponse
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

class TopikRemoteDataSourceImplTest {

	@get:Rule
	var rule: TestRule = InstantTaskExecutorRule()

	private lateinit var dataSource: TopikRemoteDataSource
	private lateinit var apiService: ApiService
	private val apiToken = "apiToken"

	@Before
	fun setUp() {
		apiService = mock()
		dataSource = TopikRemoteDataSourceImpl(apiService)
	}

	@Test
	fun `getTopikCapstone should return correct response`() = runBlocking {
		// Arrange
		val expectedResponse = mockk<TopikRemoteResponse>()
		Mockito.`when`(apiService.getTopikCapstone(apiToken)).thenReturn(expectedResponse)

		// Act
		val actualResponse = dataSource.getTopikCapstone(apiToken)

		// Assert
		assertEquals("Returned response should match expected response", expectedResponse, actualResponse)
	}
}
