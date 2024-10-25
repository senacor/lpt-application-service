package com.senacor.lpt.service.creditapplication.repository

import com.google.cloud.firestore.annotation.DocumentId
import com.google.cloud.spring.data.firestore.Document

@Document(collectionName = "credit_applications")
class CreditApplicationFirestoreModel(
    @DocumentId
    var id: String? = null,
    var creditAmountInCents: Long? = null,
    var firstName: String? = null,
    var lastName: String? = null,
    var zipCode: String? = null,
    var occupation: String? = null,
    var monthlyNetIncomeInCents: Long? = null,
    var monthlyExpensesInCents: Long? = null
)
