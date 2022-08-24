package com.epam.esm.controller;

import com.epam.esm.dto.response.BaseExceptionDto;
import com.epam.esm.dto.response.BaseResponseDto;
import com.epam.esm.entity.Tag;
import com.epam.esm.service.tag.TagService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/tag")
@AllArgsConstructor
public class TagController {

    private final TagService tagService;

    @PostMapping("/create")
    public ResponseEntity<?> create( @RequestBody Tag tag){
        tag.setId(UUID.randomUUID());
        tag = tagService.create(tag);
        if(tag == null)
            return ResponseEntity.badRequest().body(new BaseExceptionDto(400, "Failed to created", 10204));
        else
            return ResponseEntity.status(201).body(new BaseResponseDto<>(201, "Tag created", tag));
    }

    @GetMapping("/get")
    public ResponseEntity<?> get(@RequestParam UUID id){
        Tag tag = tagService.get(id);
        return ResponseEntity.ok(new BaseResponseDto(200, "Tag", tag));
    }

    @GetMapping("/get_all")
    public ResponseEntity<?> getAll(){
        List<Tag> tags = tagService.getAll();
        if(tags.isEmpty())
            return ResponseEntity.ok(new BaseResponseDto<>(404, "No Tags were found"));
        else
            return ResponseEntity.ok(new BaseResponseDto<>(200, "List of tags", tags));
    }

    @PostMapping("/delete")
    public ResponseEntity<?> delete( @RequestParam UUID id){
        int delete = tagService.delete(id);
        if(delete == 0)
            return ResponseEntity.badRequest().body(new BaseExceptionDto(404, "No tags were found", 10204));
        return ResponseEntity.ok(new BaseResponseDto<>(200, "deleted"));
    }
}
