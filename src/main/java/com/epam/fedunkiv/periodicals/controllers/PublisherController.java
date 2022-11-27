package com.epam.fedunkiv.periodicals.controllers;

import com.epam.fedunkiv.periodicals.dto.publishers.FullPublisherDto;
import com.epam.fedunkiv.periodicals.dto.publishers.CreatePublisherDto;
import com.epam.fedunkiv.periodicals.dto.publishers.UpdatePublisherDto;
import com.epam.fedunkiv.periodicals.dto.subscriptions.SubscribeDto;
import com.epam.fedunkiv.periodicals.dto.users.FullUserDto;
import com.epam.fedunkiv.periodicals.exceptions.NoSuchPublisherException;
import com.epam.fedunkiv.periodicals.exceptions.NotEnoughMoneyException;
import com.epam.fedunkiv.periodicals.exceptions.UserIsAlreadySubscribedException;
import com.epam.fedunkiv.periodicals.model.Topics;
import com.epam.fedunkiv.periodicals.services.PublisherService;
import com.epam.fedunkiv.periodicals.services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.*;

@Log4j2
@RestController
@RequestMapping("/publishers")
public class PublisherController {
    @Resource
    private PublisherService publisherService;

    @Operation(summary = "Get a publisher by its title")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found the publisher",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = FullPublisherDto.class))}),
            @ApiResponse(responseCode = "404", description = "Publisher not found",
                    content = @Content)})
    @GetMapping("/get-by/{title}")
    public ResponseEntity<Object> getByTitle(@Parameter(description = "title of publisher to be searched")
                                             @PathVariable("title") String title) {
        log.info("getting publisher by title {}", title);
        try {
            return new ResponseEntity<>(publisherService.getByTitle(title), HttpStatus.OK);
        } catch (NoSuchPublisherException e) {
            log.error("Publisher with such title not found");
            return new ResponseEntity<>(title + " — the publisher with such a title not found",
                    HttpStatus.NOT_FOUND);
        }
    }

    @Operation(summary = "Get all publishers")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found all publishers",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = FullPublisherDto.class))})
    })
    @GetMapping("/get-all")
    public ResponseEntity<Object> getAll() {
        log.info("getting all publishers");
        return new ResponseEntity<>(publisherService.getAll(), HttpStatus.OK);
    }
    @Operation(summary = "Create a publisher")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Publisher was created",
                    content = @Content),
            @ApiResponse(responseCode = "400", description = "Bad request. Publisher wasn't created",
                    content = @Content)
    })
    @PostMapping("/create")
    public ResponseEntity<Object> createPublisher(@io.swagger.v3.oas.annotations.parameters.RequestBody(description = "publisher object to be created")
                                                  @RequestBody @Valid CreatePublisherDto publisher) {
        Map<String, Object> responseBody = new LinkedHashMap<>();
        publisherService.createPublisher(publisher);
        log.info("publisher is created {}", publisher);

        responseBody.put("message", publisher.getTitle() + "– publisher was created");
        responseBody.put("status", HttpStatus.OK);
        return new ResponseEntity<>(responseBody, HttpStatus.OK);
    }

    @Operation(summary = "Updated a publisher by its title")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Publisher was updated"),
            @ApiResponse(responseCode = "404", description = "Publisher not found")})
    @PatchMapping("/update/{title}")
    public ResponseEntity<Object> updatePublisher(@RequestBody @Valid UpdatePublisherDto updatePublisherDto,
                                                  @PathVariable("title") String title) {
        try{
            publisherService.updatePublisher(updatePublisherDto, title);
            log.info("updated publisher by title {}", title);
            return new ResponseEntity<>(title + " was updated", HttpStatus.OK);
        }catch (NoSuchPublisherException e){
            log.error("{} not found", title);
            return new ResponseEntity<>(title + " not found", HttpStatus.NOT_FOUND);
        }
    }

    @Operation(summary = "Subscribe a user to a publisher")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User was subscribed",
                    content = @Content),
            @ApiResponse(responseCode = "412", description = "User hasn't enough money",
                    content = @Content),
            @ApiResponse(responseCode = "400", description = "wrong user email or publisher title",
                    content = @Content),
            @ApiResponse(responseCode = "409", description = "user is already subscribed",
                    content = @Content)
    })
    @PostMapping("/get-by/{title}/{email}")
    public ResponseEntity<Object> subscribe(@PathVariable("email") String email,
                                            @PathVariable("title") String title,
                                            @RequestBody SubscribeDto subscribeDto) {
        log.info("start subscribing process");
        try {
            publisherService.subscribe(email, title, subscribeDto);
            log.info("user {} was subscribed", email);
            return new ResponseEntity<>(email + " user was subscribed to '" + title + "' title", HttpStatus.OK);
        }catch (NoSuchElementException e){
            log.error("wrong email or title");
            return new ResponseEntity<>("wrong email or title", HttpStatus.BAD_REQUEST);
        }catch(UserIsAlreadySubscribedException e){
            log.error("user is already subscribed");
            return new ResponseEntity<>(email+" this user is already subscribed", HttpStatus.CONFLICT);
        }catch (NotEnoughMoneyException e){
            log.error("user haven't enough money");
            return new ResponseEntity<>("user haven't enough money", HttpStatus.PRECONDITION_FAILED);
        }
    }

    @Operation(summary = "Deactivate a publisher by its title")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Publisher was deactivated"),
            @ApiResponse(responseCode = "404", description = "Publisher not found")})
    @DeleteMapping("/deactivate/{title}")
    public ResponseEntity<Object> deactivateByTitle(@PathVariable("title") String title) {
        try{
            if (!publisherService.isActive(title)){
                log.warn("publisher {} is already deactivated!", title);
                return new ResponseEntity<>(title + " – publisher is already deactivated", HttpStatus.CONFLICT);
            } else{
                publisherService.deactivatePublisher(title);
                log.info("deactivated publisher by title {}", title);
                return new ResponseEntity<>(title + " was deactivated", HttpStatus.OK);
            }
        } catch (NoSuchPublisherException e){
            log.error("{} not found", title);
            return new ResponseEntity<>(title + " not found", HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/get/all/{page}")
    public ResponseEntity<Object> getAllByPages(@PathVariable String page) {
        log.info("getting all publishers");
        return new ResponseEntity<>(publisherService.getAllByPages(page), HttpStatus.OK);
    }

    @GetMapping("/sort/by/{sort}/{page}")
    public ResponseEntity<Object> sortBy(@PathVariable String sort, @PathVariable String page) {
        if (sort.equals("price") || sort.equals("title")){
            log.info("sorting all publishers");
            return new ResponseEntity<>(publisherService.sortingBy(sort, page), HttpStatus.OK);
        }else {
            return new ResponseEntity<>("Incorrect sorting type (must be price or title)", HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/get/by/{topic}/{page}")
    public ResponseEntity<Object> getByTopic(@PathVariable String topic, @PathVariable String page) {
        try {
            Topics.valueOf(topic);
            log.info("sorting all publishers");
            return new ResponseEntity<>(publisherService.getByTopic(topic, page), HttpStatus.OK);
        } catch (IllegalArgumentException e){
            log.error("This topic doesn't exist: {}", topic);
            return new ResponseEntity<>(topic + " — Topic doesn't exist", HttpStatus.NOT_FOUND);
        }
    }

    @Operation(summary = "Get all publishers which match the search pattern")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found all publishers",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = FullPublisherDto.class))})
    })
    @GetMapping("/search/{title}")
    public ResponseEntity<Object> search(@PathVariable String title) {
        log.info("getting searched publishers");
        return new ResponseEntity<>(publisherService.search(title), HttpStatus.OK);
    }
}
