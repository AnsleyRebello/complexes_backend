package com.ansley.demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ansley.demo.model.Appointment;
import com.ansley.demo.service.AppointmentService;

@RestController
@RequestMapping("/api/appointments")
public class AppointmentController {
    @Autowired private AppointmentService appointmentService;
    
    @GetMapping
    public List<Appointment> getAllAppointments()
    {
    	return appointmentService.getAllAppointments();
    }

    @PostMapping("/book")
    public Appointment bookAppointment(@RequestBody Appointment appointment) {
        return appointmentService.bookAppointment(appointment);
    }

    @GetMapping("/verify")
    public String verifyAppointment(@RequestParam Long appointmentId) {
        appointmentService.verifyAppointment(appointmentId);
        return "Appointment verified!";
    }
}
