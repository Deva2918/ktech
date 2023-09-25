package com.ktech.core.models;

import com.day.cq.wcm.api.Page;
import com.day.cq.wcm.api.PageManager;
import com.day.cq.tagging.Tag;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.SlingObject;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;


@Model(adaptables = {Resource.class, SlingHttpServletRequest.class}, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class EventListModel {
    @SlingObject
    private ResourceResolver resourceResolver;
    @Inject
    private Page currentPage;
    @ValueMapValue
    private String header;

    public String getHeader() {
        return header;
    }
    List<EventPagesList> pageList;
    public List<EventPagesList> getPageList() {
        return pageList;
    }
    @PostConstruct
    protected void init() {
        PageManager pageManager = currentPage.getPageManager();
        pageList = new ArrayList<>();
        if(pageManager!=null) {
            Page rootPage = pageManager.getPage(currentPage.getPath());
            if(rootPage!=null) {
                Iterator<Page> childPages = rootPage.listChildren();
                while (childPages.hasNext()) {
                    Page childPage = childPages.next();
                    EventPagesList eventChildPages = new EventPagesList();
                    eventChildPages.setTitle(childPage.getTitle());
                    eventChildPages.setPagePath(childPage.getPath());
                    ValueMap pageProperties = childPage.getProperties();
                    if (pageProperties.containsKey("eventStartDate")) {
                        eventChildPages.setEventStartDate(pageProperties.get("eventStartDate",new Date()));
                    }
                    if (pageProperties.containsKey("eventEndDate")) {
                        eventChildPages.setEventEndDate(pageProperties.get("eventEndDate",new Date()));
                    }
                    if (pageProperties.containsKey("eventLocation")) {
                        eventChildPages.setEventLocation(pageProperties.get("eventLocation").toString());
                    }
                    pageList.add(eventChildPages);
                }
            }
        }
    }
}