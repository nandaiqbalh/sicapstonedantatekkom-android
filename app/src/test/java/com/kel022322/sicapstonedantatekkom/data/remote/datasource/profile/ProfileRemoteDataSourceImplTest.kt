package com.kel022322.sicapstonedantatekkom.data.remote.datasource.profile

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.kel022322.sicapstonedantatekkom.data.remote.model.profile.index.response.ProfileRemoteResponse
import com.kel022322.sicapstonedantatekkom.data.remote.model.profile.update.request.UpdateProfileRemoteRequestBody
import com.kel022322.sicapstonedantatekkom.data.remote.model.profile.update.response.UpdateProfileRemoteResponse
import com.kel022322.sicapstonedantatekkom.data.remote.model.profile.updatepassword.request.UpdatePasswordRemoteRequestBody
import com.kel022322.sicapstonedantatekkom.data.remote.model.profile.updatepassword.response.UpdatePasswordRemoteResponse
import com.kel022322.sicapstonedantatekkom.data.remote.model.profile.updatephoto.response.UpdateProfilePhotoRemoteResponse
import com.kel022322.sicapstonedantatekkom.data.remote.service.ApiService
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import okhttp3.MultipartBody
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.mockito.Mockito
import org.mockito.kotlin.mock

@ExperimentalCoroutinesApi
class ProfileRemoteDataSourceImplTest {

	@get:Rule
	var rule: TestRule = InstantTaskExecutorRule()

	private lateinit var dataSource: ProfileRemoteDataSource
	private lateinit var apiService: ApiService
	private val apiToken = "apiToken"

	@Before
	fun setUp() {
		apiService = mock()
		dataSource = ProfileRemoteDataSourceImpl(apiService)
	}

	@Test
	fun `getMahasiswaProfile should return correct profile`() = runBlocking {
		// Arrange
		val expectedProfile = mockk<ProfileRemoteResponse>()
		Mockito.`when`(apiService.getMahasiswaProfile(apiToken)).thenReturn(expectedProfile)

		// Act
		val actualProfile = dataSource.getMahasiswaProfile(apiToken)

		// Assert
		assertEquals("Returned profile should match expected profile", expectedProfile, actualProfile)
	}

	@Test
	fun `updateMahasiswaProfile should return correct response`() = runBlocking {
		// Arrange
		val requestBody = mockk<UpdateProfileRemoteRequestBody>()
		val expectedResponse = mockk<UpdateProfileRemoteResponse>()
		Mockito.`when`(apiService.updateMahasiswaProfile(apiToken, requestBody)).thenReturn(expectedResponse)

		// Act
		val actualResponse = dataSource.updateMahasiswaProfile(apiToken, requestBody)

		// Assert
		assertEquals("Returned response should match expected response", expectedResponse, actualResponse)
	}

	@Test
	fun `updatePasswordProfile should return correct response`() = runBlocking {
		// Arrange
		val requestBody = mockk<UpdatePasswordRemoteRequestBody>()
		val expectedResponse = mockk<UpdatePasswordRemoteResponse>()
		Mockito.`when`(apiService.updatePasswordProfile(apiToken, requestBody)).thenReturn(expectedResponse)

		// Act
		val actualResponse = dataSource.updatePasswordProfile(apiToken, requestBody)

		// Assert
		assertEquals("Returned response should match expected response", expectedResponse, actualResponse)
	}

	@Test
	fun `updatePhotoProfile should return correct response`() = runBlocking {
		// Arrange
		val requestBody = mockk<MultipartBody.Part>()
		val expectedResponse = mockk<UpdateProfilePhotoRemoteResponse>()
		Mockito.`when`(apiService.updatePhotoProfile(apiToken, requestBody)).thenReturn(expectedResponse)

		// Act
		val actualResponse = dataSource.updatePhotoProfile(apiToken, requestBody)

		// Assert
		assertEquals("Returned response should match expected response", expectedResponse, actualResponse)
	}
}