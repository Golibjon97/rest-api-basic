package com.epam.esm.mapper.service.gift_certificate;

import com.epam.esm.DTO.GiftCertificateDto;
import com.epam.esm.DTO.response.BaseResponseDto;
import com.epam.esm.exception.NoDataFoundException;
import com.epam.esm.exception.gift_certificate.InvalidCertificateException;
import com.epam.esm.DAO.gift_certificate.GiftCertificateDAO;
import com.epam.esm.DAO.tag.TagDAO;
import com.epam.esm.entity.gift_certificate.GiftCertificate;
import lombok.AllArgsConstructor;
import org.modelmapper.Conditions;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class GiftCertificateServiceImpl implements GiftCertificateService {

    private final GiftCertificateDAO giftCertificateDAO;
    private final TagDAO tagDAO;
    private final ModelMapper modelMapper;

    @Override
    @Transactional
    public BaseResponseDto<GiftCertificateDto> create(GiftCertificateDto giftCertificateDto) {
        isNotValid(giftCertificateDto);

        giftCertificateDto.setId(UUID.randomUUID());
        giftCertificateDto.setCreateDate(LocalDateTime.now());
        giftCertificateDto.setLastUpdateDate(LocalDateTime.now());

        GiftCertificate create = giftCertificateDAO.create(modelMapper.map(giftCertificateDto, GiftCertificate.class));
        if (create != null) {
            createTags(giftCertificateDto);
            return new BaseResponseDto<>(200, "certificate created", giftCertificateDto);
        }
        return new BaseResponseDto<>(400, "failed to create gift certificate");
    }

    @Override
    public BaseResponseDto<GiftCertificateDto> get(UUID certificateId) {
        GiftCertificate giftCertificate = giftCertificateDAO.get(certificateId);
        GiftCertificateDto certificateDto = modelMapper.map(giftCertificate, GiftCertificateDto.class);
        certificateDto.setTags(tagDAO.getTags(certificateId));
        return new BaseResponseDto<>(1, "gift certificate", certificateDto);
    }

    @Override
    public BaseResponseDto<List<GiftCertificateDto>> getAll(
            String searchWord, String tagName, boolean doNameSort, boolean doDateSort, boolean isDescending
    ) {
        List<GiftCertificate> certificateList = giftCertificateDAO.searchAndGetByTagName(
                searchWord, tagName, doNameSort, doDateSort, isDescending
        );
        if (certificateList.size() == 0)
            return new BaseResponseDto<>(204, "no certificates found");
        List<GiftCertificateDto> giftCertificateDtos = convertToDto(certificateList);
        return new BaseResponseDto<>(200, "success", addTags(giftCertificateDtos));
    }

    @Override
    public BaseResponseDto delete(UUID certificateId) {
        int delete = giftCertificateDAO.delete(certificateId);
        if (delete == 1) {
            return new BaseResponseDto(200, "certificate deleted");
        }
        throw new NoDataFoundException("no certificate found with id: " + certificateId);
    }

    @Override
    public BaseResponseDto update(GiftCertificateDto update, UUID certificateId) {
        if (!isUpdate(update))
            return new BaseResponseDto(204, "nothing to update");
        isNotValid(update);

        GiftCertificate old = giftCertificateDAO.get(certificateId);
        update.setLastUpdateDate(LocalDateTime.now());
        modelMapper.getConfiguration().setPropertyCondition(Conditions.isNotNull());
        modelMapper.map(update, old);
        GiftCertificate result = giftCertificateDAO.update(old, certificateId);
        if (result != null) {
            update.setId(certificateId);
            createTags(update);
            modelMapper.map(result, update);
            update.setTags(tagDAO.getTags(update.getId()));
            return new BaseResponseDto(200, "certificate updated", update);
        }
        return new BaseResponseDto(0, "cannot update certificate");
    }

    List<GiftCertificateDto> convertToDto(List<GiftCertificate> certificates) {
        return certificates.stream()
                .map((certificate) ->
                        modelMapper.map(certificate, GiftCertificateDto.class))
                .collect(Collectors.toList());
    }

    List<GiftCertificateDto> addTags(List<GiftCertificateDto> certificateDtos) {
        return certificateDtos.stream().peek(certificate -> certificate.setTags(tagDAO.getTags(certificate.getId())))
                .collect(Collectors.toList());
    }

    void createTags(GiftCertificateDto giftCertificateDto) {
        if (giftCertificateDto.getTags() != null)
            tagDAO.createWithGiftCertificate(giftCertificateDto.getId(), giftCertificateDto.getTags());
    }

    boolean isUpdate(GiftCertificateDto update) {
        return update.getName() != null ||
                update.getDescription() != null ||
                update.getPrice() != null ||
                update.getDuration() != null ||
                update.getTags() != null;
    }

    void isNotValid(GiftCertificateDto giftCertificate){
        if((giftCertificate.getDuration() != null && giftCertificate.getDuration() <= 0) ||
           (giftCertificate.getPrice() != null && giftCertificate.getPrice().compareTo(new BigDecimal(0)) == - 1))
            throw  new InvalidCertificateException(
                    "duration should be greater than 0 and price should not be less than 0"
            );
    }
}
