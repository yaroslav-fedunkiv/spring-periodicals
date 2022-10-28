package com.epam.fedunkiv.periodicals.controllers;

import com.epam.fedunkiv.periodicals.dto.publishers.FullPublisherDto;
import com.epam.fedunkiv.periodicals.dto.publishers.CreatePublisherDto;
import com.epam.fedunkiv.periodicals.dto.publishers.UpdatePublisherDto;
import com.epam.fedunkiv.periodicals.dto.subscriptions.SubscribeDto;
import com.epam.fedunkiv.periodicals.services.PublisherService;
import com.epam.fedunkiv.periodicals.services.UserService;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.List;

@Log4j2
@RestController
@RequestMapping("/publishers")
public class PublisherController {
    @Resource
    private PublisherService publisherService;
    @Resource
    private UserService userService;


    @PostMapping("/get-by/{title}/{email}")
    public void subscribe(@PathVariable("email") String email, @PathVariable("title") String title, @RequestBody SubscribeDto subscribeDto){
        subscribeDto.setPublisherId(publisherService.getByTitle(title).getId());
        subscribeDto.setUserId(userService.getByEmail(email).get().getId());
        log.info("start subscribing process");
        userService.writeOffFromBalance(publisherService.getByTitle(title).getPrice(), email);
        publisherService.subscribe(subscribeDto);
    }

    @PostMapping("/create")
    public boolean createPublisher(@RequestBody @Valid CreatePublisherDto publisher){
        publisherService.createPublisher(publisher);
        log.info("publisher is created {}", publisher);
        return publisherService.getByTitle(publisher.getTitle()).getTitle().equals(publisher.getTitle());
    }

    @PostMapping("/update")
    public void updatePublisher(@RequestBody @Valid UpdatePublisherDto updatePublisherDto){
        publisherService.updatePublisher(updatePublisherDto);
        log.info("publisher updated {}", updatePublisherDto);
    }

    @GetMapping("/get-by/{title}")
    public FullPublisherDto getByTitle(@PathVariable("title") String title){
        log.info("getting publisher by title {}", title);
        return publisherService.getByTitle(title);
    }

    @GetMapping("/delete-by/{title}")
    public void deleteByTitle(@PathVariable("title") String title){
        publisherService.deletePublisher(title);
        log.info("deleting publisher by title {}", title);
    }

    @GetMapping("/get-all")
    public List<FullPublisherDto> getAll(){
        log.info("getting all publishers");
        return publisherService.getAll();
    }
}
