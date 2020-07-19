package com.dangleterre.thomas.movies.controllers;

import com.dangleterre.thomas.movies.beans.TopBean;
import com.dangleterre.thomas.movies.services.TopService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
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
    @ApiOperation(value = "Get most commented movies", notes = "Should return top movies already present in the database ranking based on a number of comments added to the movie (as in the example) in the specified date range. The response should include the ID of the movie, position in rank and total number of comments (in the specified date range).")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Success", response = String.class),
    })
    public ResponseEntity<List<TopBean>> get(@RequestParam Long dateBegin, @RequestParam Long dateEnd) {
        List<TopBean> listTopBean = topService.getTops(dateBegin, dateEnd);
        return ResponseEntity.ok(listTopBean);
    }
}
