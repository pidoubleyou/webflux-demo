package com.example.demo.repository;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface TicketRepository extends ReactiveMongoRepository<TicketEntity, Long> {
}
