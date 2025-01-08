package com.bances.agua_deliciosa.model;

public class OrderMovement extends BaseModel {
    private String title;
    private String description;

    public OrderMovement() {
        super();
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof OrderMovement)) return false;
        OrderMovement that = (OrderMovement) o;
        return title != null && title.equals(that.title);
    }

    @Override
    public int hashCode() {
        return title != null ? title.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "OrderMovement [id=" + getId() + ", title=" + title + 
               ", description=" + description + "]";
    }
}
