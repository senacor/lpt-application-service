package com.senacor.lpt.service.creditapplication.domain

import java.math.BigDecimal

/*
 * Kreditantrag
 */
class CreditApplication(
    val id: String,
    val creditAmount: BigDecimal,
    val firstName: String,
    val lastName: String,
    val zipCode: String,
    val occupation: String,
    val monthlyNetIncome: BigDecimal,
    val monthlyExpenses: BigDecimal
)
