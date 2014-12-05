package net.webownia.applicationmgr.data.model;

import net.webownia.applicationmgr.shared.enums.ApplicationStatus;
import org.joda.time.LocalDateTime;

import javax.persistence.*;

/**
 * Created by Adam Barczewski on 2014-12-04.
 */
@Entity
public class ApplicationForm extends AbstractApplicationForm {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Version
    private long version;

    public ApplicationForm() {
        super();
    }

    public ApplicationForm(String name, String content, ApplicationStatus status, LocalDateTime createdDate) {
        super(name, content, status, createdDate);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public long getVersion() {
        return version;
    }

    public void setVersion(long version) {
        this.version = version;
    }
}
