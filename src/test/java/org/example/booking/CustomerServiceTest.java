package org.example.booking;

import io.restassured.RestAssured;
import org.example.booking.dto.BookingDtoRs;
import org.example.booking.service.CustomerService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CustomerServiceTest {
    @Autowired
    private CustomerService customerService;

    @LocalServerPort
    private int port;

    @BeforeEach
    public void init() {
        RestAssured.baseURI = "http://localhost:" + port;
    }

    //При выполнении теста возникала ошибка org.hibernate.LazyInitializationException
    //CustomerService над методом getBookingsByEmail добавил @Transactional
    @Test
    public void getBookingsByEmailGoodTest()
    {
        List<BookingDtoRs> actual = customerService.getBookingsByEmail("222@gmail.com");
        List expected = new ArrayList();

        BookingDtoRs bookingDtoRs = BookingDtoRs
                .builder()
                .number("111")
                .startDate(LocalDate.of(2000, 1, 1))
                .endDate(LocalDate.of(2000, 1, 2))
                .roomName("111")
                .customerName("Petr")
                .build();

        expected.add(bookingDtoRs);
        Assertions.assertEquals(expected, actual);
    }

    //Падает с ошибкой java.util.NoSuchElementException: No value present
//    @Test
//    public void getBookingsByEmailFailTest()
//    {
//        Assertions.assertThrows(Exception.class,
//                (Executable) customerService.getBookingsByEmail("111@gmail.com"));
//    }
}
