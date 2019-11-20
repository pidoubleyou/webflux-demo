package com.example.demo.api.model;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import lombok.Builder;
import lombok.Setter;
import lombok.Value;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@JsonDeserialize(builder = Ticket.TicketBuilder.class)
@Builder
@Value
@Document
public class Ticket {
  @Id
  Long id;
  String title;
  String description;

  @JsonPOJOBuilder(withPrefix = "")
  public static class TicketBuilder {}
}
