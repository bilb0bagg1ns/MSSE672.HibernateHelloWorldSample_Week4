DROP TABLE IF EXISTS `regis`.`greetings`;

DROP TABLE IF EXISTS `regis`.`greetings`;
CREATE TABLE  `regis`.`greetings` (
  `GREETING_ID` int(10) unsigned NOT NULL auto_increment,
  `GREETING_TEXT` varchar(255) default NULL,
  `CREATED_DATE` timestamp NOT NULL default CURRENT_TIMESTAMP,
  PRIMARY KEY  (`GREETING_ID`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;