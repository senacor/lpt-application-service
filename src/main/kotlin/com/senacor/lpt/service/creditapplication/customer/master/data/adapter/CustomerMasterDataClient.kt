package com.senacor.lpt.service.creditapplication.customer.master.data.adapter

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.service.annotation.GetExchange
import org.springframework.web.service.annotation.HttpExchange
import reactor.core.publisher.Mono

@HttpExchange("/customer")
interface CustomerMasterDataClient {
    @GetExchange("/{customerId}")
    fun selectCustomerById(@PathVariable("customerId") customerId: String): Mono<ResponseEntity<CustomerMasterData>>
}