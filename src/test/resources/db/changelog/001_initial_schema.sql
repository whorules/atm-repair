set search_path = public;

CREATE TABLE atm_repair
(
  case_id           bigserial     NOT NULL,
  atm_id            text          NOT NULL,
  reason            text          NOT NULL,
  start_date        timestamp     NOT NULL,
  end_date          timestamp     NOT NULL,
  serial_number     text          NOT NULL,
  bank_name         text          NOT NULL,
  channel           text          NOT NULL
);