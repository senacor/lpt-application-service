package com.senacor.lpt.service.creditapplication

import com.google.cloud.firestore.annotation.DocumentId
import com.google.cloud.spring.data.firestore.Document
import com.senacor.lpt.service.creditapplication.repository.CreditApplicationFirestoreModel
import java.math.BigDecimal
import java.math.RoundingMode

@Document(collectionName = "credit_applications")
data class CreditApplication(
    @DocumentId
    val id: String,
    val creditAmount: BigDecimal,
    val firstName: String,
    val lastName: String,
    val zipCode: String,
    val occupation: String,
    val monthlyNetIncome: BigDecimal,
    val monthlyExpenses: BigDecimal,
    val creditDecision: CreditDecisionType? = null,
)

fun toFirestoreModel(value: CreditApplication) =
    CreditApplicationFirestoreModel(
        id = value.id,
        creditAmountInCents = toCents(value.creditAmount),
        firstName = value.firstName,
        lastName = value.lastName,
        zipCode = value.zipCode,
        occupation = value.occupation,
        monthlyNetIncomeInCents = toCents(value.monthlyNetIncome),
        monthlyExpensesInCents = toCents(value.monthlyExpenses),
        creditDecision = value.creditDecision
    )

fun fromFirestoreModel(value: CreditApplicationFirestoreModel) =
    CreditApplication(
        id = value.id!!,
        creditAmount = fromCents(value.creditAmountInCents!!),
        firstName = value.firstName!!,
        lastName = value.lastName!!,
        zipCode = value.zipCode!!,
        occupation = value.occupation!!,
        monthlyNetIncome = fromCents(value.monthlyNetIncomeInCents!!),
        monthlyExpenses = fromCents(value.monthlyExpensesInCents!!),
        creditDecision = value.creditDecision
    )

private fun toCents(value: BigDecimal) = value.times(BigDecimal(100)).toLong()
private fun fromCents(value: Long) = value.toBigDecimal().divide(BigDecimal(100), 2, RoundingMode.HALF_UP)
