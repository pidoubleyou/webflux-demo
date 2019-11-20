package com.example.demo.api.annotation;

import com.example.demo.api.model.Ticket;
import com.example.demo.repository.TicketRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController()
@RequestMapping("/v1/tickets")
public class TicketController {

  private TicketRepository ticketRepository;

  @Autowired
  public TicketController(TicketRepository ticketRepository) {
    this.ticketRepository = ticketRepository;
  }

  @PostMapping()
  @ResponseStatus(HttpStatus.CREATED)
  public Mono<Ticket> createTicket(@RequestBody Ticket ticket) {
    return ticketRepository.save(ticket);
  }

  @GetMapping("/{id}")
  public Mono<Ticket> getTicketById(@PathVariable Long id) {
    return ticketRepository.findById(id).switchIfEmpty(Mono.error(NotFoundException::new));
  }

  @GetMapping()
  public Flux<Ticket> getTickets() {
    return ticketRepository.findAll();
  }
}
