package com.senacor.lpt.service.creditapplication.repository

import com.senacor.lpt.service.creditapplication.CreditApplication
import com.senacor.lpt.service.creditapplication.CreditDecisionType
import com.senacor.lpt.service.creditapplication.fromFirestoreModel
import com.senacor.lpt.service.creditapplication.toFirestoreModel
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import java.math.BigDecimal
import java.util.UUID

@SpringBootTest
@ActiveProfiles("test")
class CreditApplicationRepositoryIntegrationTest {

    @Autowired
    lateinit var repository: CreditApplicationRepository

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
            BigDecimal("2500.00"),
            CreditDecisionType.PENDING,
            accepted = false
        )

        repository.save(toFirestoreModel(creditApplication)).block()

        val loadedCreditApplication = repository.findById(id)
            .map { fromFirestoreModel(it) }
            .block()!!

        assertThat(loadedCreditApplication.id).isEqualTo(id)
        assertThat(loadedCreditApplication.creditAmount).isEqualTo(BigDecimal("10000.00"))
    }
}
