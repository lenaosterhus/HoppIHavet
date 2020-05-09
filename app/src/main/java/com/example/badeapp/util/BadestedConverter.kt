package com.example.badeapp.util

import androidx.room.TypeConverter
import com.example.badeapp.models.Badested
import com.example.badeapp.models.alleBadesteder

class BadestedConverter {

    @TypeConverter
    fun toBadested(value: String?): Badested? {
        val values = value?.split(' ') ?: return null
        if (values.size != 2) return null
        return alleBadesteder.find { it -> it.lat == values[0] && it.lon == values[1] }
    }

    @TypeConverter
    fun toInternal(value: Badested?): String? {
        if (value != null) {
            return "${value.lat} ${value.lon}"
        } else {
            return null
        }
    }

}
