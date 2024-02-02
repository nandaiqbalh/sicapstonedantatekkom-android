package com.kel022322.sicapstonedantatekkom.data.local.datastore.auth

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class AuthDataStoreManager(@ApplicationContext private val context: Context) {

	suspend fun clear() {
		context.dataStore.edit {
			it.clear()
		}
	}

	val getUsername: Flow<String?> = context.dataStore.data.map {
		it[USERNAME_KEY]
	}

	suspend fun setUsername(username: String) {
		context.dataStore.edit {
			it[USERNAME_KEY] = username
		}
	}

	val getPhotoProfile: Flow<String?> = context.dataStore.data.map {
		it[PHOTO_PROFILE_KEY]
	}

	suspend fun setPhotoProfile(photoProfile: String) {
		context.dataStore.edit {
			it[PHOTO_PROFILE_KEY] = photoProfile
		}
	}

	val getApiToken: Flow<String?> = context.dataStore.data.map {
		it[API_TOKEN_KEY]
	}

	suspend fun setApiToken(apiToken: String) {
		context.dataStore.edit {
			it[API_TOKEN_KEY] = apiToken
		}
	}

	val getUserId: Flow<String?> = context.dataStore.data.map {
		it[USER_ID_KEY]
	}

	suspend fun setUserId(userId: String) {
		context.dataStore.edit {
			it[USER_ID_KEY] = userId
		}
	}

	val getStatusAuth: Flow<Boolean?> = context.dataStore.data.map {
		it[STATUS_AUTH_KEY]
	}

	suspend fun setStatusAuth(statusAuth: Boolean) {
		context.dataStore.edit {
			it[STATUS_AUTH_KEY] = statusAuth
		}
	}

	companion object {
		private const val DATASTORE_NAME = "authdatastore_preferences"

		private val PHOTO_PROFILE_KEY = stringPreferencesKey("PHOTO_PROFILE_KEY")
		private val USERNAME_KEY = stringPreferencesKey("USERNAME_KEY")
		private val API_TOKEN_KEY = stringPreferencesKey("API_TOKEN_KEY")
		private val USER_ID_KEY = stringPreferencesKey("USER_ID_KEY")
		private val STATUS_AUTH_KEY = booleanPreferencesKey("STATUS_AUTH_KEY")

		private val Context.dataStore by preferencesDataStore(
			name = DATASTORE_NAME
		)
	}
}