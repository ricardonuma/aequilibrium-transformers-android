package com.aequilibrium.transformers.data.model

import android.os.Parcelable
import com.squareup.moshi.Json
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Transformer(
    @Json(name = "id")
    val id: String,
    @Json(name = "name")
    val name: String,
    @Json(name = "team")
    val team: String,
    @Json(name = "strength")
    val strength: Int,
    @Json(name = "intelligence")
    val intelligence: Int,
    @Json(name = "speed")
    val speed: Int,
    @Json(name = "endurance")
    val endurance: Int,
    @Json(name = "rank")
    val rank: Int,
    @Json(name = "courage")
    val courage: Int,
    @Json(name = "firepower")
    val firepower: Int,
    @Json(name = "skill")
    val skill: Int,
    @Json(name = "team_icon")
    val team_icon: String
) : Parcelable {
    fun overallRating() = strength + intelligence + speed + endurance + rank + courage + firepower + skill
}