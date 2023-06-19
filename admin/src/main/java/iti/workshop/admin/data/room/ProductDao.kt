package iti.workshop.admin.data.room


import androidx.room.*
import iti.workshop.admin.data.dto.Product
import kotlinx.coroutines.flow.Flow

//@Dao
interface ProductDao {
    //@Query("SELECT * FROM product")
    fun getAllProducts(): Flow<MutableList<Product>>

    //@Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllTable(products: List<Product?>?)

}