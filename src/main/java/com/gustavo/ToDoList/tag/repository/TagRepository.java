package com.gustavo.ToDoList.tag.repository;

import com.gustavo.ToDoList.tag.model.Tag;
import org.springframework.data.repository.CrudRepository;

public interface TagRepository extends CrudRepository<Tag, Integer> {

}
