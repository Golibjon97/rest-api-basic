package com.epam.esm.service.gift_certificate;

import com.epam.esm.dto.GiftCertificateDto;
import com.epam.esm.enums.SortEnum;
import com.epam.esm.service.BaseService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public interface GiftCertificateService extends BaseService<GiftCertificateDto> {

    List<GiftCertificateDto> getAll(
            String searchWord, String tagName, SortEnum sort
    );

    GiftCertificateDto update(GiftCertificateDto update, UUID certificateId);
}
