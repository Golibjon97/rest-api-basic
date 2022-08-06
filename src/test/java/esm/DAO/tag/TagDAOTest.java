package esm.DAO.tag;

import com.epam.esm.DAO.tag.TagDAOImpl;
import com.epam.esm.entity.tag.Tag;
import esm.config.TestConfig;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

import javax.annotation.Resource;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(
        classes = {TestConfig.class},
        loader = AnnotationConfigContextLoader.class)
class TagDAOTest {

    @Autowired
    private TagDAOImpl tagDAO;

    @Resource
    private JdbcTemplate jdbcTemplate;

    @Test
    public void canCreateTag() {
        createTestObjects();
        Tag tag = new Tag(UUID.randomUUID(), "test");
        Tag test = tagDAO.create(tag);

        assertEquals(tag, test);
    }

    @Test
    public void canGetTagById() {
        Tag tag = tagDAO.get(UUID.fromString("64eeb184-972c-4bef-9879-c003d7352bd0"));

        assertNotNull(tag);
    }

    @Test
    public void canGetTagByName() {
        Tag testTag = tagDAO.getByName("testTag");
        assertNotNull(testTag);
    }

    @Test
    public void canDeleteTagById() {
        int delete = tagDAO.delete(UUID.fromString("badc0e82-f873-491a-b2cc-5ba6580ac71f"));
        assertEquals(1, delete);
    }

    private void createTestObjects() {
        jdbcTemplate.update(
                "create table tag (id uuid not null primary key,name varchar);insert into tag values('64eeb184-972c-4bef-9879-c003d7352bd0', 'testTag');insert into tag values('badc0e82-f873-491a-b2cc-5ba6580ac71f', 'testTag2');insert into tag values('c57e4db1-6ae4-4aee-b0d1-aaee00c26f77', 'testTag3');insert into tag values('fce5b289-6029-44cf-a870-5a0c30fd6d83', 'testTag4');");
    }
}