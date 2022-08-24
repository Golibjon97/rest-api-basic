package com.epam.esm.service;

import com.epam.esm.dao.gift_certificate.GiftCertificateDAO;
import com.epam.esm.dao.tag.TagDAO;
import com.epam.esm.dto.GiftCertificateDto;
import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.service.gift_certificate.GiftCertificateServiceImpl;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.modelmapper.internal.InheritingConfiguration;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;

@ExtendWith(MockitoExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class GiftCertificateServiceTest {

    @Mock
    private GiftCertificateDAO giftCertificateDAO;
    @Mock
    private ModelMapper modelMapper;
    @Mock
    private TagDAO tagDAO;

    @InjectMocks
    private GiftCertificateServiceImpl giftCertificateService;

    private GiftCertificate giftCertificate;
    private GiftCertificate giftCertificateFalse;
    private GiftCertificateDto giftCertificateDto;
    private GiftCertificateDto giftCertificateDtoFalse;

    @BeforeEach
    void setUp() {
        giftCertificate = new GiftCertificate(
                UUID.randomUUID(),
                "testCertificate",
                "this is for testing",
                new BigDecimal("12000"),
                5,
                LocalDateTime.now(),
                LocalDateTime.now()
        );

        giftCertificateDto = new GiftCertificateDto(
                giftCertificate.getId(),
                giftCertificate.getName(),
                giftCertificate.getDescription(),
                giftCertificate.getPrice(),
                giftCertificate.getDuration(),
                giftCertificate.getCreateDate(),
                giftCertificate.getLastUpdateDate(),
                null
        );

        giftCertificateFalse = new GiftCertificate(
                UUID.randomUUID(),
                "testCertificate",
                "this is for testing",
                new BigDecimal("12000"),
                5,
                LocalDateTime.now(),
                LocalDateTime.now()
        );

        giftCertificateDtoFalse = new GiftCertificateDto(
                giftCertificate.getId(),
                giftCertificate.getName(),
                giftCertificate.getDescription(),
                giftCertificate.getPrice(),
                giftCertificate.getDuration(),
                giftCertificate.getCreateDate(),
                giftCertificate.getLastUpdateDate(),
                null
        );


    }

    @Test
    @Order(1)
    public void canCreateGiftCertificate(){
        given(giftCertificateDAO.create(giftCertificate)).willReturn(giftCertificate);
        given(modelMapper.map(giftCertificateDto, GiftCertificate.class))
                .willReturn(giftCertificate);
        GiftCertificateDto response
                = giftCertificateService.create(giftCertificateDto);
        assertEquals(giftCertificateDto, response);
    }

    @Test
    @Order(2)
    public void canGetGiftCertificateById(){
        given(giftCertificateDAO.get(giftCertificate.getId())).willReturn(giftCertificate);
        given(modelMapper.map(giftCertificate, GiftCertificateDto.class))
                .willReturn(giftCertificateDto);
        given(tagDAO.getTags(giftCertificate.getId())).willReturn(null);

        GiftCertificateDto responseDto = giftCertificateService.get(giftCertificate.getId());
        assertNotNull(responseDto);
    }

    @Test
    @Order(3)
    public void canUpdateGiftCertificate(){
        given(giftCertificateDAO.get(giftCertificate.getId())).willReturn(giftCertificate);
        given(giftCertificateDAO.update(giftCertificate, giftCertificate.getId())).willReturn(giftCertificate);
        given(modelMapper.getConfiguration()).willReturn(new InheritingConfiguration());
        doNothing().when(modelMapper).map(giftCertificateDto, giftCertificate);

        GiftCertificateDto update1 = giftCertificateService.update(giftCertificateDto, giftCertificateDto.getId());

        assertEquals(giftCertificateDto, update1);
    }

    @Test
    @Order(4)
    public void canNotDeleteGiftCertificate(){

        given(giftCertificateDAO.delete(giftCertificateFalse.getId())).willReturn(0);
        int notDeleted = giftCertificateService.delete(giftCertificateFalse.getId());
        assertEquals(0, notDeleted);

    }

    @Test
    @Order(5)
    public void canDeleteGiftCertificate(){
        given(giftCertificateDAO.delete(giftCertificate.getId())).willReturn(1);

        int delete = giftCertificateService.delete(giftCertificate.getId());

        assertEquals(1, delete);
    }

}