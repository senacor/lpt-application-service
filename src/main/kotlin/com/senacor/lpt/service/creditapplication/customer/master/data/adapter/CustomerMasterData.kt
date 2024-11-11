package com.senacor.lpt.service.creditapplication.customer.master.data.adapter

import com.fasterxml.jackson.annotation.JsonFormat
import java.time.LocalDate

data class CustomerMasterData(
    val firstname: String,
    val lastname: String,
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd.MM.yyyy")
    val birthday: LocalDate
)
