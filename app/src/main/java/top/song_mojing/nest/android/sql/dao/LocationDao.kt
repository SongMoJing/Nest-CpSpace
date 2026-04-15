package top.song_mojing.nest.android.sql.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import top.song_mojing.nest.android.sql.model.LocationInformation

@Dao
interface LocationDao {
	@Insert
	suspend fun insert(location: LocationInformation)

	@Query("SELECT * FROM location_records ORDER BY timestamp DESC")
	fun getAllLocations(): Flow<List<LocationInformation>>

	@Query("DELETE FROM location_records")
	suspend fun clearAll()
}