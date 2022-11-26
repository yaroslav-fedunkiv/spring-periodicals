package com.epam.fedunkiv.periodicals.controllers;

import com.epam.fedunkiv.periodicals.dto.publishers.CreatePublisherDto;
import com.epam.fedunkiv.periodicals.dto.publishers.FullPublisherDto;
import com.epam.fedunkiv.periodicals.dto.publishers.UpdatePublisherDto;
import com.epam.fedunkiv.periodicals.dto.users.CreateUserDto;
import com.epam.fedunkiv.periodicals.dto.users.FullUserDto;
import com.epam.fedunkiv.periodicals.dto.users.UpdateUserDto;
import com.epam.fedunkiv.periodicals.exceptions.NoSuchPublisherException;
import com.epam.fedunkiv.periodicals.exceptions.NoSuchUserException;
import com.epam.fedunkiv.periodicals.model.Topics;
import com.epam.fedunkiv.periodicals.services.PublisherService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
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
import java.util.List;
import java.util.Optional;

import static groovy.json.JsonOutput.toJson;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

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
        when(publisherService.updatePublisher(updatePublisherDto, "Time")).thenThrow(NoSuchPublisherException.class);
        when(publisherService.getByTitle("Time")).thenThrow(NoSuchPublisherException.class);

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
}