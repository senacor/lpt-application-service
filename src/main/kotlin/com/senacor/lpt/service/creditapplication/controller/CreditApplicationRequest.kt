package com.senacor.lpt.service.creditapplication.controller

import java.math.BigDecimal

class CreditApplicationRequest(
    val creditAmount: BigDecimal,
    val firstName: String,
    val lastName: String,
    val zipCode: String,
    val occupation: String,
    val monthlyNetIncome: BigDecimal,
    val monthlyExpenses: BigDecimal
)
