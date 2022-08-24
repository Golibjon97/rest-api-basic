package com.epam.esm.service.tag;


import com.epam.esm.entity.Tag;
import com.epam.esm.service.BaseService;

import java.util.List;

public interface TagService extends BaseService<Tag> {
    List<Tag> getAll();

}
