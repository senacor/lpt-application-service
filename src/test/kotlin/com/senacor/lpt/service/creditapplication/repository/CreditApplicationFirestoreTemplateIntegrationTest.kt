package com.senacor.lpt.service.creditapplication.repository

import com.google.cloud.spring.data.firestore.FirestoreTemplate
import com.senacor.lpt.service.creditapplication.CreditApplication
import com.senacor.lpt.service.creditapplication.fromFirestoreModel
import com.senacor.lpt.service.creditapplication.toFirestoreModel
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import reactor.core.publisher.Mono
import java.math.BigDecimal
import java.util.UUID

@SpringBootTest
@ActiveProfiles("test")
class CreditApplicationFirestoreTemplateIntegrationTest {

    @Autowired
    lateinit var firestoreTemplate: FirestoreTemplate

    @Test
    fun `test round-trip`() {
        val id = UUID.randomUUID().toString()
        val creditApplication = CreditApplication(
            id,
            BigDecimal("10000.00"),
            "Max",
            "Mustermann",
            "12345",
            "Arbeiter",
            BigDecimal("3000.00"),
            BigDecimal("2500.00")
        )

        firestoreTemplate.save(toFirestoreModel(creditApplication)).block()

        val loadedCreditApplication = firestoreTemplate.findById(Mono.just(id), CreditApplicationFirestoreModel::class.java)
            .map { fromFirestoreModel(it) }
            .block()!!

        assertThat(loadedCreditApplication.id).isEqualTo(id)
        assertThat(loadedCreditApplication.creditAmount).isEqualTo(BigDecimal("10000.00"))
    }
}
