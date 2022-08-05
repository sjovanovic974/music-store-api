package com.example.demo.api.controller;

import com.example.demo.api.model.Artist;
import com.example.demo.api.service.ArtistService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/artists")
public class ArtistController {

    private final ArtistService artistService;

    public ArtistController(ArtistService artistService) {
        this.artistService = artistService;
    }

    @GetMapping
    @Operation(
            tags = {"api", "admin"},
            summary = "Gets a list of artist",
            description ="Retrieves a list of artist from DB"
    )
    public List<Artist> getAllArtists() {
        return artistService.getArtists();
    }

    @PostMapping
    @Operation(
            tags = {"api", "admin"},
            summary = "Saves a new artist",
            description ="Saves a new artist. Input is validated against a number of constraints"
    )
    @ResponseStatus(HttpStatus.CREATED)
    public Artist saveArtist(@Valid @RequestBody Artist artist) {
        return artistService.saveArtist(artist);
    }

    @PutMapping("/{id}")
    @Operation(
            tags = {"api", "admin"},
            summary = "Updates an artist",
            description ="Updates an artist. Input is validated against a number of constraints",
            parameters = {@Parameter(name = "id", description = "Valid id number", example = "3")}
    )
    public Optional<Artist> updateArtist(@PathVariable("id") Long id, @Valid @RequestBody Artist artist) {
        return Optional.ofNullable(artistService.updateArtist(id, artist));
    }

    @DeleteMapping("/{id}")
    @Operation(
            tags = {"api", "admin"},
            summary = "Deletes an artist",
            description ="Removes an artist from DB",
            parameters = {@Parameter(name = "id", description = "Valid id number", example = "3")}
    )
    public void deleteArtist(@PathVariable("id") Long id) {
        artistService.deleteArtist(id);
    }
}
