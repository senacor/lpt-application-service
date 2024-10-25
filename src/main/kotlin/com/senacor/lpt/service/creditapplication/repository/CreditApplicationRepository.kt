package com.senacor.lpt.service.creditapplication.repository

import com.google.cloud.spring.data.firestore.FirestoreReactiveRepository
import org.springframework.stereotype.Repository

@Repository
interface CreditApplicationRepository : FirestoreReactiveRepository<CreditApplicationFirestoreModel>