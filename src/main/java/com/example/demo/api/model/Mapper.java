package com.example.demo.api.model;

import com.example.demo.repository.TicketEntity;

public class Mapper {
  private Mapper() {}

  public static Ticket map(TicketEntity entity) {
    return Ticket.builder().id(entity.getId()).title(entity.getTitle()).description(entity.getDescription()).build();
  }

  public static TicketEntity map(Ticket ticket) {
    return TicketEntity.builder().id(ticket.getId()).title(ticket.getTitle()).description(ticket.getDescription()).build();
  }
}
