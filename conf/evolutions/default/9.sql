# --- !Ups

update `parameters` set `unit` = '' where `unit` is null;
update `parameters` set `default_value` = '' where `default_value` is null;

# --- !Downs