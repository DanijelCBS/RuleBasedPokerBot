package sbnz.rulebased.pokerbot.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sbnz.rulebased.pokerbot.dto.RulesDTO;
import sbnz.rulebased.pokerbot.services.RulesService;

@RestController
@RequestMapping("/rules")
@CrossOrigin
public class RulesController {

    @Autowired
    RulesService rulesService;

    @PostMapping
    public ResponseEntity<Object> createRules(@RequestBody RulesDTO rules) {
        try {
            return new ResponseEntity<>(rulesService.createRules(rules), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}
