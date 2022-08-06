package com.epam.esm.controller;

import com.epam.esm.DTO.GiftCertificateDto;
import com.epam.esm.mapper.service.gift_certificate.GiftCertificateService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/gift_certificate")
@AllArgsConstructor
public class GiftCertificateController {

    private final GiftCertificateService giftCertificateService;

    @PostMapping("/create")
    public ResponseEntity<?> create(@RequestBody GiftCertificateDto giftCertificate){
        return ResponseEntity.status(201).body(giftCertificateService.create(giftCertificate));
    }

    @GetMapping("/get")
    @ResponseBody
    public ResponseEntity<?> get(@RequestParam UUID id){
        return ResponseEntity.ok(giftCertificateService.get(id));
    }

    @GetMapping("/get_all")
    public ResponseEntity<?> getAll(
            @RequestParam(required = false) String searchWord,
            @RequestParam(required = false) String tagName,
            @RequestParam(required = false) boolean doNameSort,
            @RequestParam(required = false) boolean doDateSort,
            @RequestParam(required = false) boolean isDescending
    ){
        return ResponseEntity.ok(giftCertificateService.getAll(
               searchWord, tagName, doNameSort, doDateSort, isDescending
            ));
    }

    @DeleteMapping("/delete")
    public ResponseEntity<?> delete(@RequestParam UUID id){
        return ResponseEntity.ok(giftCertificateService.delete(id));
    }

    @PutMapping("/update")
    public ResponseEntity<?> update(
            @RequestBody GiftCertificateDto update,
            @RequestParam UUID id
    ){
        return ResponseEntity.ok(giftCertificateService.update(update, id));
    }

}
