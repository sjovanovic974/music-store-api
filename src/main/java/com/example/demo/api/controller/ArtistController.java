package com.example.demo.api.controller;

import com.example.demo.api.model.Artist;
import com.example.demo.api.service.ArtistService;
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
    public List<Artist> getAllArtists() {
        return artistService.getArtists();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Artist saveArtist(@Valid @RequestBody Artist artist) {
        return artistService.saveArtist(artist);
    }

    @PutMapping("/{id}")
    public Optional<Artist> updateArtist(@PathVariable("id") Long id, @Valid @RequestBody Artist artist) {
        return Optional.ofNullable(artistService.updateArtist(id, artist));
    }

    @DeleteMapping("/{id}")
    public void deleteArtist(@PathVariable("id") Long id) {
        artistService.deleteArtist(id);
    }
}
