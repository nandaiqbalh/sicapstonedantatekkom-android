package com.kel022322.sicapstonedantatekkom.data.remote.datasource.mahasiswa

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.kel022322.sicapstonedantatekkom.data.remote.model.mahasiswa.index.response.MahasiswaIndexRemoteResponse
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

class MahasiswaRemoteDataSourceImplTest {

	@get:Rule
	var rule: TestRule = InstantTaskExecutorRule()

	private lateinit var dataSource: MahasiswaRemoteDataSource
	private lateinit var apiService: ApiService
	private val apiToken = "apiToken"

	@Before
	fun setUp() {
		apiService = mock()
		dataSource = MahasiswaRemoteDataSourceImpl(apiService)
	}

	@Test
	fun `getDataMahasiswa should return correct response`() = runBlocking {
		// Arrange
		val expectedResponse = mockk<MahasiswaIndexRemoteResponse>()
		Mockito.`when`(apiService.getDataMahasiswa(apiToken)).thenReturn(expectedResponse)

		// Act
		val actualResponse = dataSource.getDataMahasiswa(apiToken)

		// Assert
		assertEquals("Returned response should match expected response", expectedResponse, actualResponse)
	}
}
