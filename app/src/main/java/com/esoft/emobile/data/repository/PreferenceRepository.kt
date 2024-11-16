package com.esoft.emobile.data.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.esoft.emobile.domain.model.Preference
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject

class PreferenceRepository @Inject constructor(
    private val dataStore: DataStore<Preferences>
) {

    private companion object {
        val PLATE = stringPreferencesKey("plate")
        val ACRONYM = stringPreferencesKey("acronym")
        //val MAC_ADDRESS = stringPreferencesKey("macAddress")
    }

    private val preferenceFlow: Flow<Preference> = dataStore.data.map { preferences ->
        Preference(
            plate = preferences[PLATE] ?: "",
            acronym = preferences[ACRONYM] ?: "",
            //macAddress = preferences[MAC_ADDRESS] ?: "",
        )
    }

    suspend fun saveActivation(preference: Preference) = withContext(Dispatchers.IO) {
        dataStore.edit { preferences ->
            preferences[PLATE] = preference.plate
            preferences[ACRONYM] = preference.acronym
            //preferences[MAC_ADDRESS] = preference.macAddress
        }
    }

    fun isLogged(): Flow<Boolean> {
        return preferenceFlow.map { it.plate.isNotEmpty() && it.acronym.isNotEmpty() }
    }

    fun plate(): Flow<String> {
        return preferenceFlow.map { it.plate }
    }

    fun acronym(): Flow<String> {
        return preferenceFlow.map { it.acronym }
    }

//    fun macAddress(): Flow<String> {
//        return preferenceFlow.map { it.macAddress }
//    }

    suspend fun extractPlate(): String = withContext(Dispatchers.IO) {
        preferenceFlow.map { it.plate }.firstOrNull() ?: ""
    }

    suspend fun extractAcronym(): String = withContext(Dispatchers.IO) {
        preferenceFlow.map { it.acronym }.firstOrNull() ?: ""
    }

    suspend fun clearPlate(): Unit {
        dataStore.edit {
            it.remove(PLATE)
        }
    }

//    suspend fun extractMacAddress(): String = withContext(Dispatchers.IO) {
//        preferenceFlow.map { it.macAddress }.firstOrNull() ?: ""
//    }
}
