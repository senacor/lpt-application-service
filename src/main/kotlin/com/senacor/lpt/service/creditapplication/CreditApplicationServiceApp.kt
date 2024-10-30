package com.senacor.lpt.service.creditapplication

import com.google.cloud.spring.data.firestore.repository.config.EnableReactiveFirestoreRepositories
import com.senacor.lpt.service.creditapplication.repository.CreditApplicationRepository
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class CreditApplicationServiceApp

fun main(args: Array<String>) {
	runApplication<CreditApplicationServiceApp>(*args)
}

