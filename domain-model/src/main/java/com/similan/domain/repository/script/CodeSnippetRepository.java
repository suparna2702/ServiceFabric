package com.similan.domain.repository.script;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

import com.similan.domain.entity.script.CodeSnippet;
import com.similan.domain.repository.script.jpa.JpaCodeSnippetRepository;

@Repository
public class CodeSnippetRepository {
  @Autowired
  private JpaCodeSnippetRepository repository;

  public CodeSnippet findOne(Long id) {
    return repository.findOne(id);
  }

  public CodeSnippet save(CodeSnippet codeSnippet) {
    return repository.save(codeSnippet);
  }

  public List<CodeSnippet> findAll() {
    return repository.findAll();
  }

  public List<CodeSnippet> findAll(Sort sort) {
    return repository.findAll(sort);
  }

  public CodeSnippet findByName(String name) {
    return repository.findByName(name);
  }

  public CodeSnippet create(String name, String snippet) {
    CodeSnippet codeSnippet = new CodeSnippet(name, snippet);
    return codeSnippet;
  }

}
