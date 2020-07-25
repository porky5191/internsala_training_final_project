package porky.training.zypko.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favourite_restaurant")
class Restaurant {
    @PrimaryKey
    var id = 0

    @ColumnInfo
    var name: String? = null

    @ColumnInfo
    var imageUrl: String? = null

    @ColumnInfo
    var rating = 0.0

    @ColumnInfo
    var costForOne = 0.0

    @ColumnInfo
    var isFavourite = false

}