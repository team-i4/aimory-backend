package com.aimory.service

import com.aimory.repository.NoteRepository
import org.springframework.stereotype.Service

@Service
class NoteService(
    private val noteRepository: NoteRepository,
)
