package com.example.demo.api.handler;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.web.reactive.function.BodyInserters.fromValue;

import com.example.demo.api.model.Ticket;
import com.example.demo.repository.TicketRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
public class TicketHandler {
  private TicketRepository ticketRepository;

  public TicketHandler(TicketRepository ticketRepository) {
    this.ticketRepository = ticketRepository;
  }

  public Mono<ServerResponse> createTicket(ServerRequest request) {
    Mono<Ticket> ticket = request.bodyToMono(Ticket.class);

    return ticketRepository.count().flatMap(id -> ticket.flatMap(
        x -> {
          Ticket ticketEntity = Ticket.builder().id(id).description(x.getDescription())
              .title(x.getTitle()).build();
          log.debug(ticketEntity.toString());
          return ticketRepository
              .save(ticketEntity)
              .flatMap(
                  y ->
                      ServerResponse.created(
                          UriComponentsBuilder.fromPath("ticket/" + ticketEntity.getId()).build().toUri())
                          .build());
        }));
  }

  public Mono<ServerResponse> getTicketById(ServerRequest request) {
    long id = Integer.valueOf(request.pathVariable("id"));
    Mono<Ticket> ticket = this.ticketRepository.findById(id);
    return ticket
        .flatMap(x -> ServerResponse.ok().contentType(APPLICATION_JSON).body(fromValue(x)))
        .switchIfEmpty(ServerResponse.notFound().build());
  }

  public Mono<ServerResponse> getTickets(ServerRequest serverRequest) {
    Flux<Ticket> tickets = ticketRepository.findAll();
    return ServerResponse.ok().contentType(APPLICATION_JSON).body(tickets, Ticket.class);
  }
}
