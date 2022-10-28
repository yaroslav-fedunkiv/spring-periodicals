package com.epam.fedunkiv.periodicals.services;

import com.epam.fedunkiv.periodicals.dto.publishers.FullPublisherDto;
import com.epam.fedunkiv.periodicals.dto.publishers.CreatePublisherDto;
import com.epam.fedunkiv.periodicals.dto.publishers.UpdatePublisherDto;
import com.epam.fedunkiv.periodicals.dto.subscriptions.SubscribeDto;
import com.epam.fedunkiv.periodicals.model.Publisher;
import com.epam.fedunkiv.periodicals.model.Subscriptions;
import com.epam.fedunkiv.periodicals.repositories.PublisherRepository;
import com.epam.fedunkiv.periodicals.repositories.SubscriptionRepository;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Log4j2
@Service
public class PublisherServiceImpl implements PublisherService {
    @Resource
    private PublisherRepository publisherRepository;
    @Resource
    private SubscriptionRepository subscriptionRepository;

    @Resource
    private ModelMapper mapper;

    @Override
    public void createPublisher(CreatePublisherDto createPublisherDto) {
        publisherRepository.save(mapper.map(createPublisherDto, Publisher.class));
        log.info("publisher is saved {}", createPublisherDto);
    }

    @Transactional
    @Override
    public void updatePublisher(UpdatePublisherDto updatePublisher){
        FullPublisherDto publisher = getByTitle(updatePublisher.getOldTitle());
        CreatePublisherDto editedPublisher = new CreatePublisherDto();
        editedPublisher.setTitle(updatePublisher.getTitle() == null ? updatePublisher.getOldTitle() : updatePublisher.getTitle());
        editedPublisher.setTopic(updatePublisher.getTopic() == null ? publisher.getTopic() : updatePublisher.getTopic());
        editedPublisher.setPrice(updatePublisher.getPrice() == null ? publisher.getPrice() : updatePublisher.getPrice());
        editedPublisher.setDescription(updatePublisher.getDescription() == null ? publisher.getDescription() : updatePublisher.getDescription());

        deletePublisher(updatePublisher.getOldTitle());
        createPublisher(editedPublisher);
        log.info("publisher is edited {}", editedPublisher);
    }

    @Override
    public List<FullPublisherDto> getAll() {
        log.info("start method getAll() in publisher service");
        return publisherRepository.findAll().stream()
                .map(e -> mapper.map(e, FullPublisherDto.class))
                .collect(Collectors.toList());
    }

    @Override
    public FullPublisherDto getByTitle(String title) {
        log.info("start method getByTitle() in publisher service {}", title);
        return mapper.map(publisherRepository.getByTitle(title), FullPublisherDto.class);
    }

    @Override
    public FullPublisherDto getById(String id) {
        log.info("start method getById() in publisher service {}", id);
        FullPublisherDto fullPublisherDto = mapper.map(publisherRepository.getById(Long.parseLong(id)), FullPublisherDto.class);
        log.info("start method getById() in publisher service {}", fullPublisherDto);
        return fullPublisherDto;
    }

    @Override
    public void subscribe(SubscribeDto subscribeDto){
        subscriptionRepository.save(mapper.map(subscribeDto, Subscriptions.class));
        log.info("user was subscribe inside subscribe() method in publisherServiceImpl {} ", subscribeDto);
    }

    @Override
    @Transactional
    public void deletePublisher(String title) {
        log.info("start method deletePublisher() by title in publisher service {}", title);
        publisherRepository.deleteByTitle(title);
    }
}
