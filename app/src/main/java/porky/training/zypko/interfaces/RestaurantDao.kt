package porky.training.zypko.interfaces

import androidx.room.*
import porky.training.zypko.model.Restaurant

@Dao
interface RestaurantDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(restaurant: Restaurant?)

    @Delete
    fun delete(restaurant: Restaurant?)

    @get:Query("SELECT * FROM favourite_restaurant")
    val all: List<Restaurant?>?

    @Query("DELETE FROM favourite_restaurant")
    fun deleteAll()

}