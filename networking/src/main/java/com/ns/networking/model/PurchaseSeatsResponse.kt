package com.ns.networking.model

data class PurchaseSeatsResponse(
    val `data`: PurchaseSeatData,
    val message: String,
    val resultcode: Int
)

data class PurchaseSeatData(
    val no_of_seats: Int,
    val purchase_id: Int,
    val rate: Int,
    val status: String,
    val tax_details: List<TaxDetail>,
    val total_amount: Int,
    val total_value: Int
)

data class TaxDetail(
    val tax_amnt: Int,
    val tax_id: Int,
    val tax_included: String,
    val tax_included_text: String,
    val tax_name: String,
    val tax_rate: String
)