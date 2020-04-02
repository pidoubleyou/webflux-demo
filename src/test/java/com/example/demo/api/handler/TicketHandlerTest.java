package com.example.demo.api.handler;

import com.example.demo.api.model.Ticket;
import com.example.demo.repository.TicketEntity;
import com.example.demo.repository.TicketRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@WebFluxTest
class TicketHandlerTest {

  private static final String TICKET_URI = "/v2/tickets";

  @MockBean
  private TicketRepository repository;

  private WebTestClient webClient;

  @BeforeEach
  public void setUp() {
    TicketRoute route = new TicketRoute(repository);
    webClient = WebTestClient.bindToRouterFunction(route.route()).build();
  }

  @Test
  public void testCreateTicket() {
    Ticket ticket = Ticket.builder().title("test").description("some description").build();
    TicketEntity createdTicketEntity = TicketEntity.builder().id(9L).title("test").description("some description").build();

    Mockito.when(repository.count()).thenReturn(Mono.just(9L));
    Mockito.when(repository.save(createdTicketEntity)).thenReturn(Mono.just(createdTicketEntity));

    webClient
      .post()
      .uri(TICKET_URI)
      .contentType(MediaType.APPLICATION_JSON)
      .body(BodyInserters.fromValue(ticket))
      .exchange()
      .expectStatus()
      .isCreated();

    Mockito.verify(repository).save(createdTicketEntity);
  }

  @Test
  void testGetTicketByIdFound() {
    Ticket expectedTicket = Ticket.builder().id(1L).build();
    TicketEntity ticketEntity = TicketEntity.builder().id(1L).build();
    Mockito.when(repository.findById(1L)).thenReturn(Mono.just(ticketEntity));

    webClient
      .get()
      .uri(TICKET_URI + "/1")
      .exchange()
      .expectStatus()
      .isOk()
      .expectHeader()
      .contentType(MediaType.APPLICATION_JSON)
      .expectBodyList(Ticket.class)
      .contains(expectedTicket);
  }

  @Test
  void testGetTicketByIdNotFound() {
    Mockito.when(repository.findById(1L)).thenReturn(Mono.empty());

    webClient.get().uri(TICKET_URI + "/1").exchange().expectStatus().isNotFound();
  }

  @Test
  void testGetTickets() {
    TicketEntity[] ticketEntities =
      new TicketEntity[]{TicketEntity.builder().id(1L).build(), TicketEntity.builder().id(2L).build()};
    Mockito.when(repository.findAll()).thenReturn(Flux.fromArray(ticketEntities));

    Ticket[] expected =
      new Ticket[]{Ticket.builder().id(1L).build(), Ticket.builder().id(2L).build()};

    webClient
      .get()
      .uri(TICKET_URI)
      .exchange()
      .expectStatus()
      .isOk()
      .expectHeader()
      .contentType(MediaType.APPLICATION_JSON)
      .expectBodyList(Ticket.class)
      .contains(expected);
  }
}
