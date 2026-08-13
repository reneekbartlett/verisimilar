package com.reneekbartlett.verisimilar.api.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
/***
 * Disable favicon without throwing errors
 */
public class FaviconController {
    @GetMapping("favicon.ico")
    @ResponseBody
    void returnNoFavicon() {}
}
