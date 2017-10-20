CREATE TABLE timeseries (
  id bigint,
  date timestamp,
  manual boolean,
  value double,
  PRIMARY KEY (id, date)
) WITH CLUSTERING ORDER BY (date DESC);
