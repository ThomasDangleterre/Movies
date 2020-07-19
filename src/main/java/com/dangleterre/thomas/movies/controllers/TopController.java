package com.dangleterre.thomas.movies.controllers;

import com.dangleterre.thomas.movies.beans.TopBean;
import com.dangleterre.thomas.movies.services.TopService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/top")
public class TopController {

    @Autowired
    TopService topService;

    @GetMapping
    public ResponseEntity<List<TopBean>> get(@RequestParam Long dateBegin, @RequestParam Long dateEnd) {
        List<TopBean> listTopBean = topService.getTops(dateBegin, dateEnd);
        return ResponseEntity.ok(listTopBean);
    }
}
