package com.example.demo.api.model;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import lombok.Builder;
import lombok.Value;

@JsonDeserialize(builder = Ticket.TicketBuilder.class)
@Builder
@Value
public class Ticket {
  Long id;
  String title;
  String description;

  @JsonPOJOBuilder(withPrefix = "")
  public static class TicketBuilder {
  }
}
