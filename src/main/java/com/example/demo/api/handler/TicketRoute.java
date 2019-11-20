package com.example.demo.api.handler;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.http.MediaType.APPLICATION_STREAM_JSON;
import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RequestPredicates.POST;
import static org.springframework.web.reactive.function.server.RequestPredicates.accept;
import static org.springframework.web.reactive.function.server.RequestPredicates.contentType;

import com.example.demo.repository.TicketRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

@Component
public class TicketRoute {

  private TicketRepository ticketRepository;

  @Autowired
  public TicketRoute(TicketRepository ticketRepository) {
    this.ticketRepository = ticketRepository;
  }

  @Bean
  public RouterFunction<ServerResponse> route() {
    TicketHandler handler = new TicketHandler(ticketRepository);
    return RouterFunctions.route(POST("/v2/tickets").and(contentType(APPLICATION_JSON)), handler::createTicket)
        .andRoute(GET("/v2/tickets").and(accept(APPLICATION_STREAM_JSON)), handler::getTickets)
        .andRoute(GET("/v2/tickets/{id}").and(accept(APPLICATION_JSON)), handler::getTicketById);
  }
}
