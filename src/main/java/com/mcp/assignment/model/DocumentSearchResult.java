package com.mcp.assignment.model;

public class DocumentSearchResult {

    private String fileName;
    private String filePath;
    private String content;
    private int occurrences;
    private int relevance;

    public String getFileName() { return fileName; }
    public void setFileName(String fileName) { this.fileName = fileName; }

    public String getFilePath() { return filePath; }
    public void setFilePath(String filePath) { this.filePath = filePath; }

    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }

    public int getOccurrences() { return occurrences; }
    public void setOccurrences(int occurrences) { this.occurrences = occurrences; }

    public int getRelevance() { return relevance; }
    public void setRelevance(int relevance) { this.relevance = relevance; }
}