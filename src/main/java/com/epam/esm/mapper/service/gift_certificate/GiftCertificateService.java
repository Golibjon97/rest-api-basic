package com.epam.esm.mapper.service.gift_certificate;

import com.epam.esm.DTO.GiftCertificateDto;
import com.epam.esm.DTO.response.BaseResponseDto;
import com.epam.esm.mapper.service.BaseService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public interface GiftCertificateService extends BaseService<GiftCertificateDto> {

    BaseResponseDto<List<GiftCertificateDto>> getAll(
            String searchWord, String tagName, boolean doNameSort, boolean doDateSort, boolean isDescending
    );

    BaseResponseDto update(GiftCertificateDto update, UUID certificateId);

//    BaseResponseDto<List<GiftCertificateDto>> getByTagName(String tagName);
//
//    BaseResponseDto<List<GiftCertificateDto>> searchByDescriptionOrName(String keyWord);
//
//    BaseResponseDto<List<GiftCertificateDto>> sortByName();
//
//    BaseResponseDto<List<GiftCertificateDto>> sortByDate();
}
