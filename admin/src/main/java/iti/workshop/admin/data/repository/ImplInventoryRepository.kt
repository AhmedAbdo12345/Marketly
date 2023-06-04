package iti.workshop.admin.data.repository

import iti.workshop.admin.data.remote.retrofit.InventoryAPICalls

class ImplInventoryRepository(private val _api:InventoryAPICalls):IInventoryRepository {
}