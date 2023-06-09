package com.muravev.samokatimmonolit.service.impl;

import com.muravev.samokatimmonolit.entity.user.EmployeeEntity;
import com.muravev.samokatimmonolit.entity.OrganizationEntity;
import com.muravev.samokatimmonolit.error.ApiException;
import com.muravev.samokatimmonolit.error.StatusCode;
import com.muravev.samokatimmonolit.integration.dadata.service.DadataOrganizationService;
import com.muravev.samokatimmonolit.repo.OrganizationRepo;
import com.muravev.samokatimmonolit.service.OrganizationReader;
import com.muravev.samokatimmonolit.service.SecurityService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrganizationReaderImpl implements OrganizationReader {
    private final OrganizationRepo organizationRepo;
    private final DadataOrganizationService dadataOrganizationService;
    private final SecurityService securityService;

    @Override
    @Transactional(readOnly = true)
    public OrganizationEntity getByInn(String inn) {
        if (StringUtils.isNumeric(inn) && (inn.length() == 10 || inn.length() == 12))
            return organizationRepo.findByInn(inn)
                .or(() -> dadataOrganizationService.getOneByInn(inn))
                .orElseThrow(() -> new ApiException(StatusCode.ORGANIZATION_NOT_FOUND));

        throw new ApiException(StatusCode.ORGANIZATION_INVALID_INN);
    }

    @Override
    @Transactional(readOnly = true)
    public OrganizationEntity getMyOrg() {
        EmployeeEntity employee = securityService.getCurrentEmployee();
        return employee.getOrganization();
    }

    @Override
    @Transactional(readOnly = true)
    public double getMyRevenue() {
        EmployeeEntity employee = securityService.getCurrentEmployee();
        return organizationRepo.getRevenue(employee.getOrganization())
                .orElse(0.);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<OrganizationEntity> getAll(String keyword, Pageable pageable) {
        return organizationRepo.findAllByKeyword(keyword, pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public OrganizationEntity getOne(long id) {
        return organizationRepo.findById(id)
                .orElseThrow(() -> new ApiException(StatusCode.ORGANIZATION_NOT_FOUND));
    }
}
