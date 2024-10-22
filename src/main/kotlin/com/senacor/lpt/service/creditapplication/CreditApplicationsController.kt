package com.senacor.lpt.service.creditapplication

import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/credit-applications")
class CreditApplicationsController {

    @PostMapping
    fun evaluateCreditApplication(@RequestBody request: CreditApplicationRequest): CreditDecision {
        // TODO: put fancy credit application evaluation logic here...
        return CreditDecision(CreditDecisionType.APPROVED)
    }

    @PostMapping("/{id}/accept")
    fun acceptCreditApplication(@PathVariable id: String) {
        // TODO: accept credit and create credit agreement
    }
}
