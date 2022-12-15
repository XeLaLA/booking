package org.example.booking;

import io.restassured.RestAssured;
import io.restassured.common.mapper.TypeRef;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.example.booking.dto.BookingDtoRq;
import org.example.booking.dto.BookingDtoRs;
import org.example.booking.dto.CustomerDtoRq;
import org.example.booking.service.BookingService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.boot.test.web.server.LocalServerPort;
import java.time.LocalDate;
import java.util.List;
import static io.restassured.RestAssured.given;
import static org.mockito.Mockito.doReturn;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class BookingControllerTest {
    @SpyBean
    private BookingService mockBookingService;
    @LocalServerPort
    private int port;

    @BeforeEach
    public void init() {
        RestAssured.baseURI = "http://localhost:" + port;
    }

    @Test
    public void getBookingNumberFailTest() {
        Response response = given()
                .contentType(ContentType.JSON)
                .when()
                .get("/booking?number=112")
                .then()
                .extract()
                .response();

        Assertions.assertEquals(400, response.statusCode());
    }

    @Test
    public void getBookingNumberGoodTest() {
        Response response = given()
                .contentType(ContentType.JSON)
                .when()
                .get("/booking?number=111")
                .then()
                .extract()
                .response();

        BookingDtoRs actual = response.body().as(BookingDtoRs.class);
        BookingDtoRs expected = BookingDtoRs.builder()
                .number("111")
                .roomName("111")
                .startDate(LocalDate.of(2000, 1, 1))
                .endDate(LocalDate.of(2000, 1, 2))
                .customerName("Petr")
                .build();

        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void getBookingEmailGoodTest() {
        Response response = given()
                .contentType(ContentType.JSON)
                .when()
                .get("/booking?customerEmail=222@gmail.com")
                .then()
                .extract()
                .response();

        List<BookingDtoRs> actual = response.body().as(new TypeRef<List<BookingDtoRs>>() {});

        BookingDtoRs expected = BookingDtoRs.builder()
                .number("111")
                .roomName("111")
                .startDate(LocalDate.of(2000, 1, 1))
                .endDate(LocalDate.of(2000, 1, 2))
                .customerName("Petr")
                .build();

        Assertions.assertEquals(expected, actual.get(0));
    }

    @Test
    public void getBookingEmailFailTest() {
        Response response = given()
                .contentType(ContentType.JSON)
                .when()
                .get("/booking?customerEmail=2232@gmail.com")
                .then()
                .extract()
                .response();

        Assertions.assertEquals(400, response.statusCode());
    }

    @Test
    public void createBookingGoodTest()
    {
    String InputBody ="{\n" +
            "  \"roomName\": \"111\",\n" +
            "  \"startDate\":\"2022-12-12\",\n" +
            "  \"endDate\":\"2022-12-14\",\n" +
            "  \"customer\":  {\n" +
            "      \"name\": \"Vlad\",\n" +
            "       \"email\": \"1@mail.ru\"\n" +
            "    }\n" +
            "}";

        CustomerDtoRq customerDtoRq = CustomerDtoRq
                .builder()
                .name("Vlad")
                .email("1@mail.ru")
                .build();

        BookingDtoRq bookingDtoRq = BookingDtoRq
                .builder()
                .roomName("111")
                .startDate(LocalDate.of(2022, 12, 12))
                .endDate(LocalDate.of(2022, 12, 14))
                .customer(customerDtoRq)
                .build();

        doReturn("222").when(mockBookingService).createBooking(bookingDtoRq);

        Response response = given()
                .contentType(ContentType.JSON)
                .body(InputBody)
                .when()
                .post("/booking")
                .then()
                .extract()
                .response();

        String actual = response.body().asString();
        Assertions.assertEquals("222", actual);
    }

    @Test
    public void deleteBookingGoodTest() {

        Response response = given()
                .contentType(ContentType.JSON)
                .when()
                .delete("/booking/333")
                .then()
                .extract()
                .response();

        Assertions.assertEquals(200, response.statusCode());
    }
    @Test
    public void deleteBookingFailTest() {
        Response response = given()
                .contentType(ContentType.JSON)
                .when()
                .delete("/booking/555")
                .then()
                .extract()
                .response();

        Assertions.assertEquals(400, response.statusCode());
    }
}
