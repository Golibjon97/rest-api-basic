package com.epam.esm.controller;

import com.epam.esm.entity.tag.Tag;
import com.epam.esm.mapper.service.tag.TagService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/tag")
@AllArgsConstructor
public class TagController {

    private final TagService tagService;

    @PostMapping("/create")
    public ResponseEntity<?> create( @RequestBody Tag tag){
        return ResponseEntity.status(201).body(tagService.create(tag));
    }

    @GetMapping("/get")
    public ResponseEntity<?> get(@RequestParam UUID id){
        return ResponseEntity.ok(tagService.get(id));
    }

    @GetMapping("/get_all")
    public ResponseEntity<?> getAll(){
        return ResponseEntity.ok(tagService.getAll());
    }

    @DeleteMapping("/delete")
    public ResponseEntity<?> delete( @RequestParam UUID id){
        return ResponseEntity.ok(tagService.delete(id));
    }
}
