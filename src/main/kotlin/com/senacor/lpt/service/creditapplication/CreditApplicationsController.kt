package com.senacor.lpt.service.creditapplication

import com.senacor.lpt.service.creditapplication.customer.master.data.adapter.CustomerMasterDataClient
import com.senacor.lpt.service.creditapplication.repository.CreditApplicationRepository
import org.slf4j.Logger
import org.slf4j.LoggerFactory.getLogger
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Mono

@RestController
@CrossOrigin(origins = ["*"])
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
                logger.info("Selected credit decision and customer")
                println("Selected credit decision and customer")
                request.toDomain().copy(creditDecision = creditDecisionCustomerTuple.t1.decision)
            }.flatMap { domainModel ->
                logger.info("Saving credit decision to database")
                println("Saving credit decision to database")
                creditApplicationRepository.save(toFirestoreModel(domainModel))
            }.map { savedCreditApplication ->
                logger.info("Received new credit decision request with id {}", savedCreditApplication.id)
                println(String.format("Received new credit decision request with id %s", savedCreditApplication.id))
                CreditApplicationResponse(
                    savedCreditApplication.creditDecision!!, savedCreditApplication.id!!
                )
            }

    @PostMapping("/{id}/acceptance")
    @ResponseStatus(HttpStatus.ACCEPTED)
    fun acceptCreditApplication(@PathVariable id: String) =
        creditApplicationRepository.findById(id)
            .map { fromFirestoreModel(it) }
            .map {
                logger.info("Accepting credit application with id {}", it.id)
                it.copy(accepted = true)
            }.map { toFirestoreModel(it) }
            .flatMap {
                logger.info("Saving credit decision with id {} to database", it.id)
                creditApplicationRepository.save(it)
            }
}
