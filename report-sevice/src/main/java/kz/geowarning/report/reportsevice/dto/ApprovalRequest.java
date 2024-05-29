package kz.geowarning.report.reportsevice.dto;

import kz.geowarning.report.reportsevice.entity.Assignment;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ApprovalRequest {
    private Assignment assignment;
    private String comment;
    private boolean publish;
    private String title;

    public ApprovalRequest(Assignment assignment, String comment, boolean publish, String title) {
        this.assignment = assignment;
        this.comment = comment;
        this.publish = publish;
        this.title = title;
    }

    public ApprovalRequest() {
    }
}
