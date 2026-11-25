package com.mcp.assignment.model;

public class FileContent {

    private String name;
    private String path;
    private String content;
    private String sha;
    private String htmlUrl;

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getPath() { return path; }
    public void setPath(String path) { this.path = path; }

    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }

    public String getSha() { return sha; }
    public void setSha(String sha) { this.sha = sha; }

    public String getHtmlUrl() { return htmlUrl; }
    public void setHtmlUrl(String htmlUrl) { this.htmlUrl = htmlUrl; }
}