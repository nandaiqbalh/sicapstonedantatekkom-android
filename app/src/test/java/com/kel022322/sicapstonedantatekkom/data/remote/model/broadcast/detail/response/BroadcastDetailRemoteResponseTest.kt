package com.kel022322.sicapstonedantatekkom.data.remote.model.broadcast.detail.response

import org.junit.Assert.assertEquals
import org.junit.Test

class BroadcastDetailRemoteResponseTest {

	@Test
	fun `create BroadcastDetailRemoteResponse with valid data`() {
		// Arrange
		val broadcastDetailResponse = BroadcastDetailRemoteResponse(
			DataDetailBroadcast(
				Broadcast(
					"image.jpg",
					"/images/",
					"http://example.com/images/image.jpg",
					"admin",
					"2024-03-10",
					"123",
					null,
					"Event Description",
					"http://example.com/link",
					null,
					null,
					"Event Name",
					"2024-03-10",
					"2024-03-15"
				)
			),
			"Success",
			true
		)

		// Assert
		assertEquals("Data should match", "Success", broadcastDetailResponse.message)
		assertEquals("Status should match", true, broadcastDetailResponse.status)

		val broadcastData = broadcastDetailResponse.data?.broadcast
		assertEquals("Image Name should match", "image.jpg", broadcastData?.broadcastImageName)
		assertEquals("Image Path should match", "/images/", broadcastData?.broadcastImagePath)
		assertEquals("Image URL should match", "http://example.com/images/image.jpg", broadcastData?.broadcastImageUrl)
		// Add more assertions based on your actual implementation
	}
}
