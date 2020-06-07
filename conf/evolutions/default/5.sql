# --- !Ups

alter table `documents` change `name` `object_name` VARCHAR(256);
alter table `documents` add column `engine_number` INT;
alter table `documents` add column `object_engine_number` INT;
alter table `documents` add column `lang` VARCHAR(16);
alter table `documents` add column `revision` INT;
alter table `documents` drop index `unique_name`;

# --- !Downs
alter table `documents` drop column `engine_number`;
alter table `documents` change `object_name` `name` VARCHAR(256);
alter table `documents` drop column `object_engine_number`;
alter table `documents` drop column `lang`;
alter table `documents` drop column `revision`;
alter table `documents` add constraint UNIQUE `unique_name` (`name`);