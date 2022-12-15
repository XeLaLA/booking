package org.example.booking.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.booking.dto.BookingDtoRq;
import org.example.booking.dto.BookingDtoRs;
import org.example.booking.service.BookingService;
import org.example.booking.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Slf4j
public class BookingController {
    private final CustomerService customerService;
    private final BookingService bookingService;

    @GetMapping("/booking")
    public ResponseEntity getBookingsByCustomer(@RequestParam(required = false) String customerEmail,
                                                @RequestParam(required = false) String number) {
        if (customerEmail != null) {
            return getBookingsByEmail(customerEmail);
        } else {
            return getBookingsByNumber(number);
        }
    }

    @PostMapping("/booking")
    public ResponseEntity createBooking(@RequestBody BookingDtoRq bookingDtoRq) {
        try {
            String bookingNumber = bookingService.createBooking(bookingDtoRq);
            return new ResponseEntity(bookingNumber, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/booking/{number}")
    public ResponseEntity deleteBooking(@PathVariable String number) {
        try {
            Boolean result = bookingService.deleteByNumber(number);
            if (!result) {
                throw new RuntimeException("Такого бронирования - нет");
            }
            return new ResponseEntity("Успешно удалена бронь", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    private ResponseEntity getBookingsByNumber(String number) {
        try {
            Optional<BookingDtoRs> bookingByNumber = bookingService.getBookingByNumber(number);
            BookingDtoRs bookingDtoRs = bookingByNumber.orElseThrow();
            return new ResponseEntity<>(bookingDtoRs, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    private ResponseEntity getBookingsByEmail(String customerEmail) {
        try {
            List<BookingDtoRs> bookingsByEmail = customerService.getBookingsByEmail(customerEmail);
            return new ResponseEntity<>(bookingsByEmail, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}
