package com.example.demo.service;

import com.example.demo.error.exceptions.CustomBadRequestException;
import com.example.demo.model.Artist;
import com.example.demo.repository.ArtistRepository;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class ArtistServiceImpl implements ArtistService {

    private final ArtistRepository artistRepository;

    public ArtistServiceImpl(ArtistRepository artistRepository) {
        this.artistRepository = artistRepository;
    }

    @Override
    public Artist saveArtist(Artist artist) {
        return artistRepository.save(artist);
    }

    @Override
    public void deleteArtist(Long id) {
        artistRepository.deleteById(id);
    }

    @Override
    public Optional<Artist> findByName(String artistName) {
        return artistRepository.findByName(artistName);
    }

    @Override
    public List<Artist> getArtists() {
        return artistRepository.findAll();
    }

    @Override
    public Optional<Artist> getArtistById(Long id) {
        return artistRepository.findById(id);
    }

    @Override
    public Artist updateArtist(Long id, Artist artist) {
        Optional<Artist> dbArtist = artistRepository.findById(id);
        if (dbArtist.isEmpty()) {
            throw new CustomBadRequestException("No artist with id: " + id + " in the system!");
        }
        dbArtist.get().setName(artist.getName());
        return artistRepository.save(dbArtist.get());
    }
}
