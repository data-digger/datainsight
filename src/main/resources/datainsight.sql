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
c_paramtype varchar(255) COLLATE utf8_bin,
c_controltype varchar(255) COLLATE utf8_bin,
c_parmdefine longtext, 
PRIMARY KEY (c_parmid)) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE DATABASE `demo_db` /*!40100 DEFAULT CHARACTER SET utf8 */;
CREATE TABLE `t_sina_news` (
  `id` varchar(255) NOT NULL,
  `ctime` datetime DEFAULT NULL,
  `title` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
