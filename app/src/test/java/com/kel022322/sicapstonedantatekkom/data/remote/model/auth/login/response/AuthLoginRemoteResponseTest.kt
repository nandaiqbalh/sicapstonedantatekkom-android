package com.kel022322.sicapstonedantatekkom.data.remote.model.auth.login.response

import org.junit.Assert.assertEquals
import org.junit.Test

class AuthLoginRemoteResponseTest {

	@Test
	fun `create AuthLoginRemoteResponse with valid data`() {
		// Arrange
		val status = "success"
		val success = true
		val message = "Login successful"
		val userData = UserData(
			"Address",
			2024,
			"apiToken123",
			"admin",
			"2024-03-10",
			"3.75",
			"Male",
			"admin",
			"2024-03-10",
			"1234567890",
			"123456",
			"1",
			100,
			"2024-03-10",
			"1",
			"user@example.com",
			1L,
			"user.jpg",
			"/images/",
			"http://example.com/images/user.jpg",
			"John Doe"
		)

		// Act
		val response = AuthLoginRemoteResponse(status, success, message, userData)

		// Assert
		assertEquals("Status should match", status, response.status)
		assertEquals("Success should match", success, response.success)
		assertEquals("Message should match", message, response.message)
		assertEquals("UserData should match", userData, response.userData)
	}

	@Test
	fun `create UserData with valid data`() {
		// Arrange
		val userData = UserData(
			"Address",
			2024,
			"apiToken123",
			"admin",
			"2024-03-10",
			"3.75",
			"Male",
			"admin",
			"2024-03-10",
			"1234567890",
			"123456",
			"1",
			100,
			"2024-03-10",
			"1",
			"user@example.com",
			1L,
			"user.jpg",
			"/images/",
			"http://example.com/images/user.jpg",
			"John Doe"
		)

		// Assert
		// Assert
		assertEquals("Address should match", "Address", userData.alamat)
		assertEquals("Angkatan should match", 2024, userData.angkatan)
		assertEquals("API token should match", "apiToken123", userData.apiToken)
		assertEquals("Created by should match", "admin", userData.createdBy)
		assertEquals("Created date should match", "2024-03-10", userData.createdDate)
		assertEquals("IPK should match", "3.75", userData.ipk)
		assertEquals("Jenis kelamin should match", "Male", userData.jenisKelamin)
		assertEquals("Modified by should match", "admin", userData.modifiedBy)
		assertEquals("Modified date should match", "2024-03-10", userData.modifiedDate)
		assertEquals("No. Telp should match", "1234567890", userData.noTelp)
		assertEquals("Nomor Induk should match", "123456", userData.nomorInduk)
		assertEquals("Role ID should match", "1", userData.roleId)
		assertEquals("SKS should match", 100, userData.sks)
		assertEquals("Updated at should match", "2024-03-10", userData.updatedAt)
		assertEquals("User active should match", "1", userData.userActive)
		assertEquals("User email should match", "user@example.com", userData.userEmail)
		assertEquals("User ID should match", 1L, userData.userId)
		assertEquals("User image name should match", "user.jpg", userData.userImageName)
		assertEquals("User image path should match", "/images/", userData.userImagePath)
		assertEquals("User image URL should match", "http://example.com/images/user.jpg", userData.userImageUrl)
		assertEquals("User name should match", "John Doe", userData.userName)
	}
}