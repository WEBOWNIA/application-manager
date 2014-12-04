package net.webownia.applicationmgr.data.model;

import net.webownia.applicationmgr.shared.enums.ApplicationStage;
import org.hibernate.annotations.Type;
import org.joda.time.LocalDateTime;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

/**
 * Created by abarczewski on 2014-12-03.
 */
@Entity
@EnableJpaAuditing
public class ApplicationForm {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    private String name;

    @NotNull
    private String content;

    @Enumerated(EnumType.STRING)
    @NotNull
    private ApplicationStage stage;

    private String cause;

    @DateTimeFormat(pattern = "dd/MM/yyyy HH:mm:ss:SSS")
    @NotNull
    @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentLocalDateTime")
    private LocalDateTime fromDate;

    @Version
    private long version;

    public ApplicationForm() {
    }

    public ApplicationForm(String name, String content, ApplicationStage stage, LocalDateTime fromDate) {
        this.name = name;
        this.content = content;
        this.stage = stage;
        this.fromDate = fromDate;
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

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public ApplicationStage getStage() {
        return stage;
    }

    public void setStage(ApplicationStage stage) {
        this.stage = stage;
    }

    public String getCause() {
        return cause;
    }

    public void setCause(String cause) {
        this.cause = cause;
    }

    public LocalDateTime getFromDate() {
        return fromDate;
    }

    public void setFromDate(LocalDateTime fromDate) {
        this.fromDate = fromDate;
    }

    public long getVersion() {
        return version;
    }

    public void setVersion(long version) {
        this.version = version;
    }

    @Override
    public String toString() {
        return "ApplicatinForm{" +
                "id=" + id +
                ", version=" + version +
                ", name='" + name + '\'' +
                ", stage=" + stage +
                ", fromDate=" + fromDate +
                '}';
    }
}
