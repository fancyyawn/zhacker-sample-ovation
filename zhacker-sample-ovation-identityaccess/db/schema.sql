

CREATE TABLE IF NOT EXISTS `tbl_es_event_store` (
  `event_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `event_body` text NOT NULL,
  `event_type` varchar(250) NOT NULL,
  `stream_name` varchar(250) NOT NULL,
  `stream_version` int(11) NOT NULL,
  PRIMARY KEY (`event_id`),
  UNIQUE KEY `stream_name_2` (`stream_name`,`stream_version`),
  KEY `stream_name` (`stream_name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;



# Dump of table tbl_group
# ------------------------------------------------------------

CREATE TABLE  IF NOT EXISTS `tbl_group` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `description` varchar(250) NOT NULL,
  `name` varchar(100) NOT NULL,
  `tenant_id_id` varchar(36) NOT NULL,
  `concurrency_version` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `k_tenant_id_name` (`name`,`tenant_id_id`),
  KEY `k_tenant_id_id` (`tenant_id_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;



# Dump of table tbl_group_member
# ------------------------------------------------------------

CREATE TABLE IF NOT EXISTS  `tbl_group_member` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(100) NOT NULL,
  `tenant_id_id` varchar(36) NOT NULL,
  `type` varchar(5) NOT NULL,
  `group_id` bigint(20) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `k_group_id` (`group_id`),
  KEY `k_tenant_id_id` (`tenant_id_id`),
  CONSTRAINT `fk_tbl_group_member_tbl_group` FOREIGN KEY (`group_id`) REFERENCES `tbl_group` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;



# Dump of table tbl_person
# ------------------------------------------------------------

CREATE TABLE IF NOT EXISTS  `tbl_person` (
  `id` bigint(20) NOT NULL,
  `contact_information_email_address_address` varchar(100) NOT NULL,
  `contact_information_postal_address_city` varchar(100) NOT NULL,
  `contact_information_postal_address_country_code` varchar(2) NOT NULL,
  `contact_information_postal_address_postal_code` varchar(12) NOT NULL,
  `contact_information_postal_address_state_province` varchar(100) NOT NULL,
  `contact_information_postal_address_street_address` varchar(100) DEFAULT NULL,
  `contact_information_primary_telephone_number` varchar(20) NOT NULL,
  `contact_information_secondary_telephone_number` varchar(20) NOT NULL,
  `name_first_name` varchar(50) NOT NULL,
  `name_last_name` varchar(50) NOT NULL,
  `tenant_id_id` varchar(36) NOT NULL,
  `concurrency_version` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `k_tenant_id_id` (`tenant_id_id`),
  CONSTRAINT `fk_tbl_person_tbl_user` FOREIGN KEY (`id`) REFERENCES `tbl_user` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;



# Dump of table tbl_published_notification_tracker
# ------------------------------------------------------------

CREATE TABLE IF NOT EXISTS  `tbl_published_notification_tracker` (
  `published_notification_tracker_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `most_recent_published_notification_id` bigint(20) NOT NULL,
  `type_name` varchar(100) NOT NULL,
  `concurrency_version` int(11) NOT NULL,
  PRIMARY KEY (`published_notification_tracker_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;



# Dump of table tbl_registration_invitation
# ------------------------------------------------------------

CREATE TABLE  IF NOT EXISTS `tbl_registration_invitation` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `description` varchar(100) NOT NULL,
  `invitation_id` varchar(36) NOT NULL,
  `starting_on` datetime DEFAULT NULL,
  `tenant_id_id` varchar(36) NOT NULL,
  `until` datetime DEFAULT NULL,
  `tenant_id` bigint(20) NOT NULL,
  `concurrency_version` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `k_invitation_id` (`invitation_id`),
  KEY `k_tenant_id` (`tenant_id`),
  KEY `k_tenant_id_id` (`tenant_id_id`),
  CONSTRAINT `fk_tbl_registration_invitation_tbl_tenant` FOREIGN KEY (`tenant_id`) REFERENCES `tbl_tenant` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;



# Dump of table tbl_role
# ------------------------------------------------------------

CREATE TABLE IF NOT EXISTS  `tbl_role` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `description` varchar(250) NOT NULL,
  `group_id` bigint(20) NOT NULL,
  `name` varchar(100) NOT NULL,
  `supports_nesting` tinyint(1) NOT NULL,
  `tenant_id_id` varchar(36) NOT NULL,
  `concurrency_version` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `k_tenant_id_name` (`name`,`tenant_id_id`),
  KEY `k_tenant_id_id` (`tenant_id_id`),
  KEY `fk_tbl_role_tbl_group` (`group_id`),
  CONSTRAINT `fk_tbl_role_tbl_group` FOREIGN KEY (`group_id`) REFERENCES `tbl_group` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;



# Dump of table tbl_stored_event
# ------------------------------------------------------------

CREATE TABLE IF NOT EXISTS  `tbl_stored_event` (
  `event_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `event_body` text NOT NULL,
  `occurred_on` datetime NOT NULL,
  `type_name` varchar(200) NOT NULL,
  PRIMARY KEY (`event_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;



# Dump of table tbl_tenant
# ------------------------------------------------------------

CREATE TABLE IF NOT EXISTS  `tbl_tenant` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `active` tinyint(1) NOT NULL,
  `description` varchar(100) DEFAULT NULL,
  `name` varchar(100) NOT NULL,
  `tenant_id_id` varchar(36) NOT NULL,
  `concurrency_version` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `k_name` (`name`),
  UNIQUE KEY `k_tenant_id_id` (`tenant_id_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;



# Dump of table tbl_time_constrained_process_tracker
# ------------------------------------------------------------

CREATE TABLE IF NOT EXISTS  `tbl_time_constrained_process_tracker` (
  `time_constrained_process_tracker_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `allowable_duration` bigint(20) NOT NULL,
  `completed` tinyint(1) NOT NULL,
  `description` varchar(100) NOT NULL,
  `process_id_id` varchar(36) NOT NULL,
  `process_informed_of_timeout` tinyint(1) NOT NULL,
  `process_timed_out_event_type` varchar(200) NOT NULL,
  `retry_count` int(11) NOT NULL,
  `tenant_id` varchar(36) NOT NULL,
  `timeout_occurs_on` bigint(20) NOT NULL,
  `total_retries_permitted` bigint(20) NOT NULL,
  `concurrency_version` int(11) NOT NULL,
  PRIMARY KEY (`time_constrained_process_tracker_id`),
  KEY `k_process_id` (`process_id_id`),
  KEY `k_tenant_id` (`tenant_id`),
  KEY `k_timeout_occurs_on` (`timeout_occurs_on`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;



# Dump of table tbl_user
# ------------------------------------------------------------

CREATE TABLE IF NOT EXISTS  `tbl_user` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `enablement_enabled` tinyint(1) NOT NULL,
  `enablement_end_date` datetime DEFAULT NULL,
  `enablement_start_date` datetime DEFAULT NULL,
  `password` varchar(32) NOT NULL,
  `tenant_id_id` varchar(36) NOT NULL,
  `username` varchar(250) NOT NULL,
  `concurrency_version` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `k_tenant_id_username` (`tenant_id_id`,`username`),
  KEY `k_tenant_id_id` (`tenant_id_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
