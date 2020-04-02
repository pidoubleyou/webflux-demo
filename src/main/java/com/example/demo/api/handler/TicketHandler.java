package com.example.demo.api.handler;

import com.example.demo.api.model.Mapper;
import com.example.demo.api.model.Ticket;
import com.example.demo.repository.TicketRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.web.reactive.function.BodyInserters.fromValue;

@Slf4j
public class TicketHandler {
  private TicketRepository ticketRepository;

  public TicketHandler(TicketRepository ticketRepository) {
    this.ticketRepository = ticketRepository;
  }

  public Mono<ServerResponse> createTicket(ServerRequest request) {
    return request.bodyToMono(Ticket.class)
      .flatMap(this::setId)
      .map(Mapper::map)
      .doOnNext(ticketRepository::save)
      .map(Mapper::map)
      .flatMap(
        ticket ->
          ServerResponse.created(
            UriComponentsBuilder.fromPath("ticket/" + ticket.getId()).build().toUri())
            .build());
  }

  private Mono<Ticket> setId(Ticket ticket) {
    return ticketRepository
      .count()
      .map(id -> Ticket.builder().id(id).description(ticket.getDescription()).title(ticket.getTitle()).build());
  }

  public Mono<ServerResponse> getTicketById(ServerRequest request) {
    long id = Long.parseLong(request.pathVariable("id"));

    return this.ticketRepository
      .findById(id)
      .map(Mapper::map)
      .flatMap(ticket -> ServerResponse.ok().contentType(APPLICATION_JSON).body(fromValue(ticket)))
      .switchIfEmpty(ServerResponse.notFound().build());
  }

  public Mono<ServerResponse> getTickets(ServerRequest serverRequest) {
    Flux<Ticket> tickets = ticketRepository.findAll().map(Mapper::map);
    return ServerResponse.ok().contentType(APPLICATION_JSON).body(tickets, Ticket.class);
  }
}
