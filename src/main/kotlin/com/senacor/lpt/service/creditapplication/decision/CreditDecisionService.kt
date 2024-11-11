package com.senacor.lpt.service.creditapplication.decision

import com.senacor.lpt.service.creditapplication.CreditDecisionType
import com.senacor.lpt.service.creditapplication.CreditDecisionType.*
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono
import java.math.BigDecimal

@Service
class CreditDecisionService {

    val minimalNetSavings = BigDecimal(1000);

    fun decideOnCredit(monthlyNetIncome: BigDecimal,
                       monthlyExpenses: BigDecimal
    ): Mono<CreditDecisionType> = when(monthlyNetIncome.minus(monthlyExpenses) > minimalNetSavings) {
        true -> Mono.just(APPROVED)
        false -> Mono.just(DENIED)
    }
}