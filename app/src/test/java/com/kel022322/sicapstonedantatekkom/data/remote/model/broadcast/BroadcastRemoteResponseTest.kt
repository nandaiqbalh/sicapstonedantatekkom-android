package com.kel022322.sicapstonedantatekkom.data.remote.model.broadcast

import com.kel022322.sicapstonedantatekkom.data.remote.model.broadcast.allbroadcast.RsBroadcast
import org.junit.Assert.assertEquals
import org.junit.Test

class BroadcastRemoteResponseTest {

	@Test
	fun `create BroadcastRemoteResponse with valid data`() {
		// Arrange
		val broadcastResponse = BroadcastRemoteResponse(
			DataBroadcast(
				RsBroadcast(
					1,
					listOf(
						DataX(
							"image.jpg",
							"/images/",
							"http://example.com/images/image.jpg",
							"admin",
							"2024-03-10",
							123,
							"1",
							"Event Description",
							"http://example.com/link",
							null,
							null,
							"Event Name",
							"2024-03-10",
							"2024-03-15"
						)
					),
					"http://example.com/first",
					1,
					1,
					"http://example.com/last",
					listOf(Link(true, "label", "http://example.com")),
					null,
					"/path/",
					10,
					null,
					null,
					1,
				)
			),
			"Success",
			true
		)

		// Assert
		assertEquals("Data should match", "Success", broadcastResponse.message)
		assertEquals("Status should match", true, broadcastResponse.status)

		val rsBroadcast = broadcastResponse.data?.rs_broadcast
		assertEquals("Current Page should match", 1, rsBroadcast?.currentPage)
		assertEquals("First Page URL should match", "http://example.com/first", rsBroadcast?.firstPageUrl)
		// Add more assertions based on your actual implementation
	}
}
