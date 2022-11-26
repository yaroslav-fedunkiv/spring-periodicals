package com.epam.fedunkiv.periodicals.services;

import com.epam.fedunkiv.periodicals.dto.publishers.FullPublisherDto;
import com.epam.fedunkiv.periodicals.dto.publishers.CreatePublisherDto;
import com.epam.fedunkiv.periodicals.dto.publishers.UpdatePublisherDto;
import com.epam.fedunkiv.periodicals.dto.subscriptions.SubscribeDto;
import com.epam.fedunkiv.periodicals.model.Subscriptions;

import java.util.List;
import java.util.Optional;

public interface PublisherService {
    Optional<FullPublisherDto> createPublisher(CreatePublisherDto createPublisherDto);
    List<FullPublisherDto> getAll();
    List<FullPublisherDto> search(String title);
    void subscribe(String email, String title, SubscribeDto subscribeDto);
    Optional<FullPublisherDto> getByTitle(String title);
    FullPublisherDto deactivatePublisher(String title);
    UpdatePublisherDto updatePublisher(UpdatePublisherDto updatePublisher, String title);
    boolean isActive(String title);
    List<FullPublisherDto> getAllByPages(String page);
    List<FullPublisherDto> sortingBy(String sortingOption, String page);
    List<FullPublisherDto> getByTopic(String topic, String page);
}
