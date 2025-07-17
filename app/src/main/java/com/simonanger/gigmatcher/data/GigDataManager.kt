package com.simonanger.gigmatcher.data

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.simonanger.gigmatcher.model.Gig
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class GigDataManager(context: Context) {
    private val sharedPreferences: SharedPreferences = 
        context.getSharedPreferences("gig_matcher_data", Context.MODE_PRIVATE)
    
    private val gson = Gson()
    
    companion object {
        private const val GIGS_KEY = "saved_gigs"
    }
    
    suspend fun saveGigs(gigs: List<Gig>) {
        withContext(Dispatchers.IO) {
            try {
                val gigsJson = gson.toJson(gigs)
                sharedPreferences.edit()
                    .putString(GIGS_KEY, gigsJson)
                    .apply()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
    
    suspend fun loadGigs(): List<Gig> {
        return withContext(Dispatchers.IO) {
            try {
                val gigsJson = sharedPreferences.getString(GIGS_KEY, null)
                if (gigsJson != null) {
                    val type = object : TypeToken<List<Gig>>() {}.type
                    gson.fromJson(gigsJson, type)
                } else {
                    emptyList()
                }
            } catch (e: Exception) {
                e.printStackTrace()
                emptyList()
            }
        }
    }
    
    fun clearAllData() {
        sharedPreferences.edit()
            .remove(GIGS_KEY)
            .apply()
    }
}