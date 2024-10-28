package com.senacor.lpt.service.creditapplication.repository

import com.google.api.client.util.escape.PercentEscaper
import com.google.api.gax.rpc.internal.Headers
import com.google.auth.oauth2.GoogleCredentials
import com.google.cloud.spring.core.DefaultGcpProjectIdProvider
import com.google.cloud.spring.data.firestore.FirestoreTemplate
import com.google.cloud.spring.data.firestore.mapping.FirestoreClassMapper
import com.google.cloud.spring.data.firestore.mapping.FirestoreDefaultClassMapper
import com.google.cloud.spring.data.firestore.mapping.FirestoreMappingContext
import com.google.cloud.spring.data.firestore.repository.config.EnableReactiveFirestoreRepositories
import com.google.firestore.v1.FirestoreGrpc
import io.grpc.CallCredentials
import io.grpc.ClientInterceptor
import io.grpc.ManagedChannel
import io.grpc.ManagedChannelBuilder
import io.grpc.Metadata
import io.grpc.auth.MoreCallCredentials
import io.grpc.stub.MetadataUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.PropertySource
import java.io.IOException

@Configuration
@PropertySource("classpath:application-test.properties")
@EnableReactiveFirestoreRepositories(basePackageClasses = [CreditApplicationRepository::class])
class FirestoreIntegrationTestsConfiguration @Autowired constructor(
    @param:Value("\${test.integration.firestore.database-id}") var databaseId: String
) {
    lateinit var defaultParent: String

    val projectId: String = DefaultGcpProjectIdProvider().projectId

    init {
        this.defaultParent = String.format(
            "projects/%s/databases/%s/documents", this.projectId,
            databaseId
        )
    }

    @Bean
    fun firestoreTemplate(
        firestoreStub: FirestoreGrpc.FirestoreStub,
        classMapper: FirestoreClassMapper,
        firestoreMappingContext: FirestoreMappingContext
    ) = FirestoreTemplate(
        firestoreStub, this.defaultParent, classMapper, firestoreMappingContext
    )

    @Bean
    @Throws(IOException::class)
    fun firestoreStub(firestoreRoutingHeadersInterceptor: ClientInterceptor): FirestoreGrpc.FirestoreStub {
        val credentials: GoogleCredentials = GoogleCredentials.getApplicationDefault()
        val callCredentials: CallCredentials = MoreCallCredentials.from(credentials)

        // Create a channel
        val channel: ManagedChannel =
            ManagedChannelBuilder.forTarget("dns:///firestore.googleapis.com:443")
                .intercept(firestoreRoutingHeadersInterceptor)
                .build()
        return FirestoreGrpc.newStub(channel).withCallCredentials(callCredentials)
    }

    @Bean
    @ConditionalOnMissingBean(name = ["firestoreRoutingHeadersInterceptor"])
    fun firestoreRoutingHeadersInterceptor(): ClientInterceptor {
        // add routing header for custom database id
        val routingHeader = Metadata()
        if (projectId != null && databaseId != null) {
            val key =
                Metadata.Key.of(Headers.DYNAMIC_ROUTING_HEADER_KEY, Metadata.ASCII_STRING_MARSHALLER)
            routingHeader.put(
                key,
                ("project_id="
                        + PERCENT_ESCAPER.escape(projectId)
                        + "&database_id="
                        + PERCENT_ESCAPER.escape(databaseId))
            )
        }
        return MetadataUtils.newAttachHeadersInterceptor(routingHeader)
    }

    @Bean
    @ConditionalOnMissingBean
    fun getClassMapper(mappingContext: FirestoreMappingContext) = FirestoreDefaultClassMapper(mappingContext)

    @Bean
    fun firestoreMappingContext() = FirestoreMappingContext()

    companion object {
        private val PERCENT_ESCAPER = PercentEscaper("._-~")
    }
}
