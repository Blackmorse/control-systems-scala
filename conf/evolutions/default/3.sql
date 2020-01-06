# --- !Ups

create table `documents` (
    `id` INT NOT NULL AUTO_INCREMENT,
    `number` INT,
    `name` VARCHAR(256),
    `document_date` text,

    CONSTRAINT
      PRIMARY KEY(`id`),
      UNIQUE KEY `unique_name` (`name`)
)


# --- !Downs
drop table `documents`