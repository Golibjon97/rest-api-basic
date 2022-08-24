package com.epam.esm.controller;

import com.epam.esm.dto.GiftCertificateDto;
import com.epam.esm.dto.response.BaseExceptionDto;
import com.epam.esm.dto.response.BaseResponseDto;
import com.epam.esm.enums.SortEnum;
import com.epam.esm.exception.NoDataFoundException;
import com.epam.esm.service.gift_certificate.GiftCertificateService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

/**
 * Gift Certificate API to manipulate with Gift Certificate objects, Tags and their connections
 * (create, get, update, delete)
 */
@RestController
@RequestMapping("/gift_certificate")
@AllArgsConstructor
public class GiftCertificateController {

    private final GiftCertificateService giftCertificateService;

    /**
     * Used for gift-certificate creation. It includes tags related to the certificates.
     * @param giftCertificate DTO typed gift certificate object
     * @return created giftCertificate object along with tags included
     */
    @PostMapping("/create")
    public ResponseEntity<?> create(@RequestBody GiftCertificateDto giftCertificate){
        GiftCertificateDto created = giftCertificateService.create(giftCertificate);
        if(created==null)
            return ResponseEntity.badRequest().body(new BaseExceptionDto(400, "Failed to create", 10204));
        else
            return ResponseEntity.status(201).body(new BaseResponseDto<>(201, "certificate created", created));
    }

    /**
     * gets the gift_certificates as per the given UUID including the tags connected
     * @param id UUID of existing giftCertificate
     * @return existing giftCertificate object along with tags included
     */
    @GetMapping("/get")
    @ResponseBody
    public ResponseEntity<?> get(@RequestParam UUID id){
        GiftCertificateDto result = giftCertificateService.get(id);
        if(result != null) {
            return ResponseEntity.ok(new BaseResponseDto<>(200, "Gift Certificate", result));
        }
        return ResponseEntity.badRequest().body(new BaseResponseDto<>(404, "No gift certificate found"));
    }

    /**
     * Gets all the giftCertificates as per the given giftCertificate name or description name
     * or tag name with the additional possibility of date sort or name sort
     * @param searchWord searches as per the given giftCertificate name or description name
     * @param tagName searches as per the given tag name
     * @param sort can sort the search as per date, name in ascending or descending order
     * @return returns list of giftCertificates
     */
    @GetMapping("/get_all")
    public ResponseEntity<?> getAll(
            @RequestParam(required = false) String searchWord,
            @RequestParam(required = false) String tagName,
            @RequestParam(required = false) String sort
    ){
        SortEnum sortEnum = SortEnum.findByName(sort);
        if (sortEnum==SortEnum.UNKNOWN)
            throw new NoDataFoundException("The sort type provided is unknown");

        List<GiftCertificateDto> giftCertificateDtos = giftCertificateService.getAll(
                searchWord, tagName, sortEnum
        );
        if(giftCertificateDtos.isEmpty())
            return  ResponseEntity.badRequest().body(new BaseResponseDto<>(404, "No certificates found"));
        return ResponseEntity.ok(new BaseResponseDto<>(200, "List of Gift Certificates", giftCertificateDtos));
    }

    /**
     * Deletes the giftCertificate as per the given UUID. Also the connection with tags get removed
     * @param id input parameter to find the giftCertificate
     * @return the HTTP status
     */
    @PostMapping("/delete")
    public ResponseEntity<?> delete(@RequestParam UUID id){
        int deleted = giftCertificateService.delete(id);
        if(deleted == 1) {
            return ResponseEntity.ok(new BaseResponseDto<>(200, "Certificate Deleted"));
        }
        throw new NoDataFoundException("No gift certificate exists with this ID");
    }

    /**
     * Updates the giftCertificate as per the given UUID
     * @param update new GiftCertificate DTO object to update the old one
     * @param id input parameter to find the giftCertificate
     * @return the DTO typed object of updated giftCertificate
     */
    @PostMapping("/update")
    public ResponseEntity<?> update(
            @RequestBody GiftCertificateDto update,
            @RequestParam UUID id
    ){
        GiftCertificateDto giftCertificate = giftCertificateService.update(update, id);
        if(giftCertificate != null) {
            return ResponseEntity.ok(new BaseResponseDto<>(200, "Certificate Updated", giftCertificate));
        }
        return ResponseEntity.badRequest().body(new BaseExceptionDto(400, "Cannot update Certificate", 10204));
    }

}
