package com.epam.fedunkiv.periodicals;

import com.epam.fedunkiv.periodicals.dto.publishers.CreatePublisherDto;
import com.epam.fedunkiv.periodicals.dto.users.CreateUserDto;
import com.epam.fedunkiv.periodicals.model.Subscriptions;
import com.epam.fedunkiv.periodicals.repositories.SubscriptionRepository;
import com.epam.fedunkiv.periodicals.services.PublisherService;
import com.epam.fedunkiv.periodicals.services.UserService;
import lombok.extern.log4j.Log4j2;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.List;


@SpringBootApplication
@Log4j2
public class PeriodicalsApplication {

    public static void main(String[] args) {
        SpringApplication.run(PeriodicalsApplication.class, args);
    }

    @Bean()
    CommandLineRunner init(UserService userService, PublisherService publisherService, SubscriptionRepository subscriptionRepository) {
        return args -> {
            CreateUserDto createUserDto = new CreateUserDto();
            createUserDto.setRole("ADMIN");
            createUserDto.setFullName("Admin Adminovych");
            createUserDto.setEmail("admin@gmail.com");
            createUserDto.setPassword("123456Q@q");
            userService.addUser(createUserDto);

            CreatePublisherDto theEconomist =
                    new CreatePublisherDto("The Economist", "ECONOMY", "46.55",
                            "The Economist is a British weekly newspaper printed in demitab format and published digitally that focuses on current affairs, international business, politics, technology, and culture. Based in London, the newspaper is owned by The Economist Group, with core editorial offices in the United States, as well as across major cities in continental Europe, Asia, and the Middle East"
                    );
            publisherService.createPublisher(theEconomist);

            CreatePublisherDto time =
                    new CreatePublisherDto("Time", "NEWS", "78.99",
                            "Time is an American news magazine and news website published and based in New York City. "
                    );
            publisherService.createPublisher(time);

            CreatePublisherDto theNYT =
                    new CreatePublisherDto("The New York times", "NEWS", "90.02",
                            "The New York Times is an American daily newspaper based in New York City with a worldwide readership."
                    );
            publisherService.createPublisher(theNYT);

            CreatePublisherDto nationalGeo =
                    new CreatePublisherDto("National Geographic", "NATURE", "65.99",
                            "National Geographic (formerly the National Geographic Magazine, sometimes branded as NAT GEO) is a popular American monthly magazine published by the National Geographic Society. "
                    );
            publisherService.createPublisher(nationalGeo);


            CreatePublisherDto science =
                    new CreatePublisherDto("Science", "SCIENCE", "78.65",
                            "Science, also widely referred to as Science Magazine, is the peer-reviewed academic journal of the American Association for the Advancement of Science (AAAS) and one of the world's top academic journals.");
            publisherService.createPublisher(science);

            CreatePublisherDto adMaterials =
                    new CreatePublisherDto("Advanced Materials", "SCIENCE", "45.66",
                            "Advanced Materials is a weekly peer-reviewed scientific journal covering materials science. It includes communications, reviews, and feature articles on topics in chemistry, physics, nanotechnology, ceramics, metallurgy, and biomaterials.");
            publisherService.createPublisher(adMaterials);

            CreatePublisherDto forbes =
                    new CreatePublisherDto("Forbes", "ECONOMY", "45.66",
                            "Forbes is an American business magazine owned by Integrated Whale Media Investments and the Forbes family. Published eight times a year, it features articles on finance, industry, investing, and marketing topics. ");
            publisherService.createPublisher(forbes);

            CreatePublisherDto fashion =
                    new CreatePublisherDto("Fashion", "FASHION", "75.45",
                            "Fashion is a Canadian fashion magazine published by St. Joseph Communications. Established in 1977, it is currently based in Toronto (with satellite offices in Vancouver, Calgary and Montreal), publishes 10 issues a year and has a total readership of 1.85 million (PMB Oct 2010).");
            publisherService.createPublisher(fashion);

            CreatePublisherDto pump =
                    new CreatePublisherDto("Pump", "FASHION", "63.66",
                            "PUMP Magazine was created by artists for artists. Established in December of 2013, PUMP is an esteemed international fashion and beauty lifestyle publication circulating through countless major fashion capitals and communities around the world.");
            publisherService.createPublisher(pump);

            CreatePublisherDto vogue =
                    new CreatePublisherDto("Vogue", "FASHION", "85.66",
                            "Vogue is an American monthly fashion and lifestyle magazine that covers many topics, including haute couture fashion, beauty, culture, living, and runway. Based at One World Trade Center in the Financial District of Lower Manhattan, Vogue began as a weekly newspaper in 1892 before becoming a monthly magazine years later.");
            publisherService.createPublisher(vogue);

            CreatePublisherDto nature =
                    new CreatePublisherDto("Nature", "NATURE", "88.88",
                            "Nature is a British weekly scientific journal founded and based in London, England. As a multidisciplinary publication, Nature features peer-reviewed research from a variety of academic disciplines, mainly in science and technology. It has core editorial offices across the United States, continental Europe, and Asia under the international scientific publishing company Springer Nature.");
            publisherService.createPublisher(nature);

            Subscriptions subscription = new Subscriptions();
            subscription.setSubscriptionPeriod(12);
            subscription.setAddress("Lviv");
            subscription.setUserId(1l);
            subscription.setPublisherId(1l);

            subscriptionRepository.save(subscription);
            List<Subscriptions> subscriptions = subscriptionRepository.findByPublisherIdAndUserId(1l, 1l);
            log.warn("subscriptions ==> "+subscriptions);
        };
    }
}