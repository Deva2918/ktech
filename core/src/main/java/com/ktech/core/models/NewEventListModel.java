package com.ktech.core.models;

import com.day.cq.wcm.api.Page;
import com.day.cq.wcm.api.PageManager;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.SlingObject;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


@Model(adaptables = {Resource.class, SlingHttpServletRequest.class}, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class NewEventListModel {
    @SlingObject
    private ResourceResolver resourceResolver;
    @Inject
    private Page currentPage;
    private String test;

    public String getTest() {
        return test;
    }

    public List<EventPagesList> getPageList() {
        return pageList;
    }

    List<EventPagesList> pageList;
    private List<Page> childPages;

    public List<Page> getChildPages() {
        return childPages;
    }

    @PostConstruct
    protected void init() {
        test = currentPage.getPath();
        childPages = new ArrayList<>();
        PageManager pageManager = resourceResolver.adaptTo(PageManager.class);
        if (pageManager != null) {
            Page rootPage = pageManager.getPage(currentPage.getPath());
            if (rootPage != null) {
                Iterator<Page> childIterator = rootPage.listChildren();
                while (childIterator.hasNext()) {
                    Page childPage = childIterator.next();
                    childPages.add(childPage);
                }
            }
        }
    }
}