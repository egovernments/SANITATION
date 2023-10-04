package org.egov.pqm.web.controllers;

import org.egov.pqm.web.model.TestRequest;
import org.egov.pqm.web.model.TestResponse;
import org.egov.pqm.web.model.TestSearchRequest;
import javax.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PqmController {

  @PostMapping(value = "/pqm/v1/_create", produces = {"*/*"}, consumes = {"application/json"})
  ResponseEntity<TestResponse> pqmV1CreatePost(@Valid @RequestBody TestRequest testRequest) {
    return ResponseEntity.status(HttpStatus.ACCEPTED).body(null);
  }


  @PostMapping(value = "/pqm/v1/_search", produces = {"*/*"}, consumes = {"application/json"})
  ResponseEntity<TestResponse> pqmV1SearchPost(
      @Valid @RequestBody TestSearchRequest testSearchRequest) {
    return ResponseEntity.status(HttpStatus.ACCEPTED).body(null);
  }


  @PostMapping(value = "/pqm/v1/_update", produces = {"*/*"}, consumes = {"application/json"})
  ResponseEntity<TestResponse> pqmV1UpdatePost(@Valid @RequestBody TestRequest testRequest) {
    return ResponseEntity.status(HttpStatus.ACCEPTED).body(null);
  }

}

