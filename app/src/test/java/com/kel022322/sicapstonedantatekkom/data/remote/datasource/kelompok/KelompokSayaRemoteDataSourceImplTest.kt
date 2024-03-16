package com.kel022322.sicapstonedantatekkom.data.remote.datasource.kelompok

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.kel022322.sicapstonedantatekkom.data.remote.model.kelompok.addindividu.request.AddKelompokIndividuRemoteRequestBody
import com.kel022322.sicapstonedantatekkom.data.remote.model.kelompok.addindividu.response.AddKelompokIndividuRemoteResponse
import com.kel022322.sicapstonedantatekkom.data.remote.model.kelompok.addkelompok.request.AddKelompokPunyaKelompokRemoteRequestBody
import com.kel022322.sicapstonedantatekkom.data.remote.model.kelompok.addkelompok.response.AddKelompokPunyaKelompokRemoteResponse
import com.kel022322.sicapstonedantatekkom.data.remote.model.kelompok.index.response.KelompokSayaRemoteResponse
import com.kel022322.sicapstonedantatekkom.data.remote.model.kelompok.status.UpdateStatusKelompokBackwardRemoteResponse
import com.kel022322.sicapstonedantatekkom.data.remote.model.kelompok.status.UpdateStatusKelompokForwardRemoteResponse
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

class KelompokSayaRemoteDataSourceImplTest {

	@get:Rule
	var rule: TestRule = InstantTaskExecutorRule()

	private lateinit var dataSource: KelompokSayaRemoteDataSource
	private lateinit var apiService: ApiService
	private val apiToken = "apiToken"

	@Before
	fun setUp() {
		apiService = mock()
		dataSource = KelompokSayaRemoteDataSourceImpl(apiService)
	}

	@Test
	fun `getKelompokSaya should return correct response`() = runBlocking {
		// Arrange
		val expectedResponse = mockk<KelompokSayaRemoteResponse>()
		Mockito.`when`(apiService.getKelompokSaya(apiToken)).thenReturn(expectedResponse)

		// Act
		val actualResponse = dataSource.getKelompokSaya(apiToken)

		// Assert
		assertEquals("Returned response should match expected response", expectedResponse, actualResponse)
	}

	@Test
	fun `updateStatusKelompokForward should return correct response`() = runBlocking {
		// Arrange
		val expectedResponse = mockk<UpdateStatusKelompokForwardRemoteResponse>()
		Mockito.`when`(apiService.updateStatusKelompokForward(apiToken)).thenReturn(expectedResponse)

		// Act
		val actualResponse = dataSource.updateStatusKelompokForward(apiToken)

		// Assert
		assertEquals("Returned response should match expected response", expectedResponse, actualResponse)
	}

	@Test
	fun `updateStatusKelompokBackward should return correct response`() = runBlocking {
		// Arrange
		val expectedResponse = mockk<UpdateStatusKelompokBackwardRemoteResponse>()
		Mockito.`when`(apiService.updateStatusKelompokBackward(apiToken)).thenReturn(expectedResponse)

		// Act
		val actualResponse = dataSource.updateStatusKelompokBackward(apiToken)

		// Assert
		assertEquals("Returned response should match expected response", expectedResponse, actualResponse)
	}

	@Test
	fun `addKelompokIndividu should return correct response`() = runBlocking {
		// Arrange
		val requestBody = mockk<AddKelompokIndividuRemoteRequestBody>()
		val expectedResponse = mockk<AddKelompokIndividuRemoteResponse>()
		Mockito.`when`(apiService.addKelompokIndividu(apiToken, requestBody)).thenReturn(expectedResponse)

		// Act
		val actualResponse = dataSource.addKelompokIndividu(apiToken, requestBody)

		// Assert
		assertEquals("Returned response should match expected response", expectedResponse, actualResponse)
	}

	@Test
	fun `addKelompokPunyaKelompok should return correct response`() = runBlocking {
		// Arrange
		val requestBody = mockk<AddKelompokPunyaKelompokRemoteRequestBody>()
		val expectedResponse = mockk<AddKelompokPunyaKelompokRemoteResponse>()
		Mockito.`when`(apiService.addKelompokPunyaKelompok(apiToken, requestBody)).thenReturn(expectedResponse)

		// Act
		val actualResponse = dataSource.addKelompokPunyaKelompok(apiToken, requestBody)

		// Assert
		assertEquals("Returned response should match expected response", expectedResponse, actualResponse)
	}
}
