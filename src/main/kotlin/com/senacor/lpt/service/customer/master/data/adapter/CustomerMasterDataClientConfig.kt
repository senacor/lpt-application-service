package com.senacor.lpt.service.customer.master.data.adapter

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.support.WebClientAdapter
import org.springframework.web.service.invoker.HttpServiceProxyFactory

@Configuration
class CustomerMasterDataClientConfig {
    @Bean
    fun customerMasterDataClient(builder: WebClient.Builder,
                                 @Value("client.customer-master-data.url") baseUrl: String): CustomerMasterDataClient {
        val webClient = builder.baseUrl(baseUrl).build()
        return HttpServiceProxyFactory.builderFor(WebClientAdapter.create(webClient)).build()
            .createClient(CustomerMasterDataClient::class.java)
    }
}