package com.cards.zokudo.controllers;

import com.cards.zokudo.dto.request.ProcessorRequestDto;
import com.cards.zokudo.entities.Cards;
import com.cards.zokudo.services.card.sor.SORCardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequestMapping("{programUrl}/api/v1/sor")

@RestController
public class SORController {

    private final SORCardService sorCardService;

    @Autowired
    public SORController(final SORCardService sorCardService){
        this.sorCardService = sorCardService;
    }

    @PostMapping(value="/cards")
    public List<Page<Cards>> getCustomerCards(@RequestBody ProcessorRequestDto dto){
        return sorCardService.getCardsIncremental(dto.getStartDate(),dto.getEndDate());
    }
}
