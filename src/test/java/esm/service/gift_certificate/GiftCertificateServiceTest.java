package esm.service.gift_certificate;

import com.epam.esm.DAO.gift_certificate.GiftCertificateDAO;
import com.epam.esm.DAO.tag.TagDAO;
import com.epam.esm.DTO.GiftCertificateDto;
import com.epam.esm.DTO.response.BaseResponseDto;
import com.epam.esm.entity.gift_certificate.GiftCertificate;
import com.epam.esm.mapper.service.gift_certificate.GiftCertificateServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.modelmapper.internal.InheritingConfiguration;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;

@ExtendWith(MockitoExtension.class)
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
    private GiftCertificateDto giftCertificateDto;

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

    }

    @Test
    public void canCreateGiftCertificate(){
        given(giftCertificateDAO.create(giftCertificate)).willReturn(giftCertificate);
        given(modelMapper.map(giftCertificateDto, GiftCertificate.class))
                .willReturn(giftCertificate);
        BaseResponseDto<GiftCertificateDto> response
                = giftCertificateService.create(giftCertificateDto);
        assertEquals(1, response.getStatus());
    }

    @Test
    public void canGetGiftCertificateById(){
        given(giftCertificateDAO.get(giftCertificate.getId())).willReturn(giftCertificate);
        given(modelMapper.map(giftCertificate, GiftCertificateDto.class))
                .willReturn(giftCertificateDto);
        given(tagDAO.getTags(giftCertificate.getId())).willReturn(null);

        BaseResponseDto<GiftCertificateDto> responseDto = giftCertificateService.get(giftCertificate.getId());

        assertEquals(1, responseDto.getStatus());
        assertNotNull(responseDto.getData());
    }

    @Test
    public void canDeleteGiftCertificate(){
        given(giftCertificateDAO.delete(giftCertificate.getId())).willReturn(1);

        BaseResponseDto delete = giftCertificateService.delete(giftCertificate.getId());

        assertEquals(1, delete.getStatus());
    }

    @Test
    public void canUpdateGiftCertificate(){
        given(giftCertificateDAO.get(giftCertificate.getId())).willReturn(giftCertificate);
        given(giftCertificateDAO.update(giftCertificate, giftCertificate.getId())).willReturn(giftCertificate);
        given(modelMapper.getConfiguration()).willReturn(new InheritingConfiguration());
        doNothing().when(modelMapper).map(giftCertificateDto, giftCertificate);

        BaseResponseDto update1 = giftCertificateService.update(giftCertificateDto, giftCertificateDto.getId());

        assertEquals(1, update1.getStatus());
    }

    private List<GiftCertificate> getGiftCertificateList(){
        List<GiftCertificate> giftCertificates = new ArrayList<>();
        GiftCertificate certificate1 = new GiftCertificate();
        certificate1.setId(UUID.randomUUID());
        certificate1.setName("certificate1");

        GiftCertificate certificate2 = new GiftCertificate();
        certificate2.setId(UUID.randomUUID());
        certificate2.setName("certificate2");

        GiftCertificate certificate3 = new GiftCertificate();
        certificate3.setId(UUID.randomUUID());
        certificate3.setName("certificate3");

        giftCertificates.add(certificate1);
        giftCertificates.add(certificate2);
        giftCertificates.add(certificate3);

        return giftCertificates;
    }

    private List<GiftCertificateDto> getGiftCertificateDtoList(){
        List<GiftCertificateDto> giftCertificateDtoList = new ArrayList<>();
        GiftCertificateDto certificate1 = new GiftCertificateDto();
        certificate1.setId(UUID.randomUUID());
        certificate1.setName("certificate1");

        GiftCertificateDto certificate2 = new GiftCertificateDto();
        certificate2.setId(UUID.randomUUID());
        certificate2.setName("certificate2");

        GiftCertificateDto certificate3 = new GiftCertificateDto();
        certificate3.setId(UUID.randomUUID());
        certificate3.setName("certificate3");

        giftCertificateDtoList.add(certificate1);
        giftCertificateDtoList.add(certificate2);
        giftCertificateDtoList.add(certificate3);

        return giftCertificateDtoList;
    }


}