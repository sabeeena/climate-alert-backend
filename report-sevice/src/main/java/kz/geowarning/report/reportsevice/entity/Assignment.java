package kz.geowarning.report.reportsevice.entity;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "assigments")
public class Assignment {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String name;
    private String userIncoming;
    private String userOutComing;
    private String entityType;
    private String entityId;
    @Column(length = 2000)
    private String shortContent;
    private LocalDateTime sentDateTime;
    @Column(length = 2000)
    private String comment;
    @Column(length = 2000)
    private String description;

    private boolean isSenderAdmin;

    public Assignment(Long id, String name, String userIncoming, String userOutComing, String entityType, String entityId, String shortContent, LocalDateTime sentDateTime, String comment, String description, boolean isSenderAdmin) {
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

    public Assignment() {
    }


    public boolean isSenderAdmin() {
        return isSenderAdmin;
    }

    public void setSenderAdmin(boolean senderAdmin) {
        isSenderAdmin = senderAdmin;
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
}






