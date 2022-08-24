package com.epam.esm.service.gift_certificate;

import com.epam.esm.dao.gift_certificate.GiftCertificateDAO;
import com.epam.esm.dao.tag.TagDAO;
import com.epam.esm.dto.GiftCertificateDto;
import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.enums.SortEnum;
import com.epam.esm.exception.InvalidCertificateException;
import com.epam.esm.exception.NoDataFoundException;
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
    public GiftCertificateDto create(GiftCertificateDto giftCertificateDto) {
        isNotValid(giftCertificateDto);

        giftCertificateDto.setId(UUID.randomUUID());
        giftCertificateDto.setCreateDate(LocalDateTime.now());
        giftCertificateDto.setLastUpdateDate(LocalDateTime.now());

        giftCertificateDAO.create(modelMapper.map(giftCertificateDto, GiftCertificate.class));
        createTags(giftCertificateDto);
        return giftCertificateDto;

    }
    @Override
    public GiftCertificateDto get(UUID certificateId) {  // returns  GiftCertificateDto (with list<tags>)
        GiftCertificate giftCertificate = giftCertificateDAO.get(certificateId);
        GiftCertificateDto certificateDto =
                modelMapper.map(giftCertificate, GiftCertificateDto.class);
        certificateDto.setTags(tagDAO.getTags(certificateId));
        return certificateDto;
    }
    @Override
    public List<GiftCertificateDto> getAll(  // Gets the gift certificates by the name
            String searchWord, String tagName, SortEnum sortEnum
    ) {
        List<GiftCertificate> certificateList = giftCertificateDAO.searchAndGetByNameOrTagName(
                searchWord, tagName, sortEnum
        );
        List<GiftCertificateDto> giftCertificateDtos = convertToDto(certificateList);
        return addTags(giftCertificateDtos);
    }

    @Override
    public int delete(UUID certificateId) {
        return giftCertificateDAO.delete(certificateId);
    }

    @Override
    public GiftCertificateDto update(GiftCertificateDto update, UUID certificateId) {
        if (!isUpdate(update))
            throw new NoDataFoundException("Nothing to update");
        isNotValid(update);

        GiftCertificate old = giftCertificateDAO.get(certificateId);
        update.setLastUpdateDate(LocalDateTime.now());
        modelMapper.getConfiguration().setPropertyCondition(Conditions.isNotNull());
        modelMapper.map(update, old);       // OLD gets UPDATE
        GiftCertificate result = giftCertificateDAO.update(old, certificateId);

        update.setId(certificateId);
        createTags(update);
        modelMapper.map(result, update);  // UPDATE gets RESULT and forgets the tags brought
        update.setTags(tagDAO.getTags(update.getId())); // tags are inserted into UPDATE
        return update;
    }

    private List<GiftCertificateDto> convertToDto(List<GiftCertificate> certificates) {
        return certificates.stream()        // Converts giftCertificate into DTOs
                .map((certificate) ->
                        modelMapper.map(certificate, GiftCertificateDto.class))
                .collect(Collectors.toList());
    }

    private List<GiftCertificateDto> addTags(List<GiftCertificateDto> certificateDtos) {
        return certificateDtos.stream().peek(  // Set connected tags into giftCertificateDTO List<tags>
                certificate -> certificate.setTags(tagDAO.getTags(certificate.getId()))
                ).collect(Collectors.toList());
    }

    private void createTags(GiftCertificateDto giftCertificateDto) { // creates connection by inserting into GiftCertTag
        if (giftCertificateDto.getTags() != null)
            tagDAO.createWithGiftCertificate(giftCertificateDto.getId(), giftCertificateDto.getTags());
    }

    boolean isUpdate(GiftCertificateDto update) {    // checks if all the data exists
        return update.getName() != null || update.getDescription() != null || update.getPrice() != null || update.getDuration() != null || update.getTags() != null;
    }

    private void isNotValid(GiftCertificateDto giftCertificate){    // checks if the duration and price are not valid
        if((giftCertificate.getDuration() != null && giftCertificate.getDuration() <= 0) ||
           (giftCertificate.getPrice() != null &&
                   giftCertificate.getPrice().compareTo(new BigDecimal(0)) == - 1))
            throw new InvalidCertificateException(
                    "duration should be greater than 0 and price should not be less than 0"
            );
    }
}
