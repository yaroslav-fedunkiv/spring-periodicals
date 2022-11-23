package com.epam.fedunkiv.periodicals.services;

import com.epam.fedunkiv.periodicals.dto.publishers.FullPublisherDto;
import com.epam.fedunkiv.periodicals.dto.publishers.UpdatePublisherDto;
import com.epam.fedunkiv.periodicals.dto.subscriptions.SubscribeDto;
import com.epam.fedunkiv.periodicals.dto.users.FullUserDto;
import com.epam.fedunkiv.periodicals.exceptions.NoSuchPublisherException;
import com.epam.fedunkiv.periodicals.exceptions.NoSuchUserException;
import com.epam.fedunkiv.periodicals.exceptions.UserIsAlreadySubscribedException;
import com.epam.fedunkiv.periodicals.model.*;
import com.epam.fedunkiv.periodicals.repositories.PublisherRepository;
import com.epam.fedunkiv.periodicals.repositories.SubscriptionRepository;
import com.epam.fedunkiv.periodicals.repositories.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.*;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
class PublisherServiceImplTest {
    @Mock
    private UserRepository userRepository;
    @Mock
    private PublisherRepository publisherRepository;
    @Mock
    private SubscriptionRepository subscriptionRepository;
    @InjectMocks
    private UserServiceImpl userService;
    @InjectMocks
    private PublisherServiceImpl publisherService;
    private final ModelMapper mapper = new ModelMapper();
    private User user;
    private Publisher publisher;

    @BeforeEach
    void init() {
        publisher = new Publisher(1L, "Time", Topics.NEWS, 70.05, "Time description",null, true, LocalDateTime.now(), LocalDateTime.now());

        user = new User();
        user.setId(1L);
        user.setFullName("John Snow");
        user.setRole(Role.CLIENT);
        user.setEmail("john@gmail.com");
        user.setAddress("address");
        user.setBalance(175.05);
        user.setPassword("123456Q@q");
        user.setIsActive(true);
        user.setCreated(LocalDateTime.now());
        user.setUpdated(LocalDateTime.now());
        userService = new UserServiceImpl(userRepository, mapper);
        publisherService = new PublisherServiceImpl(publisherRepository, userService, subscriptionRepository, mapper);
    }

    @DisplayName("JUnit test for getByTitle() method (positive scenario)")
    @Test
    void GetPublisherByTitle_positiveTest(){
        when(publisherRepository.findByTitle("Time")).thenReturn(publisher);
        FullPublisherDto publisherDto = publisherService.getByTitle("Time").orElseThrow(NoSuchPublisherException::new);
        Assertions.assertNotNull(publisher);
        assertThat(publisherDto.getDescription()).isEqualTo(publisher.getDescription());
    }

    @DisplayName("JUnit test for getByTitle() method (negative scenario)")
    @Test
    void GetPublisherByTitle_negativeTest(){
        when(publisherRepository.findByTitle("Timeq")).thenThrow(NoSuchPublisherException.class);
        Assertions.assertThrows(NoSuchPublisherException.class, ()-> {
            publisherService.getByTitle("Timeq").orElseThrow(NoSuchPublisherException::new);
        });
    }

    @DisplayName("JUnit test for getAll() method")
    @Test
    void GetAllPublishers_positiveTest(){
        when(publisherRepository.findAll()).thenReturn(List.of(publisher));
        List<FullPublisherDto> publisherDtos = publisherService.getAll();
        assertThat(publisherDtos.get(0).getDescription()).isEqualTo(publisher.getDescription());
        assertThat(publisherDtos.get(0).getTitle()).isEqualTo(publisher.getTitle());
    }

    @DisplayName("JUnit test for search() method")
    @Test
    void SearchPublisher_positiveTest(){
        when(publisherRepository.searchByTitle("Time")).thenReturn(List.of(publisher));
        List<FullPublisherDto> publisherDtos = publisherService.search("Time");
        assertThat(publisherDtos.get(0).getDescription()).isEqualTo(publisher.getDescription());
        assertThat(publisherDtos.get(0).getTitle()).isEqualTo(publisher.getTitle());
    }

    @DisplayName("JUnit test for isActive() method")
    @Test
    void IsActivePublisher_positiveTest(){
        when(publisherRepository.findByTitle("Time")).thenReturn(publisher);
        assertThat(publisherService.isActive("Time")).isTrue();
    }

    @DisplayName("JUnit test for updatePublisher() method")
    @Test
    void UpdatePublisher_positiveTest(){
        when(publisherRepository.findByTitle("Time")).thenReturn(publisher);
        when(publisherRepository.findById(publisher.getId())).thenReturn(Optional.of(publisher));
        when(publisherRepository.save(publisher)).thenReturn(publisher);

        UpdatePublisherDto editPublisher = mapper.map(publisher, UpdatePublisherDto.class);
        editPublisher.setTopic("OTHER");
        editPublisher.setPrice("99.99");
        UpdatePublisherDto updatedPublisher = publisherService.updatePublisher(editPublisher, publisher.getTitle());

        assertThat(updatedPublisher.getPrice()).isEqualTo("99.99");
        assertThat(updatedPublisher.getTopic()).isEqualTo("OTHER");
    }

    @DisplayName("JUnit test for getAllByPages() method")
    @Test
    void GetAllPublishersByPages_positiveTest(){
        Pageable pagesWithThreeElements = PageRequest.of(0, 3);
        List<Publisher> publishers = List.of(publisher, new Publisher(), new Publisher());
        Page<Publisher> pagesPublishers = new PageImpl<>(publishers, pagesWithThreeElements, publishers.size());

        when(publisherRepository.findAll(pagesWithThreeElements)).thenReturn(pagesPublishers);

        List<FullPublisherDto> fullPublisherDtoList = publisherService.getAllByPages("0");
        assertThat(fullPublisherDtoList.get(0).getTitle()).isEqualTo(publisher.getTitle());
        assertThat(fullPublisherDtoList.get(0).getPrice()).isEqualTo(String.valueOf(publisher.getPrice()));
        assertThat(fullPublisherDtoList.get(1).getTitle()).isNull();
    }

    @DisplayName("JUnit test for sortingBy() method")
    @Test
    void SortingPublishersByParam_positiveTest(){
        Publisher science = new Publisher();
        science.setTitle("Science");
        science.setPrice(33.33);
        Publisher fashion = new Publisher();
        fashion.setTitle("Fashion");
        fashion.setPrice(44.11);
        Publisher nature = new Publisher();
        nature.setTitle("Nature");
        nature.setPrice(22.22);
        //sorting by title
        Pageable pagesWithThreeElementsSortingByTitle = PageRequest.of(0, 3, Sort.by("title"));
        List<Publisher> publishersByTitle = Stream.of(publisher, science, fashion, nature)
                .sorted(Comparator.comparing(Publisher::getTitle))
                .collect(Collectors.toList());
        Page<Publisher> pagesPublishersByTitle = new PageImpl<>(publishersByTitle, pagesWithThreeElementsSortingByTitle, publishersByTitle.size());
        //sorting by price
        Pageable pagesWithThreeElementsSortingByPrice = PageRequest.of(0, 3, Sort.by("price"));
        List<Publisher> publishersByPrice = Stream.of(publisher, science, fashion, nature)
                .sorted(Comparator.comparing(Publisher::getPrice))
                .collect(Collectors.toList());
        Page<Publisher> pagesPublishersByPrice = new PageImpl<>(publishersByPrice, pagesWithThreeElementsSortingByPrice, publishersByPrice.size());

        when(publisherRepository.findAll(pagesWithThreeElementsSortingByTitle)).thenReturn(pagesPublishersByTitle);
        lenient().when(publisherRepository.findAll(pagesWithThreeElementsSortingByPrice)).thenReturn(pagesPublishersByPrice);

        List<FullPublisherDto> sortingByTitle = publisherService.sortingBy("title", "0");
        assertThat(sortingByTitle.get(0).getTitle()).isEqualTo("Fashion");
        assertThat(sortingByTitle.get(0).getPrice()).isEqualTo("44.11");
        assertThat(sortingByTitle.get(1).getTitle()).isEqualTo("Nature");

        List<FullPublisherDto> sortingByPrice = publisherService.sortingBy("price", "0");
        assertThat(sortingByPrice.get(0).getPrice()).isEqualTo("22.22");
        assertThat(sortingByPrice.get(1).getPrice()).isEqualTo("33.33");
    }

    @DisplayName("JUnit test for getByTopic() method")
    @Test
    void GettingPublishersByTopic_positiveTest(){
        Publisher forbes = new Publisher();
//        forbes.setTitle("Forbes");
        forbes.setTopic(Topics.ECONOMY);
        Publisher theNewYorkTime = new Publisher();
//        theNewYorkTime.setTitle("The New York Time");
        theNewYorkTime.setTopic(Topics.NEWS);
        //news
        Pageable pagesWithThreeElementsNewsTopic = PageRequest.of(0, 3);
        List<Publisher> newsPublishers = Stream.of(publisher, forbes, theNewYorkTime)
                .filter(e->e.getTopic().toString().equals("NEWS"))
                .collect(Collectors.toList());
        //economy
        Pageable pagesWithThreeElementsEconomyTopic = PageRequest.of(0, 3);
        List<Publisher> economyPublishers = Stream.of(publisher, forbes, theNewYorkTime)
                .filter(e->e.getTopic().toString().equals("ECONOMY"))
                .collect(Collectors.toList());

        when(publisherRepository.findByTopic(pagesWithThreeElementsNewsTopic, Topics.NEWS)).thenReturn(newsPublishers);
        when(publisherRepository.findByTopic(pagesWithThreeElementsEconomyTopic, Topics.ECONOMY)).thenReturn(economyPublishers);

        List<FullPublisherDto> newsTopic = publisherService.getByTopic("NEWS", "0");
        List<FullPublisherDto> economyTopic = publisherService.getByTopic("ECONOMY", "0");
        assertThat(newsTopic.size()).isEqualTo(2);
        assertThat(economyTopic.size()).isEqualTo(1);
    }

    @DisplayName("JUnit test for deactivatePublisher() method")
    @Test
    void DeactivatePublisher_positiveTest(){
        when(publisherRepository.findById(publisher.getId())).thenReturn(Optional.of(publisher));
        when(publisherRepository.save(publisher)).thenReturn(publisher);
        when(publisherRepository.findByTitle(publisher.getTitle())).thenReturn(publisher);

        FullPublisherDto publisherDto = publisherService.deactivatePublisher(publisher.getTitle());
        assertThat(publisherDto.getIsActive()).isEqualTo("false");
    }

    @DisplayName("JUnit test for subscribe() method (negative scenario)")
    @Test
    void subscribe_negativeTest(){
        when(userRepository.findByEmail(user.getEmail())).thenReturn(user);
        when(publisherRepository.findByTitle(publisher.getTitle())).thenReturn(publisher);
        when(subscriptionRepository.findByPublisherIdAndUserId(publisher.getId(), user.getId())).thenReturn(List.of(new Subscriptions()));

        SubscribeDto subscribeDto = new SubscribeDto();
        subscribeDto.setSubscriptionPeriod("12");
        subscribeDto.setAddress("Lviv, Sadova st. 35");
        Assertions.assertThrows(UserIsAlreadySubscribedException.class, () -> {
            publisherService.subscribe(user.getEmail(), publisher.getTitle(), subscribeDto);
        });
    }

    @DisplayName("JUnit test for subscribe() method (positive scenario)")
    @Test
    void subscribe_positiveTest() {
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        when(userRepository.save(user)).thenReturn(user);
        when(userRepository.findByEmail(user.getEmail())).thenReturn(user);
        when(publisherRepository.findByTitle(publisher.getTitle())).thenReturn(publisher);
        when(subscriptionRepository.findByPublisherIdAndUserId(publisher.getId(), user.getId())).thenReturn(List.of());

        SubscribeDto subscribeDto = new SubscribeDto();
        subscribeDto.setSubscriptionPeriod("12");
        subscribeDto.setAddress("Lviv, Sadova st. 35");
        publisherService.subscribe(user.getEmail(), publisher.getTitle(), subscribeDto);
        assertThat(user.getBalance()).isEqualTo(105.00);
    }

}