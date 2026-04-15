package top.song_mojing.nest.android.sql.model


import androidx.room.Entity
import androidx.room.PrimaryKey


/**
 * 位置信息实体类
 * @property id 自增主键
 * @property latitude 纬度
 * @property longitude 经度
 * @property accuracy 精度（米）
 * @property timestamp 时间戳
 * @property eventType 事件类型：0-普通定位，1-服务开启，2-服务关闭
 */
@Entity(tableName = "location_records")
data class LocationInformation(
	@PrimaryKey(autoGenerate = true) val id: Long = 0,
	val latitude: Double,
	val longitude: Double?,
	val accuracy: Float,
	val timestamp: Long = System.currentTimeMillis(),
	val eventType: Int = EVENT_TYPE_NORMAL
) {
	companion object {
		/**
		 * 定位
		 */
		const val EVENT_TYPE_NORMAL = 0
		/**
		 * 服务开启
		 */
		const val EVENT_TYPE_START = 1
		/**
		 * 服务关闭
		 */
		const val EVENT_TYPE_STOP = 2
	}
}