package com.aimory.controller

import com.aimory.service.NoticeService
import org.springframework.web.bind.annotation.RestController

@RestController
class NoticeController(
    private val noticeService: NoticeService,
)
