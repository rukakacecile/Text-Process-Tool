package org.example.textprocessingtool;

// TextData.java


import java.util.Objects;

public class TextData {
    private int id;
    private String content;
    private String category;

    public TextData(int id, String content, String category) {
        this.id = id;
        this.content = content;
        this.category = category;
    }

    // Getters and setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    // Proper implementation of equals and hashCode for collection operations
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TextData textData = (TextData) o;
        return id == textData.id &&
                Objects.equals(content, textData.content) &&
                Objects.equals(category, textData.category);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, content, category);
    }

    @Override
    public String toString() {
        return String.format("ID: %d | Category: %s | Content: %s",
                id, category,
                content.length() > 50 ? content.substring(0, 47) + "..." : content);
    }
}
