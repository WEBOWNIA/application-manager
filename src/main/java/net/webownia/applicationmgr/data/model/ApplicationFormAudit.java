package net.webownia.applicationmgr.data.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * Created by abarczewski on 2014-12-05.
 */
@Entity
public class ApplicationFormAudit extends AbstractApplicationForm {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private Long applicationFormId;

    private long version;

    public ApplicationFormAudit() {
        super();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getApplicationFormId() {
        return applicationFormId;
    }

    public void setApplicationFormId(Long applicationFormId) {
        this.applicationFormId = applicationFormId;
    }

    public long getVersion() {
        return version;
    }

    public void setVersion(long version) {
        this.version = version;
    }
}
