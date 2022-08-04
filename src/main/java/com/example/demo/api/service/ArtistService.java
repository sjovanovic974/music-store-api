package com.example.demo.api.service;

import com.example.demo.api.model.Artist;

import java.util.List;
import java.util.Optional;

public interface ArtistService {

    Artist saveArtist(Artist artist);

    void deleteArtist(Long id);

    Optional<Artist> findByName(String artistName);

    List<Artist> getArtists();

    Optional<Artist> getArtistById(Long id);

    Artist updateArtist(Long id, Artist artist);
}
