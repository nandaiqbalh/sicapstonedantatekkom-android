package com.kel022322.sicapstonedantatekkom.data.remote.model.auth.login.reqeust

import com.kel022322.sicapstonedantatekkom.data.remote.model.auth.login.request.AuthLoginRequestBody
import org.junit.Assert.assertEquals
import org.junit.Test

class AuthLoginRequestBodyTest {

	@Test
	fun `create AuthLoginRequestBody with valid data`() {
		// Arrange
		val nomorInduk = "123456"
		val password = "password"

		// Act
		val requestBody = AuthLoginRequestBody(nomorInduk, password)

		// Assert
		assertEquals("Nomor Induk should match", nomorInduk, requestBody.nomorInduk)
		assertEquals("Password should match", password, requestBody.password)
	}

	@Test
	fun `create AuthLoginRequestBody with null data`() {
		// Arrange
		val nomorInduk: String? = null
		val password: String? = null

		// Act
		val requestBody = AuthLoginRequestBody(nomorInduk, password)

		// Assert
		assertEquals("Nomor Induk should be null", nomorInduk, requestBody.nomorInduk)
		assertEquals("Password should be null", password, requestBody.password)
	}
}
