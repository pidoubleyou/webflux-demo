package com.example.demo.repository;

import com.example.demo.api.model.Ticket;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface TicketRepository extends ReactiveMongoRepository<Ticket, Long> {
}
