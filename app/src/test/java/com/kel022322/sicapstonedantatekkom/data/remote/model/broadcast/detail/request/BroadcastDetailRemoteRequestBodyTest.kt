package com.kel022322.sicapstonedantatekkom.data.remote.model.broadcast.detail.request

import org.junit.Assert.assertEquals
import org.junit.Test

class BroadcastDetailRemoteRequestBodyTest {

	@Test
	fun `create BroadcastDetailRemoteRequestBody with valid data`() {
		// Arrange
		val broadcastDetailRequestBody = BroadcastDetailRemoteRequestBody("123")

		// Assert
		assertEquals("Broadcast ID should match", "123", broadcastDetailRequestBody.broadcastId)
	}
}