package com.kel022322.sicapstonedantatekkom.data.remote.datasource.file

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.kel022322.sicapstonedantatekkom.data.remote.model.file.c100.response.UploadC100ProcessRemoteResponse
import com.kel022322.sicapstonedantatekkom.data.remote.model.file.c200.response.UploadC200ProcessRemoteResponse
import com.kel022322.sicapstonedantatekkom.data.remote.model.file.c300.response.UploadC300ProcessRemoteResponse
import com.kel022322.sicapstonedantatekkom.data.remote.model.file.c400.response.UploadC400ProcessRemoteResponse
import com.kel022322.sicapstonedantatekkom.data.remote.model.file.c500.response.UploadC500ProcessRemoteResponse
import com.kel022322.sicapstonedantatekkom.data.remote.model.file.index.response.FileIndexRemoteResponse
import com.kel022322.sicapstonedantatekkom.data.remote.model.file.laporan.response.UploadLaporanProcessRemoteResponse
import com.kel022322.sicapstonedantatekkom.data.remote.model.file.makalah.response.UploadMakalahProcessRemoteResponse
import com.kel022322.sicapstonedantatekkom.data.remote.service.ApiService
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import okhttp3.MultipartBody
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.mockito.Mockito
import org.mockito.kotlin.mock

class FileRemoteDataSourceImplTest {

	@get:Rule
	var rule: TestRule = InstantTaskExecutorRule()

	private lateinit var dataSource: FileRemoteDataSource
	private lateinit var apiService: ApiService

	@Before
	fun setUp() {
		apiService = mock()
		dataSource = FileRemoteDataSourceImpl(apiService)
	}

	@Test
	fun `getFileIndex should return correct response`() = runBlocking {
		// Arrange
		val apiToken = "apiToken"
		val expectedResponse = mockk<FileIndexRemoteResponse>()
		Mockito.`when`(apiService.getFileIndex(apiToken)).thenReturn(expectedResponse)

		// Act
		val actualResponse = dataSource.getFileIndex(apiToken)

		// Assert
		assertEquals("Returned response should match expected response", expectedResponse, actualResponse)
	}

	@Test
	fun `uploadMakalahProcess should return correct response`() = runBlocking {
		// Arrange
		val apiToken = "apiToken"
		val makalah = mockk<MultipartBody.Part>()
		val expectedResponse = mockk<UploadMakalahProcessRemoteResponse>()
		Mockito.`when`(apiService.uploadMakalahProcess(apiToken, makalah)).thenReturn(expectedResponse)

		// Act
		val actualResponse = dataSource.uploadMakalahProcess(apiToken, makalah)

		// Assert
		assertEquals("Returned response should match expected response", expectedResponse, actualResponse)
	}

	@Test
	fun `uploadLaporanProcess should return correct response`() = runBlocking {
		// Arrange
		val apiToken = "apiToken"
		val laporanTa = mockk<MultipartBody.Part>()
		val expectedResponse = mockk<UploadLaporanProcessRemoteResponse>()
		Mockito.`when`(apiService.uploadLaporanProcess(apiToken, laporanTa)).thenReturn(expectedResponse)

		// Act
		val actualResponse = dataSource.uploadLaporanProcess(apiToken, laporanTa)

		// Assert
		assertEquals("Returned response should match expected response", expectedResponse, actualResponse)
	}

	@Test
	fun `uploadC100Process should return correct response`() = runBlocking {
		// Arrange
		val apiToken = "apiToken"
		val idKelompok = "idKelompok"
		val c100 = mockk<MultipartBody.Part>()
		val expectedResponse = mockk<UploadC100ProcessRemoteResponse>()
		Mockito.`when`(apiService.uploadC100Process(apiToken, idKelompok, c100)).thenReturn(expectedResponse)

		// Act
		val actualResponse = dataSource.uploadC100Process(apiToken, idKelompok, c100)

		// Assert
		assertEquals("Returned response should match expected response", expectedResponse, actualResponse)
	}

	@Test
	fun `uploadC200Process should return correct response`() = runBlocking {
		// Arrange
		val apiToken = "apiToken"
		val idKelompok = "idKelompok"
		val c200 = mockk<MultipartBody.Part>()
		val expectedResponse = mockk<UploadC200ProcessRemoteResponse>()
		Mockito.`when`(apiService.uploadC200Process(apiToken, idKelompok, c200)).thenReturn(expectedResponse)

		// Act
		val actualResponse = dataSource.uploadC200Process(apiToken, idKelompok, c200)

		// Assert
		assertEquals("Returned response should match expected response", expectedResponse, actualResponse)
	}

	@Test
	fun `uploadC300Process should return correct response`() = runBlocking {
		// Arrange
		val apiToken = "apiToken"
		val idKelompok = "idKelompok"
		val c300 = mockk<MultipartBody.Part>()
		val expectedResponse = mockk<UploadC300ProcessRemoteResponse>()
		Mockito.`when`(apiService.uploadC300Process(apiToken, idKelompok, c300)).thenReturn(expectedResponse)

		// Act
		val actualResponse = dataSource.uploadC300Process(apiToken, idKelompok, c300)

		// Assert
		assertEquals("Returned response should match expected response", expectedResponse, actualResponse)
	}

	@Test
	fun `uploadC400Process should return correct response`() = runBlocking {
		// Arrange
		val apiToken = "apiToken"
		val idKelompok = "idKelompok"
		val c400 = mockk<MultipartBody.Part>()
		val expectedResponse = mockk<UploadC400ProcessRemoteResponse>()
		Mockito.`when`(apiService.uploadC400Process(apiToken, idKelompok, c400)).thenReturn(expectedResponse)

		// Act
		val actualResponse = dataSource.uploadC400Process(apiToken, idKelompok, c400)

		// Assert
		assertEquals("Returned response should match expected response", expectedResponse, actualResponse)
	}

	@Test
	fun `uploadC500Process should return correct response`() = runBlocking {
		// Arrange
		val apiToken = "apiToken"
		val idKelompok = "idKelompok"
		val c500 = mockk<MultipartBody.Part>()
		val expectedResponse = mockk<UploadC500ProcessRemoteResponse>()
		Mockito.`when`(apiService.uploadC500Process(apiToken, idKelompok, c500)).thenReturn(expectedResponse)

		// Act
		val actualResponse = dataSource.uploadC500Process(apiToken, idKelompok, c500)

		// Assert
		assertEquals("Returned response should match expected response", expectedResponse, actualResponse)
	}

}