package com.senacor.lpt.service.creditapplication

import java.math.BigDecimal
import java.util.UUID

class CreditApplicationRequest(
    val creditAmount: BigDecimal,
    val firstName: String,
    val lastName: String,
    val zipCode: String,
    val occupation: String,
    val monthlyNetIncome: BigDecimal,
    val monthlyExpenses: BigDecimal
) {
    fun toDomain() = CreditApplication(
        id = UUID.randomUUID().toString(),
        creditAmount = creditAmount,
        firstName = firstName,
        lastName = lastName,
        zipCode = zipCode,
        occupation = occupation,
        monthlyNetIncome = monthlyNetIncome,
        monthlyExpenses = monthlyExpenses,
        creditDecision = CreditDecisionType.PENDING
    )
}
