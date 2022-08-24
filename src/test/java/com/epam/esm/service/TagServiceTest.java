package com.epam.esm.service;

import com.epam.esm.dao.tag.TagDAO;
import com.epam.esm.entity.Tag;
import com.epam.esm.service.tag.TagServiceImpl;
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
import static org.junit.jupiter.api.Assertions.assertNotNull;
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

        Tag tag1 = tagService.create(tag);
        assertNotNull(tag1);
    }

    @Test
    public void canGetTagById(){
        given(tagDAO.get(tag.getId())).willReturn(tag);

        Tag tag1 = tagService.get(tag.getId());
        assertNotNull(tag1);
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

        List<Tag> all = tagService.getAll();
        assertEquals(3, all.size());
        Mockito.verify(tagDAO, Mockito.times(1)).getAll();
    }

    @Test
    public void canDeleteTag(){
        given(tagDAO.delete(tag.getId())).willReturn(1);
        int deleteResponse = tagService.delete(tag.getId());
        assertEquals(1 ,deleteResponse);
    }

    @Test
    public void canNotDeleteTag(){
        assertEquals(0, tagService.delete(UUID.randomUUID()));
    }

}