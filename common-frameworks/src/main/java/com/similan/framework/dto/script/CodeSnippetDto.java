package com.similan.framework.dto.script;

import java.io.Serializable;

public class CodeSnippetDto implements Serializable {

  private static final long serialVersionUID = 1L;

  private Long id;

  private String name;

  private String snippet;

  public CodeSnippetDto() {
  }

  public CodeSnippetDto(Long id, String name, String snippet) {
    this.id = id;
    this.name = name;
    this.snippet = snippet;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getSnippet() {
    return snippet;
  }

  public void setSnippet(String snippet) {
    this.snippet = snippet;
  }
}
