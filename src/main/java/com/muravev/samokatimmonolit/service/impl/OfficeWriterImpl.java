package com.muravev.samokatimmonolit.service.impl;

import com.muravev.samokatimmonolit.entity.*;
import com.muravev.samokatimmonolit.entity.user.EmployeeEntity;
import com.muravev.samokatimmonolit.error.ApiException;
import com.muravev.samokatimmonolit.error.StatusCode;
import com.muravev.samokatimmonolit.integration.dadata.service.DadataAddressService;
import com.muravev.samokatimmonolit.model.Address;
import com.muravev.samokatimmonolit.model.in.GeoPointIn;
import com.muravev.samokatimmonolit.model.in.command.office.OfficeCreateCommand;
import com.muravev.samokatimmonolit.model.in.command.office.OfficeScheduleDayModifyCommand;
import com.muravev.samokatimmonolit.repo.InventoryRepo;
import com.muravev.samokatimmonolit.repo.OfficeRepo;
import com.muravev.samokatimmonolit.service.OfficeWriter;
import com.muravev.samokatimmonolit.service.SecurityService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class OfficeWriterImpl implements OfficeWriter {
    private final OfficeRepo officeRepo;
    private final InventoryRepo inventoryRepo;

    private final DadataAddressService addressService;
    private final SecurityService securityService;


    @Override
    @Transactional
    public OfficeEntity create(@RequestBody @Valid OfficeCreateCommand command) {
        EmployeeEntity employee = securityService.getCurrentEmployee();
        OrganizationEntity organization = employee.getOrganization();

        List<OfficeScheduleEmbeddable> schedules = command.schedules().stream()
                .map(dto -> new OfficeScheduleEmbeddable()
                        .setDay(dto.day())
                        .setStart(dto.start())
                        .setEnd(dto.end())
                        .setDayOff(dto.dayOff()))
                .toList();

        GeoPointIn location = command.location();
        List<Address> addresses = addressService.getFirstByGeoPoint(location.lat(), location.lng());

        Address nearest = addresses.stream()
                .findFirst()
                .orElseGet(() -> new Address(null, null, null, "UTC+3"));

        OfficeEntity office = new OfficeEntity()
                .setLat(command.location().lat())
                .setLng(command.location().lng())
                .setAlias(command.alias())
                .setCapacity(command.capacity())
                .setOrganization(organization)
                .setAddress(nearest.value())
                .setSchedules(schedules);

        return officeRepo.save(office);
    }

    @Override
    @Transactional
    public OfficeEntity modifyScheduleDay(long officeId, OfficeScheduleDayModifyCommand command) {
        OfficeEntity office = officeRepo.findById(officeId)
                .orElseThrow(() -> new ApiException(StatusCode.OFFICE_NOT_FOUND));

        List<OfficeScheduleEmbeddable> newSchedule = command.days().stream()
                .map(day -> new OfficeScheduleEmbeddable()
                        .setDay(day.day())
                        .setStart(day.start())
                        .setEnd(day.end())
                        .setDayOff(day.dayOff()))
                .collect(Collectors.toList());
        office.setSchedules(newSchedule);
        return office;
    }

    @Override
    @Transactional
    public OfficeEntity close(long officeId) {
        OfficeEntity office = officeRepo.findById(officeId)
                .filter(Predicate.not(OfficeEntity::isClosed))
                .orElseThrow(() -> new ApiException(StatusCode.OFFICE_NOT_FOUND));

        office.setDeletedAt(ZonedDateTime.now());
        return office;
    }

}
