package com.example.demo.service;

import com.example.demo.model.Artist;

public interface ArtistService {

    Artist saveArtist(Artist artist);

    void deleteArtist(Long id);
}
