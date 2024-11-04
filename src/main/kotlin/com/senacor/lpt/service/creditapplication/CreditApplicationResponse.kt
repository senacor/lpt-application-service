package com.senacor.lpt.service.creditapplication

import java.math.BigDecimal
import java.util.UUID

class CreditApplicationResponse(
    val creditDecision: CreditDecision,
    val id: UUID
)
