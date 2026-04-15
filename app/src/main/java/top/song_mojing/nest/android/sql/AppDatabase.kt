package top.song_mojing.nest.android.sql

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import top.song_mojing.nest.android.sql.dao.LocationDao
import top.song_mojing.nest.android.sql.model.LocationInformation

@Database(entities = [LocationInformation::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

	abstract fun locationDao(): LocationDao

	companion object {
		@Volatile
		private var INSTANCE: AppDatabase? = null

		fun getDatabase(context: Context): AppDatabase {
			return INSTANCE ?: synchronized(this) {
				val instance = Room.databaseBuilder(
					context.applicationContext,
					AppDatabase::class.java,
					"nest_database"
				).build()
				INSTANCE = instance
				instance
			}
		}
	}
}