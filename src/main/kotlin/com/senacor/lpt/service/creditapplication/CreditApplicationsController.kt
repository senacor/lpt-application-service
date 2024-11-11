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
import java.util.*

@RestController
@RequestMapping("/api/credit-applications")
class CreditApplicationsController(
    private val customerMasterDataClient: CustomerMasterDataClient,
    private val creditApplicationRepository: CreditApplicationRepository
) {

    var logger: Logger = getLogger(CreditApplicationsController::class.java)

    @PostMapping
    fun evaluateCreditApplication(@RequestBody request: CreditApplicationRequest) =
        Mono.zip(Mono.just(
            // TODO: put fancy credit application evaluation logic here...
            CreditDecision(CreditDecisionType.APPROVED)
        ), customerMasterDataClient.selectCustomerById("12983719"))
            .map { creditDecisionCustomerTuple ->
                // TODO: add a proper application/domain layer instead of just talking to a repo
                request.toDomain().copy(creditDecision = creditDecisionCustomerTuple.t1.decision)
            }.flatMap { domainModel ->
                creditApplicationRepository.save(toFirestoreModel(domainModel))
            }.map { savedCreditApplication ->
                CreditApplicationResponse(
                    savedCreditApplication.creditDecision!!, savedCreditApplication.id!!
                )
            }

    @PostMapping("/{id}")
    fun acceptCreditApplication(@PathVariable id: String) {
        // TODO: accept credit and create credit agreement
    }
}
