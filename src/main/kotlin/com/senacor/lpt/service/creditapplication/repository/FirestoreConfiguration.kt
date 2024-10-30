package com.senacor.lpt.service.creditapplication.repository

import com.google.api.client.util.escape.PercentEscaper
import com.google.api.gax.rpc.internal.Headers
import com.google.auth.oauth2.GoogleCredentials
import com.google.cloud.spring.core.DefaultGcpProjectIdProvider
import com.google.cloud.spring.data.firestore.FirestoreTemplate
import com.google.cloud.spring.data.firestore.mapping.FirestoreClassMapper
import com.google.cloud.spring.data.firestore.mapping.FirestoreMappingContext
import com.google.cloud.spring.data.firestore.repository.config.EnableReactiveFirestoreRepositories
import com.google.firestore.v1.FirestoreGrpc
import io.grpc.ClientInterceptor
import io.grpc.ManagedChannelBuilder
import io.grpc.Metadata
import io.grpc.auth.MoreCallCredentials
import io.grpc.stub.MetadataUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
@EnableReactiveFirestoreRepositories(basePackageClasses = [CreditApplicationRepository::class])
class FirestoreConfiguration @Autowired constructor(
    @param:Value("\${firestore.database-id}") var databaseId: String
) {
    val projectId: String = DefaultGcpProjectIdProvider().projectId

    val parentResource = "projects/$projectId/databases/$databaseId/documents"

    @Bean
    fun firestoreTemplate(
        firestoreStub: FirestoreGrpc.FirestoreStub,
        classMapper: FirestoreClassMapper,
        firestoreMappingContext: FirestoreMappingContext
    ) = FirestoreTemplate(
        firestoreStub, parentResource, classMapper, firestoreMappingContext
    )

    @Bean
    fun firestoreStub(firestoreRoutingHeadersInterceptor: ClientInterceptor): FirestoreGrpc.FirestoreStub {
        val credentials = GoogleCredentials.getApplicationDefault()
        val callCredentials = MoreCallCredentials.from(credentials)

        val managedChannel = ManagedChannelBuilder.forTarget("dns:///firestore.googleapis.com:443")
            .intercept(firestoreRoutingHeadersInterceptor)
            .build()
        return FirestoreGrpc.newStub(managedChannel).withCallCredentials(callCredentials)
    }

    @Bean
    @ConditionalOnMissingBean(name = ["firestoreRoutingHeadersInterceptor"])
    fun firestoreRoutingHeadersInterceptor(): ClientInterceptor {
        val routingHeaderKey = Metadata.Key.of(Headers.DYNAMIC_ROUTING_HEADER_KEY, Metadata.ASCII_STRING_MARSHALLER)
        val routingHeaderValue = String.format(
            "project_id=%s&database_id=%s",
            PERCENT_ESCAPER.escape(projectId),
            PERCENT_ESCAPER.escape(databaseId)
        )

        val routingHeader = Metadata().apply {
            put(routingHeaderKey, routingHeaderValue)
        }
        return MetadataUtils.newAttachHeadersInterceptor(routingHeader)
    }

    companion object {
        private val PERCENT_ESCAPER = PercentEscaper("_-")
    }

}