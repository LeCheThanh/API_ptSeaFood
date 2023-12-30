package seaFood.PTseafood.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import seaFood.PTseafood.dto.CreateEventRequest;
import seaFood.PTseafood.entity.Event;
import seaFood.PTseafood.service.EventService;

import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin
@RequestMapping("/api/events")
public class EventController {
    @Autowired
    private EventService eventService;

    @PostMapping
    public ResponseEntity<?> addEvent(@RequestBody CreateEventRequest eventRequest) {
        try{
            Event event = eventService.createEvent(eventRequest);
            return ResponseEntity.ok(event);
        }catch (RuntimeException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    @GetMapping("/active")
    public ResponseEntity<?> getActiveEvents() {
        try {
            List<Event> activeEvents = eventService.getActiveEvents();
            return ResponseEntity.ok(activeEvents);
        }catch (Exception e){
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }
    @GetMapping("/all")
    public ResponseEntity<?> getAllEvents() {
        List<Event> allEvents = eventService.getAllEvents();
        return ResponseEntity.ok(allEvents);
    }
    @PutMapping("/{id}")
    public ResponseEntity<?> updateEvent(@PathVariable Long id, @RequestBody CreateEventRequest updateRequest) {
        try {
            Event updatedEvent = eventService.updateEvent(id, updateRequest);
            return ResponseEntity.ok(updatedEvent);
        } catch (RuntimeException e) {
            // Log the exception details and return an appropriate error response
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable Long id){
        try{
            Optional<Event> event = eventService.getById(id);
            if(event.isEmpty()){

                return ResponseEntity.badRequest().body("khong co event");
            }
            Event haveEvent = event.get();
            return ResponseEntity.ok(haveEvent);
        }catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
