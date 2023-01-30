package com.voitov.todolist.data

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.voitov.todolist.domain.ShopItem
import com.voitov.todolist.domain.ShopListRepository

class ShopListRepositoryImpl(
    private val application: Application
) : ShopListRepository {
//    private val shopItems =
//        sortedSetOf<ShopItem>({ shopItem1, shopItem2 -> shopItem1.id.compareTo(shopItem2.id) })
//
//    init {
//        for (i in 1..10) {
//            val enabled = Random.nextBoolean()
//            val priority = when (Random.nextInt(3)) {
//                0 -> Priority.LOW
//                1 -> Priority.MEDIUM
//                else -> Priority.HIGH
//            }
//            addShopItem(ShopItem("block of flats $i", 1.0, priority, enabled))
//        }
//    }

    private val dao = AppDatabase.getInstance(application).getShopListDao()
    private val mapper = ShopListMapper()

    override fun addShopItem(shopItem: ShopItem) {
        dao.addShopItem(mapper.mapEntityToDbModel(shopItem))
    }

    override fun editShopItem(shopItem: ShopItem) {
        dao.editShopItemDbModel(mapper.mapEntityToDbModel(shopItem))
    }

    override fun getShopItem(shopItemId: Int): ShopItem {
        val dbModel = dao.getShopItemDbModel(shopItemId)
        dbModel?.let {
            return mapper.mapDbModelToEntity(it)
        }
        throw NullPointerException("There is no shopItem with id = $shopItemId")
    }

    override fun getShopList(): LiveData<List<ShopItem>> =
        Transformations.map(dao.getShopList()) {
            mapper.mapDbModelsListToEntityList(it)
        }


    override fun removeShopItem(shopItem: ShopItem) {
        dao.removeShopItemDbModel(mapper.mapEntityToDbModel(shopItem))
    }
}