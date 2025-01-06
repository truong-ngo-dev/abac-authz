package com.nob.authz.sample.repository;

import com.nob.authz.sample.model.Note;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class NoteService {

    private final NoteRepository noteRepository;

    public List<Note> getNotes() {
        return noteRepository.findAll();
    }

    public Note getNote(Long id) {
        return noteRepository.findById(id).orElse(null);
    }
}
