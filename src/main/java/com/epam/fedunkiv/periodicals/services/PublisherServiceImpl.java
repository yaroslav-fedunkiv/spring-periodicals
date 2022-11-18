package com.epam.fedunkiv.periodicals.services;

import com.epam.fedunkiv.periodicals.dto.publishers.FullPublisherDto;
import com.epam.fedunkiv.periodicals.dto.publishers.CreatePublisherDto;
import com.epam.fedunkiv.periodicals.dto.publishers.UpdatePublisherDto;
import com.epam.fedunkiv.periodicals.dto.subscriptions.SubscribeDto;
import com.epam.fedunkiv.periodicals.dto.users.FullUserDto;
import com.epam.fedunkiv.periodicals.exceptions.NoSuchPublisherException;
import com.epam.fedunkiv.periodicals.exceptions.UserIsAlreadySubscribedException;
import com.epam.fedunkiv.periodicals.model.Publisher;
import com.epam.fedunkiv.periodicals.model.Subscriptions;
import com.epam.fedunkiv.periodicals.model.Topics;
import com.epam.fedunkiv.periodicals.repositories.PublisherRepository;
import com.epam.fedunkiv.periodicals.repositories.SubscriptionRepository;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.transaction.Transactional;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

@Log4j2
@Service
public class PublisherServiceImpl implements PublisherService {
    @Resource
    private PublisherRepository publisherRepository;
    @Resource
    private UserService userService;
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
        FullPublisherDto publisher = getByTitle(updatePublisher.getOldTitle()).get();
        CreatePublisherDto editedPublisher = new CreatePublisherDto();

        String oldTitle = updatePublisher.getOldTitle();
        String newTitle = (updatePublisher.getTitle() == null ? updatePublisher.getOldTitle() : updatePublisher.getTitle());
        String topic = (updatePublisher.getTopic() == null ? publisher.getTopic() : updatePublisher.getTopic());
        String price = (updatePublisher.getPrice() == null ? publisher.getPrice() : updatePublisher.getPrice());
        String description = (updatePublisher.getDescription() == null ? publisher.getDescription() : updatePublisher.getDescription());

        log.warn("update "+oldTitle+" with fields:\n"+newTitle+"\n"+price+"\n"+topic+"\n"+description);
        publisherRepository.updatePublisher(newTitle, topic, Double.parseDouble(price), description, oldTitle);
    }

    @Override
    public List<FullPublisherDto> getAll() {
        log.info("start method getAll() in publisher service");
        return publisherRepository.findAll().stream()
                .map(e -> mapper.map(e, FullPublisherDto.class))
                .collect(Collectors.toList());
    }

    @Override
    public List<FullPublisherDto> getAllByPages(String page) {
        Pageable pagesWithThreeElements = PageRequest.of(Integer.parseInt(page), 3);
        log.info("start method getAll() in publisher service");
           return publisherRepository.findAll(pagesWithThreeElements)
                   .stream()
                   .map(e -> mapper.map(e, FullPublisherDto.class))
                   .collect(Collectors.toList());
    }

    @Override
    public List<FullPublisherDto> sortingBy(String sortingOption, String page) {
        Pageable pagesWithThreeElements = PageRequest.of(Integer.parseInt(page), 3, Sort.by(sortingOption));
        log.info("start method getAll() in publisher service");
           return publisherRepository.findAll(pagesWithThreeElements)
                   .stream()
                   .map(e -> mapper.map(e, FullPublisherDto.class))
                   .collect(Collectors.toList());
    }

    @Override
    public List<FullPublisherDto> getByTopic(String topic, String page) {
        Pageable pagesWithThreeElements = PageRequest.of(Integer.parseInt(page), 3);
        log.info("start method getAll() in publisher service");
           return publisherRepository.findByTopic(pagesWithThreeElements, Topics.valueOf(topic))
                   .stream()
                   .map(e -> mapper.map(e, FullPublisherDto.class))
                   .collect(Collectors.toList());
    }

    @Override
    public List<FullPublisherDto> search(String title) {
        log.info("start method search() in publisher service");
        return publisherRepository.searchByTitle(title).stream()
                .map(e -> mapper.map(e, FullPublisherDto.class))
                .collect(Collectors.toList());
    }


    @Override
    public Optional<FullPublisherDto> getByTitle(String title) {
        log.info("start method getByTitle() in publisher service {}", title);
        try {
            Publisher publisher = publisherRepository.getByTitle(title);
            return Optional.of(mapper.map(publisher, FullPublisherDto.class));
        } catch (IllegalArgumentException e) {
            log.info("This title was not found {}", title);
            throw new NoSuchPublisherException();
        }
    }

    @Override
    public FullPublisherDto getById(String id) {
        log.info("start method getById() in publisher service {}", id);
        FullPublisherDto fullPublisherDto = mapper.map(publisherRepository.getById(Long.parseLong(id)), FullPublisherDto.class);
        log.info("start method getById() in publisher service {}", fullPublisherDto);
        return fullPublisherDto;
    }

    @Override
    @Transactional
    public void deactivatePublisher(String title) {
        log.info("start method deletePublisher() by title in publisher service {}", title);
            publisherRepository.deactivatePublisher(title);
    }
    @Override
    public boolean isActive(String title){
        log.info("check if user with such email {} is active", title);
        return Boolean.parseBoolean(getByTitle(title).get().getIsActive());
    }

    @Transactional
    @Override
    public void subscribe(String email, String title, SubscribeDto subscribeDto){
        FullUserDto user = userService.getByEmail(email).orElseThrow();
        FullPublisherDto publisher = getByTitle(title).orElseThrow();
        subscribeDto.setUserId(user.getId());
        subscribeDto.setPublisherId(publisher.getId());
        if (!isSubscribed(email, title)){
            userService.writeOffFromBalance(publisher.getPrice(), user.getEmail());
            subscriptionRepository.save(mapper.map(subscribeDto, Subscriptions.class));
            log.info("user was subscribe inside subscribe() method in publisherServiceImpl {} ", subscribeDto);
        } else{
            log.error("user is already subscribed");
            throw new UserIsAlreadySubscribedException();
        }
    }
    private boolean isSubscribed(String email, String title){
        FullUserDto user = userService.getByEmail(email).orElseThrow();
        FullPublisherDto publisher = getByTitle(title).orElseThrow();
        List<Subscriptions> subscriptions = subscriptionRepository.findByPublisherIdAndUserId(Long.parseLong(publisher.getId()), Long.parseLong(user.getId()));
        return !subscriptions.isEmpty();
    }
}
