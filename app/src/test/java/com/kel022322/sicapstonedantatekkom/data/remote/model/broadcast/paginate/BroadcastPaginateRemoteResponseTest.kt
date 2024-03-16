package com.kel022322.sicapstonedantatekkom.data.remote.model.broadcast.paginate

import org.junit.Assert.assertEquals
import org.junit.Test

class BroadcastPaginateRemoteResponseTest {

	@Test
	fun `create BroadcastPaginateRemoteResponse with valid data`() {
		// Arrange
		val broadcastPaginateResponse = BroadcastPaginateRemoteResponse(
			DataBroadcastPaginate(
				RsBroadcastPaginate(
					1,
					listOf(
						DataXBroadcastPaginate(
							"image.jpg",
							"/images/",
							"http://example.com/images/image.jpg",
							"admin",
							"2024-03-10",
							123,
							"1",
							"Event Description",
							"http://example.com/link",
							"admin",
							"2024-03-10",
							"Event Name",
							"2024-03-10",
							"2024-03-15"
						)
					),
					"http://example.com/first",
					1,
					1,
					"http://example.com/last",
					listOf(LinkBroadcastPaginate(true, "label", "http://example.com")),
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
		assertEquals("Data should match", "Success", broadcastPaginateResponse.message)
		assertEquals("Status should match", true, broadcastPaginateResponse.status)

		val rsBroadcastPaginate = broadcastPaginateResponse.data?.rs_broadcast
		assertEquals("Current Page should match", 1, rsBroadcastPaginate?.currentPage)
		assertEquals("First Page URL should match", "http://example.com/first", rsBroadcastPaginate?.firstPageUrl)
		// Add more assertions based on your actual implementation
	}
}
