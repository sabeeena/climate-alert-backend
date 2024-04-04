package kz.geowarning.report.reportsevice.dto;

import kz.geowarning.report.reportsevice.entity.Assignment;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ApprovalRequest {
    private Assignment assignment;
    private String comment;

    public ApprovalRequest(Assignment assignment, String comment) {
        this.assignment = assignment;
        this.comment = comment;
    }

    public ApprovalRequest() {
    }
}