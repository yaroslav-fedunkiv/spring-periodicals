package com.epam.fedunkiv.periodicals.services;

import com.epam.fedunkiv.periodicals.dto.publishers.FullPublisherDto;
import com.epam.fedunkiv.periodicals.dto.publishers.CreatePublisherDto;
import com.epam.fedunkiv.periodicals.dto.publishers.UpdatePublisherDto;
import com.epam.fedunkiv.periodicals.dto.subscriptions.SubscribeDto;

import java.util.List;

public interface PublisherService {
    void createPublisher(CreatePublisherDto createPublisherDto);
    List<FullPublisherDto> getAll();
    void subscribe(SubscribeDto subscribeDto);
    FullPublisherDto getByTitle(String title);
    FullPublisherDto getById(String id);
    void deletePublisher(String title);
    void updatePublisher(UpdatePublisherDto updatePublisher);
}
