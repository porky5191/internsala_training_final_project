package porky.training.zypko.global

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import porky.training.zypko.interfaces.MenuDao
import porky.training.zypko.interfaces.RestaurantDao
import porky.training.zypko.model.Menu
import porky.training.zypko.model.Restaurant

@Database(entities = [Restaurant::class, Menu::class], version = 2, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    //Create Dao
    abstract fun restaurantDao(): RestaurantDao?
    abstract fun menuDao(): MenuDao?

    companion object {
        //    Create database instance
        private var database: AppDatabase? = null

        //    Define database name
        private const val DATABASE_NAME = "database"

        @Synchronized
        fun getInstance(context: Context): AppDatabase? {
            //Check condition
            if (database == null) {
                //when database is null
                //Initialize database
                database = Room.databaseBuilder(context.applicationContext,
                        AppDatabase::class.java, DATABASE_NAME)
                        .fallbackToDestructiveMigration()
                        .build()
            }

            //Return database
            return database
        }
    }
}