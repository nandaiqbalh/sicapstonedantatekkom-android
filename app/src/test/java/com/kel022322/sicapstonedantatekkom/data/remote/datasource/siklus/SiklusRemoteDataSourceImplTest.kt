package com.kel022322.sicapstonedantatekkom.data.remote.datasource.siklus

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.kel022322.sicapstonedantatekkom.data.remote.model.siklus.response.SiklusRemoteResponse
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

class SiklusRemoteDataSourceImplTest {

	@get:Rule
	var rule: TestRule = InstantTaskExecutorRule()

	private lateinit var dataSource: SiklusRemoteDataSource
	private lateinit var apiService: ApiService
	private val apiToken = "apiToken"

	@Before
	fun setUp() {
		apiService = mock()
		dataSource = SiklusRemoteDataSourceImpl(apiService)
	}

	@Test
	fun `getSiklusCapstone should return correct response`() = runBlocking {
		// Arrange
		val expectedResponse = mockk<SiklusRemoteResponse>()
		Mockito.`when`(apiService.getSiklusCapstone(apiToken)).thenReturn(expectedResponse)

		// Act
		val actualResponse = dataSource.getSiklusCapstone(apiToken)

		// Assert
		assertEquals("Returned response should match expected response", expectedResponse, actualResponse)
	}
}
