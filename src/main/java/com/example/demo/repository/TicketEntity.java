package com.example.demo.repository;

import lombok.Builder;
import lombok.Value;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Builder
@Value
@Document
public class TicketEntity {
  @Id
  Long id;
  String title;
  String description;
}
