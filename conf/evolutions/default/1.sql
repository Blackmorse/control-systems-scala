# --- !Ups

create table `parameters` (
    `id` INT NOT NULL,
    `name` text,
    `unit` text,
    `default_value` text,

    CONSTRAINT
      PRIMARY KEY(`id`)
)

# --- !Downs

drop table `parameters`