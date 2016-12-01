package com.similan.domain.repository.script.jpa;

import org.springframework.data.jpa.repository.JpaRepository;

import com.similan.domain.entity.script.CodeSnippet;

public interface JpaCodeSnippetRepository extends
    JpaRepository<CodeSnippet, Long> {

  CodeSnippet findByName(String name);

}
