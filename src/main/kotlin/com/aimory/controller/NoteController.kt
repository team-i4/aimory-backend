package com.aimory.controller

import com.aimory.service.NoteService
import org.springframework.web.bind.annotation.RestController

@RestController
class NoteController(
    private val noteService: NoteService,
)
