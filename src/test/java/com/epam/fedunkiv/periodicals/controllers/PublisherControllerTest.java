package com.epam.fedunkiv.periodicals.controllers;

import com.epam.fedunkiv.periodicals.dto.publishers.CreatePublisherDto;
import com.epam.fedunkiv.periodicals.dto.publishers.FullPublisherDto;
import com.epam.fedunkiv.periodicals.dto.publishers.UpdatePublisherDto;
import com.epam.fedunkiv.periodicals.dto.subscriptions.SubscribeDto;
import com.epam.fedunkiv.periodicals.dto.users.CreateUserDto;
import com.epam.fedunkiv.periodicals.dto.users.FullUserDto;
import com.epam.fedunkiv.periodicals.dto.users.UpdateUserDto;
import com.epam.fedunkiv.periodicals.exceptions.NoSuchPublisherException;
import com.epam.fedunkiv.periodicals.exceptions.NoSuchUserException;
import com.epam.fedunkiv.periodicals.exceptions.NotEnoughMoneyException;
import com.epam.fedunkiv.periodicals.exceptions.UserIsAlreadySubscribedException;
import com.epam.fedunkiv.periodicals.model.Publisher;
import com.epam.fedunkiv.periodicals.model.Topics;
import com.epam.fedunkiv.periodicals.services.PublisherService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static groovy.json.JsonOutput.toJson;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class PublisherControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PublisherService publisherService;

    private FullPublisherDto publisher;

    @BeforeEach
    void init() {
        publisher = new FullPublisherDto("1", "Time", "NEWS", "70.05", "Time description", "true", LocalDateTime.now().toString(), LocalDateTime.now().toString());
    }

    @Test
    void GetAllPublishers_positiveTest() throws Exception{
        lenient().when(publisherService.getAll()).thenReturn(List.of(publisher));
        mockMvc.perform(get("/publishers/get-all"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id", is("1")))
                .andExpect(jsonPath("$[0].title", is("Time")))
                .andExpect(jsonPath("$[0].topic", is("NEWS")));
        verify(publisherService, times(1)).getAll();
    }

    @Test
    void GetByTitle_positiveTest() throws Exception{
        lenient().when(publisherService.getByTitle("Time")).thenReturn(Optional.of(publisher));
        mockMvc.perform(get("/publishers/get-by/Time"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is("1")))
                .andExpect(jsonPath("$.title", is("Time")))
                .andExpect(jsonPath("$.topic", is("NEWS")));
        verify(publisherService, times(1)).getByTitle("Time");
    }

    @Test
    void GetByTitle_negativeTest() throws Exception{
        lenient().when(publisherService.getByTitle("Time")).thenThrow(NoSuchPublisherException.class);
        mockMvc.perform(get("/publishers/get-by/Time"))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Time — the publisher with such a title not found"));
        verify(publisherService, times(1)).getByTitle("Time");
    }

    @Test
    void CreatePublisher_positiveTest() throws Exception{
        CreatePublisherDto createPublisherDto = new CreatePublisherDto("Time", "NEWS", "70.05", "Time description");
        when(publisherService.createPublisher(createPublisherDto)).thenReturn(Optional.of(publisher));

        mockMvc.perform(post("/publishers/create")
                        .content(toJson(createPublisherDto))
                        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message", is("Time– publisher was created")))
                .andExpect(jsonPath("$.status", is("OK")));
    }

    @Test
    void CreatePublisher_checkValidation_negativeTest() throws Exception{
        CreatePublisherDto createPublisherDto = new CreatePublisherDto("", "NEWSq", "", "Time description");
        when(publisherService.createPublisher(createPublisherDto)).thenReturn(Optional.of(publisher));

        mockMvc.perform(post("/publishers/create")
                        .content(toJson(createPublisherDto))
                        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status", is(400)))
                .andExpect(jsonPath("$.errors", hasSize(3)))
                .andExpect(jsonPath("$.errors", hasItem("length of publisher's title must be between 1 and 50 symbols")))
                .andExpect(jsonPath("$.errors", hasItem("the price must follow the pattern: X.XX, XX.XX, XXX.XX and can't be empty")))
                .andExpect(jsonPath("$.errors", hasItem("topic of publisher can't be empty and must be chosen from list of topics (FASHION, SCIENCE, ECONOMY, NEWS, MUSIC, NATURE, OTHER)")))
                .andExpect(jsonPath("$.fields", hasSize(3)))
                .andExpect(jsonPath("$.fields", hasItem("title")))
                .andExpect(jsonPath("$.fields", hasItem("price")))
                .andExpect(jsonPath("$.fields", hasItem("topic")));
    }

    @Test
    void UpdatePublisher_positiveTest() throws Exception{
        UpdatePublisherDto updatePublisherDto = new UpdatePublisherDto();
        updatePublisherDto.setTopic("OTHER");
        updatePublisherDto.setPrice("100.99");
        publisher.setTopic("OTHER");
        publisher.setPrice("100.99");
        when(publisherService.updatePublisher(updatePublisherDto, "Time")).thenReturn(updatePublisherDto);

        mockMvc.perform(patch("/publishers/update/{title}", "Time")
                        .content(toJson(updatePublisherDto))
                        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("Time was updated"));
    }

    @Test
    void UpdatePublisher_negativeTest() throws Exception{
        UpdatePublisherDto updatePublisherDto = new UpdatePublisherDto();
        when(publisherService.updatePublisher(any(UpdatePublisherDto.class), eq("Time"))).thenThrow(NoSuchPublisherException.class);

        mockMvc.perform(patch("/publishers/update/{title}", "Time")
                        .content(toJson(updatePublisherDto))
                        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Time not found"));
    }

    @Test
    void DeactivatePublisher_negativeTest() throws Exception{
        when(publisherService.isActive(publisher.getTitle())).thenReturn(false);

        mockMvc.perform(delete("/publishers/deactivate/{title}", publisher.getTitle()))
                .andExpect(status().isConflict())
                .andExpect(content().string(publisher.getTitle() + " – publisher is already deactivated"));
    }

    @Test
    void DeactivatePublisher_positiveTest() throws Exception{
        when(publisherService.isActive(publisher.getTitle())).thenReturn(true);

        mockMvc.perform(delete("/publishers/deactivate/{title}", publisher.getTitle()))
                .andExpect(status().isOk())
                .andExpect(content().string(publisher.getTitle() + " was deactivated"));
    }

    @Test
    void DeactivatePublisher_checkEmail_negativeTest() throws Exception{
        when(publisherService.isActive(publisher.getTitle())).thenThrow(NoSuchPublisherException.class);

        mockMvc.perform(delete("/publishers/deactivate/{title}", publisher.getTitle()))
                .andExpect(status().isNotFound())
                .andExpect(content().string(publisher.getTitle() + " not found"));
    }

    @Test
    void searchPublisher_positiveTest() throws Exception{
        FullPublisherDto theNewYorkTimes = new FullPublisherDto("2", "The New York Times", "NEWS", "85.05", "The New York Times description", "true", LocalDateTime.now().toString(), LocalDateTime.now().toString());

        when(publisherService.search("the")).thenReturn(List.of(publisher, theNewYorkTimes));

        mockMvc.perform(get("/publishers/search/the"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title", is("Time")))
                .andExpect(jsonPath("$[1].title", is("The New York Times")));
    }

    @Test
    void GetAllPublishersByPages_positiveTest() throws Exception{
        FullPublisherDto publisher2 = new FullPublisherDto();
        publisher2.setTitle("Fashion");
        FullPublisherDto publisher3 = new FullPublisherDto();
        publisher3.setTitle("Pump");
        when(publisherService.getAllByPages("0")).thenReturn(List.of(publisher, publisher2, publisher3));

        mockMvc.perform(get("/publishers/get/all/0"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title", is("Time")))
                .andExpect(jsonPath("$[1].title", is("Fashion")))
                .andExpect(jsonPath("$[2].title", is("Pump")));
    }

    @Test
    void SubscribeUser_PositiveTest() throws Exception{
        SubscribeDto subscribeDto = new SubscribeDto();
        subscribeDto.setAddress("Lviv, Sadova st. 25");
        subscribeDto.setSubscriptionPeriod("12");
        when(publisherService.subscribe("john@gmmail.com", "Time", subscribeDto)).thenReturn(subscribeDto);
        mockMvc.perform(post("/publishers/get-by/Time/john@gmmail.com")
            .content(toJson(subscribeDto))
            .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("john@gmmail.com user was subscribed to 'Time' title"));

    }

    @Test
    void SubscribeUser_UserIsAlreadySubscribedException_NegativeTest() throws Exception{
        SubscribeDto subscribeDto = new SubscribeDto();
        subscribeDto.setAddress("Lviv, Sadova st. 25");
        subscribeDto.setSubscriptionPeriod("12");
        when(publisherService.subscribe(eq("john@gmmail.com"), eq("Time"),  any(SubscribeDto.class)))
                .thenThrow(new UserIsAlreadySubscribedException());

        mockMvc.perform(post("/publishers/get-by/Time/john@gmmail.com")
            .content(toJson(subscribeDto))
            .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
                .andExpect(status().isConflict())
                .andExpect(content().string("john@gmmail.com this user is already subscribed"));

    }

    @Test
    void SubscribeUser_NoSuchElementException_NegativeTest() throws Exception{
        SubscribeDto subscribeDto = new SubscribeDto();
        subscribeDto.setAddress("Lviv, Sadova st. 25");
        subscribeDto.setSubscriptionPeriod("12");
        when(publisherService.subscribe(eq("john@gmmail.com"), eq("Time"),  any(SubscribeDto.class)))
                .thenThrow(new NoSuchElementException());

        mockMvc.perform(post("/publishers/get-by/Time/john@gmmail.com")
            .content(toJson(subscribeDto))
            .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("wrong email or title"));

    }

    @Test
    void SubscribeUser_NotEnoughMoneyException_NegativeTest() throws Exception{
        SubscribeDto subscribeDto = new SubscribeDto();
        subscribeDto.setAddress("Lviv, Sadova st. 25");
        subscribeDto.setSubscriptionPeriod("12");
        when(publisherService.subscribe(eq("john@gmmail.com"), eq("Time"),  any(SubscribeDto.class)))
                .thenThrow(new NotEnoughMoneyException());

        mockMvc.perform(post("/publishers/get-by/Time/john@gmmail.com")
            .content(toJson(subscribeDto))
            .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
                .andExpect(status().isPreconditionFailed())
                .andExpect(content().string("user haven't enough money"));

    }

    @Test
    void SortBy_title_PositiveTest() throws Exception{
        FullPublisherDto publisher2 = new FullPublisherDto();
        publisher2.setTitle("Fashion");
        FullPublisherDto publisher3 = new FullPublisherDto();
        publisher3.setTitle("Pump");
        when(publisherService.sortingBy("title", "0")).thenReturn(Stream.of(publisher, publisher2, publisher3)
                .sorted(Comparator.comparing(FullPublisherDto::getTitle))
                .collect(Collectors.toList()));

        mockMvc.perform(get("/publishers/sort/by/title/0"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title", is("Fashion")))
                .andExpect(jsonPath("$[1].title", is("Pump")))
                .andExpect(jsonPath("$[2].title", is("Time")));
    }

    @Test
    void SortBy_price_PositiveTest() throws Exception{
        FullPublisherDto publisher2 = new FullPublisherDto("2", "Fashion", "FASHION", "22.55", "Time description", "true", LocalDateTime.now().toString(), LocalDateTime.now().toString());
        FullPublisherDto publisher3 = new FullPublisherDto("3", "Pump", "FASHION", "10.33", "Time description", "true", LocalDateTime.now().toString(), LocalDateTime.now().toString());

        when(publisherService.sortingBy("price", "0")).thenReturn(List.of(publisher3, publisher2, publisher));

        mockMvc.perform(get("/publishers/sort/by/price/0"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title", is("Pump")))
                .andExpect(jsonPath("$[0].price", is("10.33")))
                .andExpect(jsonPath("$[1].title", is("Fashion")))
                .andExpect(jsonPath("$[1].price", is("22.55")))
                .andExpect(jsonPath("$[2].title", is("Time")))
                .andExpect(jsonPath("$[2].price", is("70.05")));
    }

    @Test
    void SortBy_NegativeTest() throws Exception{
        mockMvc.perform(get("/publishers/sort/by/priceqq/0"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Incorrect sorting type (must be price or title)"));
    }

    @Test
    void GetByTopic_positiveTest() throws Exception{
        FullPublisherDto publisher2 = new FullPublisherDto("2", "Fashion", "FASHION", "22.55", "Time description", "true", LocalDateTime.now().toString(), LocalDateTime.now().toString());
        FullPublisherDto publisher3 = new FullPublisherDto("3", "Pump", "FASHION", "10.33", "Time description", "true", LocalDateTime.now().toString(), LocalDateTime.now().toString());

        when(publisherService.getByTopic("FASHION", "0")).thenReturn(List.of(publisher2, publisher3));
        mockMvc.perform(get("/publishers/get/by/FASHION/0"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title", is("Fashion")))
                .andExpect(jsonPath("$[1].title", is("Pump")))
                .andExpect(jsonPath("$[0].topic", is("FASHION")))
                .andExpect(jsonPath("$[1].topic", is("FASHION")));
    }

    @Test
    void GetByTopic_negativeTest() throws Exception{
        mockMvc.perform(get("/publishers/get/by/FASHIONqq/0"))
                .andExpect(status().isNotFound())
                .andExpect(content().string("FASHIONqq — Topic doesn't exist"));
    }
}