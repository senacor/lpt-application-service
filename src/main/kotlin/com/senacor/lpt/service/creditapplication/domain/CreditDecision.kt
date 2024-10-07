package com.senacor.lpt.service.creditapplication.domain

class CreditDecision(
    val decision: CreditDecisionType
)

enum class CreditDecisionType {
    APPROVED, DENIED, CONDITIONAL_APPROVED, REVISED_TERMS
}
