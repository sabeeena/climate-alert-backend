package kz.geowarning.report.reportsevice.dto;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import kz.geowarning.report.reportsevice.entity.Assignment;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class AssignmentDTO {

    private Long id;
    private String name;
    private String userIncoming;
    private String userOutComing;
    private String entityType;
    private String entityId;
    private String shortContent;
    private LocalDateTime sentDateTime;
    private String comment;
    private String description;
    private boolean isSenderAdmin;

    public Assignment asAssigment() {
        Assignment assignment = new Assignment();
        assignment.setId(id);
        assignment.setName(name);
        assignment.setUserIncoming(userIncoming);
        assignment.setUserOutComing(userOutComing);
        assignment.setEntityType(entityType);
        assignment.setEntityId(entityId);
        assignment.setShortContent(shortContent);
        assignment.setSentDateTime(sentDateTime);
        assignment.setComment(comment);
        assignment.setDescription(description);
        return assignment;
    }

    public AssignmentDTO(Long id, String name, String userIncoming, String userOutComing, String entityType, String entityId, String shortContent, LocalDateTime sentDateTime, String comment, String description, boolean isSenderAdmin) {
        this.id = id;
        this.name = name;
        this.userIncoming = userIncoming;
        this.userOutComing = userOutComing;
        this.entityType = entityType;
        this.entityId = entityId;
        this.shortContent = shortContent;
        this.sentDateTime = sentDateTime;
        this.comment = comment;
        this.description = description;
        this.isSenderAdmin = isSenderAdmin;
    }

    public AssignmentDTO() {
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

    public String getUserIncoming() {
        return userIncoming;
    }

    public void setUserIncoming(String userIncoming) {
        this.userIncoming = userIncoming;
    }

    public String getUserOutComing() {
        return userOutComing;
    }

    public void setUserOutComing(String userOutComing) {
        this.userOutComing = userOutComing;
    }

    public String getEntityType() {
        return entityType;
    }

    public void setEntityType(String entityType) {
        this.entityType = entityType;
    }

    public String getEntityId() {
        return entityId;
    }

    public void setEntityId(String entityId) {
        this.entityId = entityId;
    }

    public String getShortContent() {
        return shortContent;
    }

    public void setShortContent(String shortContent) {
        this.shortContent = shortContent;
    }

    public LocalDateTime getSentDateTime() {
        return sentDateTime;
    }

    public void setSentDateTime(LocalDateTime sentDateTime) {
        this.sentDateTime = sentDateTime;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isSenderAdmin() {
        return isSenderAdmin;
    }

    public void setSenderAdmin(boolean senderAdmin) {
        isSenderAdmin = senderAdmin;
    }
}
