package com.epam.esm.dao;

import com.epam.esm.config.TestConfig;
import com.epam.esm.dao.gift_certificate.GiftCertificateDAOImpl;
import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.enums.SortEnum;
import com.epam.esm.exception.NoDataFoundException;
import org.junit.jupiter.api.*;
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
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class GiftCertificateDAOTest {

    @Autowired
    private GiftCertificateDAOImpl giftCertificateDAO;

    @Resource
    private JdbcTemplate jdbcTemplate;

    private GiftCertificate certificate;

    @BeforeEach
    void setUp() {
        certificate = new GiftCertificate(
                UUID.fromString("59f2d7ab-9d5b-49ff-8e04-b9150637fd0d"),
                "testCertificate",
                "this is for test",
                new BigDecimal("1000"),
                2,
                LocalDateTime.now(),
                LocalDateTime.now()
        );
    }


    @Test
    void getAllQueriesDateAsc(){
        String query = giftCertificateDAO.getAllQueries(SortEnum.valueOf("DATE_ASC"));
        assertEquals("select *from get_certificate(?, ?) order by create_date;", query);
    }

    @Test
    void getAllQueriesDateDesc(){
        String query = giftCertificateDAO.getAllQueries(SortEnum.valueOf("DATE_DESC"));
        assertEquals("select *from get_certificate(?, ?) order by create_date desc;", query);
    }

    @Test
    void getAllQueriesNameAsc(){
        String query = giftCertificateDAO.getAllQueries(SortEnum.valueOf("NAME_ASC"));
        assertEquals("select *from get_certificate(?, ?) order by name;", query);
    }

    @Test
    void getAllQueriesNameDesc(){
        String query = giftCertificateDAO.getAllQueries(SortEnum.valueOf("NAME_DESC"));
        assertEquals("select *from get_certificate(?, ?) order by name desc;", query);
    }

    @Test
    void getAllQueriesNameDateAsc(){
        String query = giftCertificateDAO.getAllQueries(SortEnum.valueOf("NAME_DATE_ASC"));
        assertEquals("select *from get_certificate(?, ?) order by name, create_date;", query);
    }

    @Test
    void getAllQueriesNameDateDesc(){
        String query = giftCertificateDAO.getAllQueries(SortEnum.valueOf("NAME_DATE_DESC"));
        assertEquals("select *from get_certificate(?, ?) order by name, create_date desc;", query);
    }

    @Test
    @Order(1)
    public void canCreateGiftCertificate() {
        createGiftCertificateTable();
        createTagTable();
        createTagCertificateTagTable();
        GiftCertificate result = giftCertificateDAO.create(certificate);
        assertNotNull(result);
    }

    @Test
    @Order(4)
    public void canGetGiftCertificateById() {
        GiftCertificate result = giftCertificateDAO.get(this.certificate.getId());
        assertNotNull(result);
    }

    @Test
    @Order(5)
    public void canNotGetGiftCertificateById() {
        Assertions.assertThrows(NoDataFoundException.class, () ->{
            giftCertificateDAO.get(UUID.randomUUID());
        });

    }

    @Test
    @Order(2)
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
                UUID.fromString("59f2d7ab-9d5b-49ff-8e04-b9150637fd0d"));
        assertEquals("newName", result.getName());
    }

    @Test
    @Order(3)
    public void canGetCertificateList() {
        List<GiftCertificate> all = giftCertificateDAO.getAll();
        assertEquals(1, all.size());
    }

    @Test
    public void canDeleteGiftCertificate(){
        int giftCertificate = giftCertificateDAO.delete(this.certificate.getId());
        assertEquals(1, giftCertificate);
    }


    public void createGiftCertificateTable() {
        String query = "create table gift_certificate(" +
                "id uuid not null primary key,name varchar,description varchar," +
                "price numeric,duration int,create_date timestamp," +
                "last_update_date timestamp);";
        jdbcTemplate.update(query);
    }
    private void createTagTable() {
        jdbcTemplate.update(
                "create table tag (id uuid not null primary key,name varchar);" +
                        "insert into tag values('64eeb184-972c-4bef-9879-c003d7352bd0', 'testTag');" +
                        "insert into tag values('badc0e82-f873-491a-b2cc-5ba6580ac71f', 'testTag2');" +
                        "insert into tag values('c57e4db1-6ae4-4aee-b0d1-aaee00c26f77', 'testTag3');");
    }

    private void createTagCertificateTagTable() {
        jdbcTemplate.update(
                "CREATE TABLE gift_certificate_tag (\n" +
                        "  gift_certificate_id  uuid REFERENCES gift_certificate (id), \n" +
                        "  tag_id               uuid REFERENCES tag                   (id)\n" +
                        "  --CONSTRAINT bill_product_pkey PRIMARY KEY (gift_certificate_id, tag_id)  -- explicit pk\n" +
                        ");");
    }


}
