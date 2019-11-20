package com.example.demo.api.annotation;

import com.example.demo.api.model.Ticket;
import com.example.demo.repository.TicketRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@WebFluxTest(controllers = TicketController.class)
class TicketControllerTest {

  private static final String uri = "/v1/tickets";

  @MockBean private TicketRepository repository;

  @Autowired private WebTestClient webClient;
  
  @Test
  public void testCreateTicket() {
    Ticket ticket = Ticket.builder().id(1L).title("test").description("some description").build();

    webClient
        .post()
        .uri(uri)
        .contentType(MediaType.APPLICATION_JSON)
        .body(BodyInserters.fromValue(ticket))
        .exchange()
        .expectStatus()
        .isCreated()
    .expectBodyList(Ticket.class);

    Mockito.verify(repository).save(ticket);
  }

  @Test
  void testGetTicketByIdFound() {
    Ticket ticket = Ticket.builder().id(1L).build();
    Mockito.when(repository.findById(1L)).thenReturn(Mono.just(ticket));

    webClient
        .get()
        .uri(uri + "/1")
        .exchange()
        .expectStatus()
        .isOk()
        .expectHeader()
        .contentType(MediaType.APPLICATION_JSON)
        .expectBodyList(Ticket.class)
        .contains(ticket);
  }

  @Test
  void testGetTicketByIdNotFound() {
    Mockito.when(repository.findById(1L)).thenReturn(Mono.empty());

    webClient
        .get()
        .uri(uri + "/1")
        .exchange()
        .expectStatus()
        .isNotFound();
  }

  @Test
  void testGetTickets() {
    Ticket[] tickets =
        new Ticket[] {Ticket.builder().id(1L).build(), Ticket.builder().id(2L).build()};
    Mockito.when(repository.findAll()).thenReturn(Flux.fromArray(tickets));

    Ticket[] expected =
        new Ticket[] {Ticket.builder().id(1L).build(), Ticket.builder().id(2L).build()};

    webClient
        .get()
        .uri(uri)
        .exchange()
        .expectStatus()
        .isOk()
        .expectHeader()
        .contentType(MediaType.APPLICATION_JSON)
        .expectBodyList(Ticket.class)
        .contains(expected);
  }
}
