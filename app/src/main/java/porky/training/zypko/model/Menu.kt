package porky.training.zypko.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "cart")
class Menu {
    @PrimaryKey
    var id = 0

    @ColumnInfo
    var restaurant_id = 0

    @ColumnInfo
    var name: String? = null

    @ColumnInfo
    var costOfOne = 0.0

    @ColumnInfo
    var isOrdered = false

}