package com.senacor.lpt.service.creditapplication

import com.senacor.lpt.service.creditapplication.customer.master.data.adapter.CustomerMasterDataClient
import com.senacor.lpt.service.creditapplication.repository.CreditApplicationRepository
import org.slf4j.Logger
import org.slf4j.LoggerFactory.getLogger
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Mono

@RestController
@RequestMapping("/api/credit-applications")
class CreditApplicationsController(
    private val customerMasterDataClient: CustomerMasterDataClient,
    private val creditApplicationRepository: CreditApplicationRepository
) {

    var logger: Logger = getLogger(CreditApplicationsController::class.java)

    @PostMapping
    fun evaluateCreditApplication(@RequestBody request: CreditApplicationRequest): Mono<CreditDecision> {
        return customerMasterDataClient.selectCustomerById("12983719")
            .map {
                // TODO: put fancy credit application evaluation logic here...
                CreditDecision(CreditDecisionType.APPROVED)
            }
            .flatMap { creditDecision ->
                // TODO: add a proper application/domain layer instead of just talking to a repo
                val creditApplication = request.toDomain()
                    .copy(creditDecision = creditDecision.decision)

                creditApplicationRepository.save(toFirestoreModel(creditApplication))
                    .map { creditDecision }
            }
    }

    @PostMapping("/{id}/accept")
    fun acceptCreditApplication(@PathVariable id: String) {
        // TODO: accept credit and create credit agreement
    }
}
