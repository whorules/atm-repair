set search_path = public;

CREATE TABLE atm_repair
(
  id                bigserial     NOT NULL,
  case_id           bigint        NOT NULL,
  atm_id            bigint        NOT NULL,
  reason            text          NOT NULL,
  start_date        timestamp     NOT NULL,
  end_date          timestamp     NOT NULL,
  serial_number     varchar(45)   NOT NULL,
  bank_name         varchar(45)   NOT NULL,
  link              varchar(45)   NOT NULL
);