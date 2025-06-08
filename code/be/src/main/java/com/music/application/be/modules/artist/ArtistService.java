package com.music.application.be.modules.artist;

import com.music.application.be.modules.album.AlbumRepository;
import com.music.application.be.modules.artist.dto.ArtistResponseDTO;
import com.music.application.be.modules.artist.dto.CreateArtistDTO;
import com.music.application.be.modules.artist.dto.UpdateArtistDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class ArtistService {

    @Autowired
    private ArtistRepository artistRepository;

    @Autowired
    private AlbumRepository albumRepository;

    public ArtistResponseDTO createArtist(CreateArtistDTO createArtistDTO) {
        Artist artist = new Artist();
        artist.setName(createArtistDTO.getName());
        artist.setAvatar(createArtistDTO.getAvatar());
        artist.setDescription(createArtistDTO.getDescription());
        artist.setFollowerCount(0);

        Artist savedArtist = artistRepository.save(artist);
        return mapToResponseDTO(savedArtist);
    }

    public ArtistResponseDTO getArtistById(Long id) {
        Artist artist = artistRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Artist not found"));
        return mapToResponseDTO(artist);
    }

    public Page<ArtistResponseDTO> getAllArtists(Pageable pageable) {
        return artistRepository.findAll(pageable).map(this::mapToResponseDTO);
    }

    public ArtistResponseDTO updateArtist(Long id, UpdateArtistDTO updateArtistDTO) {
        Artist artist = artistRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Artist not found"));

        if (updateArtistDTO.getName() != null) {
            artist.setName(updateArtistDTO.getName());
        }
        if (updateArtistDTO.getAvatar() != null) {
            artist.setAvatar(updateArtistDTO.getAvatar());
        }
        if (updateArtistDTO.getDescription() != null) {
            artist.setDescription(updateArtistDTO.getDescription());
        }

        Artist updatedArtist = artistRepository.save(artist);
        return mapToResponseDTO(updatedArtist);
    }

    public void deleteArtist(Long id) {
        Artist artist = artistRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Artist not found"));
        artistRepository.delete(artist);
    }

    public Page<ArtistResponseDTO> searchArtists(String query, Pageable pageable) {
        return artistRepository.findByNameContainingIgnoreCase(query, pageable).map(this::mapToResponseDTO);
    }

    public String shareArtist(Long id) {
        return "https://musicapp.com/artist/" + id;
    }

    private ArtistResponseDTO mapToResponseDTO(Artist artist) {
        ArtistResponseDTO dto = new ArtistResponseDTO();
        dto.setId(artist.getId());
        dto.setName(artist.getName());
        dto.setAvatar(artist.getAvatar());
        dto.setDescription(artist.getDescription());
        dto.setFollowerCount(artist.getFollowerCount());
        return dto;
    }
}