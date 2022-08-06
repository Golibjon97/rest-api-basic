package esm.DAO.gift_certificate;

import com.epam.esm.DAO.gift_certificate.GiftCertificateDAOImpl;
import com.epam.esm.entity.gift_certificate.GiftCertificate;
import esm.config.TestConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(
        classes = {TestConfig.class},
        loader = AnnotationConfigContextLoader.class)
public class GiftCertificateDAOTest {

    @Autowired
    private GiftCertificateDAOImpl giftCertificateDAO;

    @Resource
    private JdbcTemplate jdbcTemplate;

    private GiftCertificate certificate;


    @BeforeEach
    void setUp() {
        certificate = new GiftCertificate(
                UUID.fromString("59f2d7aa-9d5b-49ff-8e04-b9150637fd0d"),
                "testCertificate",
                "this is for test",
                new BigDecimal("1000"),
                2,
                LocalDateTime.now(),
                LocalDateTime.now()
        );
    }

    @Test
    public void canCreateGiftCertificate() {
        createTestTable();
        GiftCertificate result = giftCertificateDAO.create(certificate);
        assertNotNull(result);
    }

    @Test
    public void canGetGiftCertificateById() {
        GiftCertificate result = giftCertificateDAO.get(this.certificate.getId());
        assertNotNull(result);
    }

    @Test
    public void canUpdateCertificate() {
        GiftCertificate update = new GiftCertificate(
                null,
                "newName",
                null,
                new BigDecimal("20000"),
                null,
                null,
                LocalDateTime.now()
        );
        GiftCertificate result = giftCertificateDAO.update(
                update,
                UUID.fromString("59f2d7aa-9d5b-49ff-8e04-b9150637fd0d"));
        assertEquals("newName", result.getName());
    }

    @Test
    public void canGetCertificateList() {
        List<GiftCertificate> all = giftCertificateDAO.getAll();
        assertEquals(1, all.size());
    }


    public void createTestTable() {
        String query = "create table gift_certificate(id uuid not null primary key,name varchar,description varchar,price numeric,duration int,create_date timestamp,last_update_date timestamp);";
        jdbcTemplate.update(query);
    }


}
