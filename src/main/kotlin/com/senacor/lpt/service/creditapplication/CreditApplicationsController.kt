package com.senacor.lpt.service.creditapplication

import com.senacor.lpt.service.creditapplication.repository.CreditApplicationRepository
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/credit-applications")
class CreditApplicationsController(private val creditApplicationRepository: CreditApplicationRepository) {

    @PostMapping
    fun evaluateCreditApplication(@RequestBody request: CreditApplicationRequest): CreditDecision {
        // TODO: put fancy credit application evaluation logic here...
        // TODO: add a proper application/domain layer instead of just talking to a repo
        val creditApplication = request.toDomain()
        // Quick test to check whether we can write into the repo
        creditApplicationRepository.save(toFirestoreModel(creditApplication))

        return CreditDecision(CreditDecisionType.APPROVED)
    }

    @PostMapping("/{id}/accept")
    fun acceptCreditApplication(@PathVariable id: String) {
        // TODO: accept credit and create credit agreement
    }
}
