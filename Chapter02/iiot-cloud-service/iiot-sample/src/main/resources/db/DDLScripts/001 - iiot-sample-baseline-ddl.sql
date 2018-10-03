CREATE SCHEMA IF NOT EXISTS iiot_sample;
commit;



--SET search_path = iiot-sample;
CREATE TABLE alert_type
(
  name text NOT NULL,
  created_by text NOT NULL,
  created_date timestamp with time zone NOT NULL DEFAULT now(),
  updated_by text,
  updated_date timestamp with time zone NOT NULL DEFAULT now(),
  id serial NOT NULL,
  CONSTRAINT entity_type_pkey PRIMARY KEY (id),
  CONSTRAINT uniqueEntityTypes UNIQUE (name)
);



CREATE TABLE alerts
(
  id bigserial NOT NULL,
  alerts_uuid text NOT NULL,
  severity integer,
  alert_name text,
  alert_info text,
  created_by text NOT NULL,
  created_date timestamp with time zone NOT NULL DEFAULT now(),
  updated_by text,
  updated_date timestamp with time zone,
  tenant_uuid text NOT NULL,
  CONSTRAINT alerts_pkey PRIMARY KEY (id)
);


CREATE UNIQUE INDEX ALERTS_TENANT_IDX ON Alerts(alerts_uuid, tenant_uuid);



