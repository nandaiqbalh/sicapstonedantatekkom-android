package com.kel022322.sicapstonedantatekkom.helpers

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestDispatcher
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.runner.Description

@OptIn(ExperimentalCoroutinesApi::class)
class MainDispatcherRule(
	private val dispatcher: TestDispatcher = UnconfinedTestDispatcher()
) : InstantTaskExecutorRule() {
	override fun starting(description: Description) {
		super.starting(description)

		Dispatchers.setMain(dispatcher)
	}

	override fun finished(description: Description) {
		super.finished(description)

		Dispatchers.resetMain()
	}
}