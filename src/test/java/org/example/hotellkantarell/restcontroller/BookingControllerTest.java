package org.example.hotellkantarell.controller;

import org.example.hotellkantarell.model.Booking;
import org.example.hotellkantarell.restcontroller.BookingController;
import org.example.hotellkantarell.service.BookingService;
import org.example.hotellkantarell.repository.BookingRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class BookingControllerTest {

    private BookingRepository bookingRepository;
    private BookingService bookingService;
    private BookingController bookingController;

    @BeforeEach
    void setUp() {
        bookingRepository = mock(BookingRepository.class);
        bookingService = mock(BookingService.class);
        bookingController = new BookingController(bookingRepository, bookingService);
    }

    @Test
    void getAllBookings() {
        Booking booking = new Booking();
        booking.setId(1L);
        booking.setStartDate(new Date());
        booking.setEndDate(new Date());

        when(bookingRepository.findAll()).thenReturn(List.of(booking));

        List<Booking> result = bookingController.getAllBookings();
        assertEquals(1, result.size());
        assertEquals(1L, result.get(0).getId());
    }

    @Test
    void addBooking_returnsSuccess() {
        Booking booking = new Booking();
        booking.setStartDate(new Date());
        booking.setEndDate(new Date());

        when(bookingService.createBooking(any())).thenReturn(true);

        String response = bookingController.addBooking(booking);
        assertEquals("Success", response);
    }

    @Test
    void addBooking_returnsFail() {
        Booking booking = new Booking();

        when(bookingService.createBooking(any())).thenReturn(false);

        String response = bookingController.addBooking(booking);
        assertEquals("Fail", response);
    }

    @Test
    void editBooking_success() {
        Booking booking = new Booking();

        when(bookingService.updateBooking(eq(1L), any())).thenReturn(true);

        String response = bookingController.editBooking(booking, 1L);
        assertEquals("Success", response);
    }

    @Test
    void editBooking_fail() {
        Booking booking = new Booking();

        when(bookingService.updateBooking(eq(1L), any())).thenReturn(false);

        String response = bookingController.editBooking(booking, 1L);
        assertEquals("Fail", response);
    }

    @Test
    void deleteBooking_success() {
        when(bookingService.deleteBooking(1L)).thenReturn(true);

        String response = bookingController.deleteBooking(1L);
        assertEquals("Success", response);
    }

    @Test
    void deleteBooking_fail() {
        when(bookingService.deleteBooking(1L)).thenReturn(false);

        String response = bookingController.deleteBooking(1L);
        assertEquals("Fail", response);
    }

}