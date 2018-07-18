CREATE TABLE t_datasource (c_datasrcid varchar(255) COLLATE utf8_bin NOT NULL, c_datasrcname varchar(255) COLLATE utf8_bin, c_datasrcalias varchar(255) COLLATE utf8_bin, c_user varchar(255) COLLATE utf8_bin, c_password varchar(255) COLLATE utf8_bin, c_drivertype varchar(255) COLLATE utf8_bin, c_driver varchar(255) COLLATE utf8_bin, c_url varchar(2048), c_maxconnum int, c_validation varchar(255) COLLATE utf8_bin, c_datasrcdesc varchar(255) COLLATE utf8_bin, c_dbcharset varchar(255) COLLATE utf8_bin, c_isolation int, PRIMARY KEY (c_datasrcid)) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE t_bizview (c_viewid varchar(255) COLLATE utf8_bin NOT NULL, c_datasrcid varchar(255) COLLATE utf8_bin, c_viewname varchar(255) COLLATE utf8_bin, c_viewalias varchar(255) COLLATE utf8_bin, c_viewdefine longtext, c_viewdesc varchar(255) COLLATE utf8_bin, PRIMARY KEY (c_viewid)) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE t_chart (c_chartid varchar(255) COLLATE utf8_bin NOT NULL, c_viewid varchar(255) COLLATE utf8_bin NOT NULL, c_chartname varchar(255) COLLATE utf8_bin NOT NULL, c_chartalias varchar(255) COLLATE utf8_bin, c_charttype varchar(255) COLLATE utf8_bin, c_chartdesc varchar(255) COLLATE utf8_bin, c_chartdefine longtext, PRIMARY KEY (c_chartid)) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE t_report (c_reportid varchar(255) COLLATE utf8_bin NOT NULL, c_reportname varchar(255) COLLATE utf8_bin, c_reportalias varchar(255) COLLATE utf8_bin, c_define longtext, c_reportdesc varchar(255) COLLATE utf8_bin, PRIMARY KEY (c_reportid)) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE t_table (c_tableid varchar(255) COLLATE utf8_bin NOT NULL, c_viewid varchar(255) COLLATE utf8_bin NOT NULL, c_tablename varchar(255) COLLATE utf8_bin, c_tablealias varchar(255) COLLATE utf8_bin, c_tabledesc varchar(255) COLLATE utf8_bin, PRIMARY KEY (c_tableid)) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE t_parameter 
(c_parmid varchar(255) COLLATE utf8_bin NOT NULL, 
c_parmname varchar(255) COLLATE utf8_bin, 
c_parmalias varchar(255) COLLATE utf8_bin, 
c_parmdesc varchar(255) COLLATE utf8_bin, 
c_parmdefine longtext, 
PRIMARY KEY (c_parmid)) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `bizview_columns` (
  `id` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `bizview_id` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL,
  `column_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL,
  `column_alias` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL,
  `column_type` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL,
  `groupby` tinyint(1) DEFAULT '0',
  `filterable` tinyint(1) DEFAULT '0',
  `count_distinct` tinyint(1) DEFAULT '0',
  `sum` tinyint(1) DEFAULT '0',
  `min` tinyint(1) DEFAULT '0',
  `max` tinyint(1) DEFAULT '0',
  `category` tinyint(1) DEFAULT '0',
  `expression` varchar(1024) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE DATABASE `demo_db` /*!40100 DEFAULT CHARACTER SET utf8 */;
CREATE TABLE `t_sina_news` (
  `id` varchar(255) NOT NULL,
  `ctime` datetime DEFAULT NULL,
  `title` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `t_price` (
  `cname` varchar(50) NOT NULL,
  `price` decimal(5,2) DEFAULT NULL,
  `update_time` date NOT NULL,
  PRIMARY KEY (`cname`,`update_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `t_department` (
  `depid` varchar(255) NOT NULL,
  `depname` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `t_fact_index` (
  `depid` varchar(255) NOT NULL,
  `indid` varchar(255) NOT NULL,
  `indname` varchar(255) NOT NULL,
  `indval` decimal(5,2) DEFAULT NULL,
  `upd_time` date NOT NULL,
  PRIMARY KEY (`depid`,`indid`,`upd_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

INSERT INTO `demo_db`.`t_department`
(`depid`,
`depname`)
VALUES
('bak001',
 '北京分行'),
 ('bak002',
 '深圳分行'),
('bak003',
 '上海分行');
 
INSERT INTO `demo_db`.`t_fact_index`
(`depid`,
`indid`,
`indname`,
`indval`,
`upd_time`)
VALUES
('bak001',
 'I001',
 '存款余额',
 '100203',
 '2018-01-01'),
 ('bak001',
 'I002',
 '贷款款余额',
 '110202',
 '2018-01-01'),
 ('bak002',
 'I001',
 '存款余额',
 '101232',
 '2018-01-01'),
 ('bak002',
 'I002',
 '贷款款余额',
 '111032',
 '2018-01-01'),
 ('bak003',
 'I001',
 '存款余额',
 '102032',
 '2018-01-01'),
 ('bak003',
 'I002',
 '贷款款余额',
 '112032',
 '2018-01-01')
 ;

