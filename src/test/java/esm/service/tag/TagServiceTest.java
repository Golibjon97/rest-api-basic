package esm.service.tag;

import com.epam.esm.DAO.tag.TagDAO;
import com.epam.esm.DTO.response.BaseResponseDto;
import com.epam.esm.entity.tag.Tag;
import com.epam.esm.mapper.service.tag.TagServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.BDDMockito.given;


@ExtendWith(MockitoExtension.class)
class TagServiceTest {

    @Mock
    private TagDAO tagDAO;

    @InjectMocks
    private TagServiceImpl tagService;

    private Tag tag;

    @BeforeEach
    void setUp() {
        tag = new Tag(UUID.randomUUID(), "testTag");
    }

    // return value
    @Test
    public void canCreateTag(){
        given(tagDAO.create(tag)).willReturn(tag);

        BaseResponseDto<Tag> tagBaseResponseDto = tagService.create(tag);

        assertEquals(1, tagBaseResponseDto.getStatus());
        assertEquals("success", tagBaseResponseDto.getMessage());
    }

    @Test
    public void canGetTagById(){
        given(tagDAO.get(tag.getId())).willReturn(tag);

        BaseResponseDto<Tag> tagBaseResponseDto = tagService.get(tag.getId());

        assertEquals(1, tagBaseResponseDto.getStatus());
        assertEquals("success", tagBaseResponseDto.getMessage());
    }

    @Test
    public void canGetAllTags() {
        List<Tag> list = new ArrayList<>();
        Tag tag1= new Tag(UUID.randomUUID(), "star");
        Tag tag2= new Tag(UUID.randomUUID(), "mafia");
        Tag tag3= new Tag(UUID.randomUUID(), "sun");

        list.add(tag1);
        list.add(tag2);
        list.add(tag3);

        given(tagDAO.getAll()).willReturn(list);

        BaseResponseDto<List<Tag>> all = tagService.getAll();
        assertEquals(3, all.getData().size());
        Mockito.verify(tagDAO, Mockito.times(1)).getAll();
    }

    @Test
    public void canDeleteTag(){
        given(tagDAO.delete(tag.getId())).willReturn(1);

        BaseResponseDto deleteResponse = tagService.delete(tag.getId());

        assertEquals(1 ,deleteResponse.getStatus());
    }
}