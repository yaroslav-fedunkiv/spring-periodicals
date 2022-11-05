package com.epam.fedunkiv.periodicals.services;

import com.epam.fedunkiv.periodicals.dto.publishers.FullPublisherDto;
import com.epam.fedunkiv.periodicals.dto.publishers.CreatePublisherDto;
import com.epam.fedunkiv.periodicals.dto.publishers.UpdatePublisherDto;
import com.epam.fedunkiv.periodicals.dto.subscriptions.SubscribeDto;

import java.util.List;
import java.util.Optional;

public interface PublisherService {
    void createPublisher(CreatePublisherDto createPublisherDto);
    List<FullPublisherDto> getAll();
    void subscribe(SubscribeDto subscribeDto);
    Optional<FullPublisherDto> getByTitle(String title);
    FullPublisherDto getById(String id);
    void deactivatePublisher(String title);
    void updatePublisher(UpdatePublisherDto updatePublisher);
    boolean isActive(String title);
}
