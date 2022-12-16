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
                    new CreatePublisherDto("The Economist", "ECONOMY", "46.55", "https://www.economist.com/sites/default/files/print-covers/20200725_cuk1280.jpg",
                            "The Economist is a British weekly newspaper printed in demitab format and published digitally that focuses on current affairs, international business, politics, technology, and culture. Based in London, the newspaper is owned by The Economist Group, with core editorial offices in the United States, as well as across major cities in continental Europe, Asia, and the Middle East"
                    );
            publisherService.createPublisher(theEconomist);

            CreatePublisherDto time =
                    new CreatePublisherDto("Time", "NEWS", "78.99", "https://m.media-amazon.com/images/I/51vAxfluM4S._AC_SY780_.jpg",
                            "Time is an American news magazine and news website published and based in New York City. "
                    );
            publisherService.createPublisher(time);

            CreatePublisherDto theNYT =
                    new CreatePublisherDto("The New York times", "NEWS", "90.02","https://static01.nyt.com/images/2022/02/06/magazine/06mag-cover-type/06mag-cover-type-blog480.jpg",
                            "The New York Times is an American daily newspaper based in New York City with a worldwide readership."
                    );
            publisherService.createPublisher(theNYT);

            CreatePublisherDto nationalGeo =
                    new CreatePublisherDto("National Geographic", "NATURE", "65.99","https://s.hdnux.com/photos/73/30/71/15568498/4/rawImage.jpg",
                            "National Geographic (formerly the National Geographic Magazine, sometimes branded as NAT GEO) is a popular American monthly magazine published by the National Geographic Society. "
                    );
            publisherService.createPublisher(nationalGeo);


            CreatePublisherDto science =
                    new CreatePublisherDto("Science", "SCIENCE", "78.65","https://www.science.org/cms/asset/d120bffd-92f4-49a9-8ec5-0485839da620/science.2021.373.issue-6560.largecover.jpg",
                            "Science, also widely referred to as Science Magazine, is the peer-reviewed academic journal of the American Association for the Advancement of Science (AAAS) and one of the world's top academic journals.");
            publisherService.createPublisher(science);

            CreatePublisherDto adMaterials =
                    new CreatePublisherDto("Advanced Materials", "SCIENCE", "95.66","https://onlinelibrary.wiley.com/pb-assets/assets/15214095/cover/thumbnails/2019_31_52i0020-1577814259580.jpg",
                            "Advanced Materials is a weekly peer-reviewed scientific journal covering materials science. It includes communications, reviews, and feature articles on topics in chemistry, physics, nanotechnology, ceramics, metallurgy, and biomaterials.");
            publisherService.createPublisher(adMaterials);

            CreatePublisherDto forbes =
                    new CreatePublisherDto("Forbes", "ECONOMY", "45.66","https://d1csarkz8obe9u.cloudfront.net/posterpreviews/240222-forbes-magazine-cover-template-design-f79f499f80ab399cdada1537effbf4bb_screen.jpg?ts=1645727934",
                            "Forbes is an American business magazine owned by Integrated Whale Media Investments and the Forbes family. Published eight times a year, it features articles on finance, industry, investing, and marketing topics. ");
            publisherService.createPublisher(forbes);

            CreatePublisherDto fashion =
                    new CreatePublisherDto("Fashion", "FASHION", "75.45","https://previews.123rf.com/images/belchonock/belchonock1704/belchonock170400288/105035668-attractive-young-woman-on-fashion-magazine-cover-fashionable-lifestyle-concept-.jpg",
                            "Fashion is a Canadian fashion magazine published by St. Joseph Communications. Established in 1977, it is currently based in Toronto (with satellite offices in Vancouver, Calgary and Montreal), publishes 10 issues a year and has a total readership of 1.85 million (PMB Oct 2010).");
            publisherService.createPublisher(fashion);

            CreatePublisherDto pump =
                    new CreatePublisherDto("Pump", "FASHION", "63.66","https://images.squarespace-cdn.com/content/v1/5709732559827e091ddcc8c7/1627592055938-HAITYDV2HXTJG9IYW6D6/PUMP+Magazine+%7C+Exclusive+Invitation+Edition+%7C+July+2021.png",
                            "PUMP Magazine was created by artists for artists. Established in December of 2013, PUMP is an esteemed international fashion and beauty lifestyle publication circulating through countless major fashion capitals and communities around the world.");
            publisherService.createPublisher(pump);

            CreatePublisherDto vogue =
                    new CreatePublisherDto("Vogue", "FASHION", "85.66","https://assets.vogue.com/photos/5cc9b8566e0d9e2eddf9ec6e/master/w_2400,h_3263,c_limit/10-zendaya-vogue-cover-june-2019.jpg",
                            "Vogue is an American monthly fashion and lifestyle magazine that covers many topics, including haute couture fashion, beauty, culture, living, and runway. Based at One World Trade Center in the Financial District of Lower Manhattan, Vogue began as a weekly newspaper in 1892 before becoming a monthly magazine years later.");
            publisherService.createPublisher(vogue);

            CreatePublisherDto nature =
                    new CreatePublisherDto("Nature", "NATURE", "88.88","https://media.springernature.com/w440/springer-static/cover-hires/journal/41586/608/7923",
                            "Nature is a British weekly scientific journal founded and based in London, England. As a multidisciplinary publication, Nature features peer-reviewed research from a variety of academic disciplines, mainly in science and technology. It has core editorial offices across the United States, continental Europe, and Asia under the international scientific publishing company Springer Nature.");
            publisherService.createPublisher(nature);

            Subscriptions subscription = new Subscriptions();
            subscription.setSubscriptionPeriod(12);
            subscription.setAddress("Lviv");
            subscription.setUserId(1l);
            subscription.setPublisherId(1l);
        };
    }
}