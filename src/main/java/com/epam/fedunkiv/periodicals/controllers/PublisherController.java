package com.epam.fedunkiv.periodicals.controllers;

import com.epam.fedunkiv.periodicals.dto.publishers.FullPublisherDto;
import com.epam.fedunkiv.periodicals.dto.publishers.CreatePublisherDto;
import com.epam.fedunkiv.periodicals.dto.publishers.UpdatePublisherDto;
import com.epam.fedunkiv.periodicals.dto.subscriptions.SubscribeDto;
import com.epam.fedunkiv.periodicals.dto.users.FullUserDto;
import com.epam.fedunkiv.periodicals.exceptions.NoSuchPublisherException;
import com.epam.fedunkiv.periodicals.exceptions.NotEnoughMoneyException;
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
    @Resource
    private UserService userService;


    @Operation(summary = "Subscribe a user to a publisher")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User was subscribed",
                    content = @Content),
            @ApiResponse(responseCode = "412", description = "User hasn't enough money",
                    content = @Content),
            @ApiResponse(responseCode = "400", description = "wrong user email or publisher title",
                    content = @Content)
    })
    @PostMapping("/get-by/{title}/{email}")
    public ResponseEntity<Object> subscribe(@PathVariable("email") String email,
                                            @PathVariable("title") String title,
                                            @RequestBody SubscribeDto subscribeDto) {
        log.info("start subscribing process");
        ResponseEntity<Object> responseEntity = null;
        Map<String, Object> responseBody = new LinkedHashMap<>();
        List<String> messages = new LinkedList<>();
        FullPublisherDto publisher = publisherService.getByTitle(title).orElse(null);
        FullUserDto user = userService.getByEmail(email).orElse(null);

        if (!publisherService.getByTitle(title).isEmpty()) {
            publisher = publisherService.getByTitle(title).get();
        } else {
            messages.add(title + " — publisher with the such title was not found");
        }

        if (!userService.getByEmail(email).isEmpty()) {
            user = userService.getByEmail(email).get();
        } else {
            messages.add(email + " — user with the such email was not found");
        }

        responseBody.put("status", publisher == null || user == null ? HttpStatus.BAD_REQUEST : null);
        responseBody.put("messages", messages);


        try {
            if (publisher != null && user != null) {
                subscribeDto.setPublisherId(publisher.getId());
                subscribeDto.setUserId(user.getId());
                userService.writeOffFromBalance(publisher.getPrice(), user.getEmail());
                publisherService.subscribe(subscribeDto);
                responseBody.put("message", email + " user was subscribed to '" + title + "' title");
                responseBody.put("status", HttpStatus.OK);
                responseEntity = new ResponseEntity<>(responseBody, HttpStatus.OK);
            } else {
                responseEntity = new ResponseEntity<>(responseBody, HttpStatus.BAD_REQUEST);
            }

        } catch (NotEnoughMoneyException e) {
            log.error("user hasn't enough money");
            messages.add(email + " user hasn't enough money");
            responseBody.put("messages", messages);
            responseBody.put("status", HttpStatus.PRECONDITION_FAILED);
            responseEntity = new ResponseEntity<>(responseBody, HttpStatus.PRECONDITION_FAILED);
        }
        return responseEntity;
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

    @PostMapping("/update") //fixme
    public void updatePublisher(@RequestBody @Valid UpdatePublisherDto updatePublisherDto) {
        publisherService.updatePublisher(updatePublisherDto);
        log.info("publisher updated {}", updatePublisherDto);
    }

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
            log.error("Publisher with such title was not found");
            return new ResponseEntity<>(title + " — the publisher with such a title was not found",
                    HttpStatus.NOT_FOUND);
        }

//
//        Map<String, Object> responseBody = new LinkedHashMap<>();
//        responseBody.put("message", title + " — publisher with the such title was not found");
//        responseBody.put("status", HttpStatus.NOT_FOUND.value() + " NOT_FOUND");
//
//        return publisherService.getByTitle(title).isEmpty()
//                ? new ResponseEntity<>(responseBody, HttpStatus.NOT_FOUND)
//                : new ResponseEntity<>(publisherService.getByTitle(title), HttpStatus.OK);
    }

    @Operation(summary = "Deactivate a publisher by its title")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Publisher as ddeactivated"),
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
            log.error("{} was not found", title);
            return new ResponseEntity<>(title + " was not found", HttpStatus.NOT_FOUND);
        }
    }

    @Operation(summary = "Get all publishers")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found all publishers",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = FullPublisherDto.class))})
    })
    @GetMapping("/get-all")
    public List<FullPublisherDto> getAll() {
        log.info("getting all publishers");
        return publisherService.getAll();
    }
}
