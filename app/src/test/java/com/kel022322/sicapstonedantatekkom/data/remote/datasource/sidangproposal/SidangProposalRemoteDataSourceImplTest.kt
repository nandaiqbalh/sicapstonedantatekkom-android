package com.kel022322.sicapstonedantatekkom.data.remote.datasource.sidangproposal

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.kel022322.sicapstonedantatekkom.data.remote.model.sidangproposal.response.SidangProposalByKelompokResponse
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

class SidangProposalRemoteDataSourceImplTest {

	@get:Rule
	var rule: TestRule = InstantTaskExecutorRule()

	private lateinit var dataSource: SidangProposalRemoteDataSource
	private lateinit var apiService: ApiService
	private val apiToken = "apiToken"

	@Before
	fun setUp() {
		apiService = mock()
		dataSource = SidangProposalRemoteDataSourceImpl(apiService)
	}

	@Test
	fun `getSidangProposalByKelompok should return correct response`() = runBlocking {
		// Arrange
		val expectedResponse = mockk<SidangProposalByKelompokResponse>()
		Mockito.`when`(apiService.getSidangProposalByKelompok(apiToken)).thenReturn(expectedResponse)

		// Act
		val actualResponse = dataSource.getSidangProposalByKelompok(apiToken)

		// Assert
		assertEquals("Returned response should match expected response", expectedResponse, actualResponse)
	}
}
