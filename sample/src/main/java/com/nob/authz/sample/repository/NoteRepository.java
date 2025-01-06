package com.nob.authz.sample.repository;

import com.nob.authz.sample.model.Note;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NoteRepository extends JpaRepository<Note, Long> {

}
