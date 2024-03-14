package com.kel022322.sicapstonedantatekkom.data.remote.model.auth.logout

import com.kel022322.sicapstonedantatekkom.data.remote.model.auth.logout.response.AuthLogoutRemoteResponse
import org.junit.Assert.assertEquals
import org.junit.Test

class AuthLogoutRemoteResponseTest {

	@Test
	fun `create AuthLogoutRemoteResponse with valid data`() {
		// Arrange
		val authLogoutResponse = AuthLogoutRemoteResponse(
			"OK",
			true,
			"Logout successful"
		)

		// Assert
		assertEquals("Status should match", "OK", authLogoutResponse.status)
		assertEquals("Success should match", true, authLogoutResponse.success)
		assertEquals("Message should match", "Logout successful", authLogoutResponse.message)
	}
}
