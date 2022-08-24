package com.epam.esm.dao;

import com.epam.esm.config.TestConfig;
import com.epam.esm.dao.gift_certificate.GiftCertificateDAOImpl;
import com.epam.esm.dao.tag.TagDAOImpl;
import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.Tag;
import com.epam.esm.exception.TagAlreadyExistException;
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
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(
        classes = {TestConfig.class},
        loader = AnnotationConfigContextLoader.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class TagDAOTest{

    @Autowired
    private TagDAOImpl tagDAO;
    @Autowired
    private GiftCertificateDAOImpl giftCertificateDAO;

    @Resource
    private JdbcTemplate jdbcTemplate;

    private GiftCertificate certificate;
    private Tag tag;
    @BeforeEach
    void setUp() {
        certificate = new GiftCertificate(
                UUID.fromString("59f2d7ac-9d5b-49ff-8e04-b9150637fd0d"),
                "testCertificate",
                "this is for test",
                new BigDecimal("1000"),
                2,
                LocalDateTime.now(),
                LocalDateTime.now()
        );

    }

    @Test
    @Order(1)
    public void canCreateGiftCertificate() {
        createGiftCertificateTable();
        GiftCertificate result = giftCertificateDAO.create(certificate);
        assertNotNull(result);
    }

    @Test
    @Order(2)
    public void canCreateTag() {
        createTagTable();
        createTagCertificateTagTable();
        Tag tag1 = new Tag(UUID.fromString("fce5b289-6029-44cf-a870-5a0c30fd6d83"), "test144444");
        Tag test = tagDAO.create(tag1);
        assertEquals(tag1, test);
    }
    @Test
    @Order(3)
    public void canNotCreateTag() {
        Assertions.assertThrows(TagAlreadyExistException.class, () -> {
            Tag tag2 = new Tag(UUID.fromString("fce5b289-6029-44cf-a870-5a0c30fd6d83"), "test244444");
            Tag test = tagDAO.create(tag2);
        });
    }

    @Test
    @Order(4)
    public void canGetTagById() {
        Tag tag = tagDAO.get(UUID.fromString("fce5b289-6029-44cf-a870-5a0c30fd6d83"));

        assertNotNull(tag);
    }

    @Test
    @Order(5)
    public void canGetAll(){
        List<Tag> testTag = tagDAO.getAll();
        assertNotNull(testTag);
    }

    @Test
    public void canGetTagByName() {
        Tag testTag = tagDAO.getByName("testTag3");
        assertNotNull(testTag);
    }

    @Test
    public void canCreateWithGiftCertificate(){
        Tag tag = new Tag(UUID.randomUUID(), "TestName1");
        Tag tag1 = new Tag(UUID.randomUUID(), "TestName2");
        Tag tag2 = new Tag(UUID.randomUUID(), "TestName3");
        List<Tag> list = new ArrayList<>();
        list.add(tag);
        list.add(tag1);
        list.add(tag2);
        tagDAO.createWithGiftCertificate(certificate.getId(), list);

    }

    @Test
    public void canDeleteTagById() {
        int delete = tagDAO.delete(UUID.fromString("64eeb184-972c-4bef-9879-c003d7352bd0"));
        assertEquals(1, delete);
    }

    public void createGiftCertificateTable() {
        String query = "create table gift_certificate" +
                "(" +
                "id uuid not null primary key,name varchar," +
                "description varchar,price numeric,duration int," +
                "create_date timestamp,last_update_date timestamp" +
                ");";
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