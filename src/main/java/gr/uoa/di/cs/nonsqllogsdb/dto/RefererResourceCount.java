package gr.uoa.di.cs.nonsqllogsdb.dto;

import java.util.Set;

public class RefererResourceCount {
    private String referer;
    private Set<String> resources;

    // Constructor, getters, and setters
    public RefererResourceCount(String referer, Set<String> resources) {
        this.referer = referer;
        this.resources = resources;
    }

    public String getReferer() {
        return referer;
    }

    public void setReferer(String referer) {
        this.referer = referer;
    }

    public Set<String> getResources() {
        return resources;
    }

    public void setResources(Set<String> resources) {
        this.resources = resources;
    }
}
