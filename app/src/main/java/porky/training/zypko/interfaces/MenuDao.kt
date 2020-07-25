package porky.training.zypko.interfaces

import androidx.room.*
import porky.training.zypko.model.Menu

@Dao
interface MenuDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(menu: Menu?)

    @Delete
    fun delete(menu: Menu?)

    @get:Query("SELECT * FROM cart")
    val all: List<Menu?>?

    @Query("DELETE FROM cart")
    fun deleteAll()

    @get:Query("SELECT DISTINCT restaurant_id FROM cart")
    val restaurantId: Int
}