package com.gustavo.ToDoList.tag.service;

import com.gustavo.ToDoList.tag.repository.TagRepository;
import org.springframework.stereotype.Service;

@Service
public class TagService {

    private final TagRepository repository;


    public TagService(TagRepository repository) {
        this.repository = repository;
    }


}
