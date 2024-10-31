package com.senacor.lpt.service.creditapplication

import com.senacor.lpt.service.customer.master.data.adapter.CustomerMasterDataClient
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.slf4j.LoggerFactory.getLogger
import org.springframework.web.bind.annotation.*
import reactor.core.publisher.Mono

@RestController
@RequestMapping("/api/credit-applications")
class CreditApplicationsController(private val customerMasterDataClient: CustomerMasterDataClient) {

    var logger: Logger = getLogger(CreditApplicationsController::class.java)


    @PostMapping
    fun evaluateCreditApplication(@RequestBody request: CreditApplicationRequest): Mono<CreditDecision> {
        return customerMasterDataClient.selectCustomerById("12983719")
            .map {
                // TODO: put fancy credit application evaluation logic here...
                CreditDecision(CreditDecisionType.APPROVED)
            }
    }

    @PostMapping("/{id}/accept")
    fun acceptCreditApplication(@PathVariable id: String) {
        // TODO: accept credit and create credit agreement
    }
}
