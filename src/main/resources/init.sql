-- Table: application_form

-- DROP TABLE application_form;

CREATE TABLE application_form
(
  id bigint NOT NULL,
  cause character varying(255),
  content character varying(500) NOT NULL,
  created_date timestamp without time zone,
  last_modified_date timestamp without time zone,
  name character varying(255) NOT NULL,
  status character varying(255) NOT NULL,
  version bigint NOT NULL,
  CONSTRAINT application_form_pkey PRIMARY KEY (id)
)
WITH (
  OIDS=FALSE
);
ALTER TABLE application_form
  OWNER TO postgres;
  
-- Table: application_form_audit

-- DROP TABLE application_form_audit;

CREATE TABLE application_form_audit
(
  id bigint NOT NULL,
  cause character varying(255),
  content character varying(500) NOT NULL,
  created_date timestamp without time zone,
  last_modified_date timestamp without time zone,
  name character varying(255) NOT NULL,
  status character varying(255) NOT NULL,
  application_form_id bigint,
  version bigint NOT NULL,
  CONSTRAINT application_form_audit_pkey PRIMARY KEY (id)
)
WITH (
  OIDS=FALSE
);
ALTER TABLE application_form_audit
  OWNER TO postgres;
