DROP TABLE IF EXISTS `t_engine_condition`;
CREATE TABLE `t_engine_condition` (
                                      `id` bigint(20) NOT NULL AUTO_INCREMENT,
                                      `code` varchar(255) NOT NULL,
                                      `description` varchar(255) NOT NULL,
                                      `left_type` tinyint(4) NOT NULL COMMENT '0: 常量 1: 上下文属性 2: 函数 3: 指标',
                                      `left_property` varchar(50) DEFAULT NULL,
                                      `left_value` varchar(255) DEFAULT NULL,
                                      `right_type` tinyint(4) NOT NULL COMMENT '0: 常量 1: 上下文属性 2: 函数 3: 指标',
                                      `right_property` varchar(255) DEFAULT NULL,
                                      `right_value` varchar(255) DEFAULT NULL,
                                      `comparator` varchar(50) NOT NULL,
                                      `relation_id` bigint(20) NOT NULL,
                                      `relation_type` tinyint(4) NOT NULL COMMENT '0: 规则 1: 函数 2: 指标 3: 条件组',
                                      `version` bigint(20) NOT NULL DEFAULT '0',
                                      `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
                                      `update_by` varchar(50) NOT NULL,
                                      `update_Time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                                      `create_by` varchar(50) NOT NULL
);
DROP TABLE IF EXISTS `t_engine_condition_group`;
CREATE TABLE `t_engine_condition_group` (
                                            `id` bigint(20) NOT NULL AUTO_INCREMENT,
                                            `code` varchar(255) NOT NULL,
                                            `description` varchar(255) NOT NULL,
                                            `logic` tinyint(4) NOT NULL COMMENT '0: or 1: and',
                                            `relation_id` bigint(20) NOT NULL,
                                            `relation_type` tinyint(4) NOT NULL COMMENT '0: 规则 1: 函数 2: 指标 3: 条件组',
                                            `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
                                            `update_by` varchar(50) NOT NULL DEFAULT '',
                                            `update_Time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                                            `create_by` varchar(50) NOT NULL,
                                            `version` bigint(20) NOT NULL DEFAULT '0'
);
DROP TABLE IF EXISTS `t_engine_event`;
CREATE TABLE `t_engine_event` (
                                  `id` bigint(20) NOT NULL AUTO_INCREMENT,
                                  `code` varchar(255) NOT NULL,
                                  `description` varchar(255) NOT NULL,
                                  `analysis` tinyint(4) NOT NULL COMMENT '0: no 1: yes',
                                  `accumulate` tinyint(4) NOT NULL COMMENT '0: or 1: and',
                                  `status` tinyint(4) NOT NULL COMMENT '0: 下线 1: 上线',
                                  `create_by` varchar(50) NOT NULL DEFAULT '',
                                  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
                                  `update_by` varchar(50) NOT NULL DEFAULT '',
                                  `update_Time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                                  `version` bigint(20) NOT NULL DEFAULT '0'
);
DROP TABLE IF EXISTS `t_engine_event_indicator_relation`;
CREATE TABLE `t_engine_event_indicator_relation` (
                                                     `id` bigint(20) NOT NULL AUTO_INCREMENT,
                                                     `event_id` bigint(20) NOT NULL,
                                                     `indicator_id` bigint(20) NOT NULL
);
DROP TABLE IF EXISTS `t_engine_event_rule_relation`;
CREATE TABLE `t_engine_event_rule_relation` (
                                                `id` bigint(20) NOT NULL AUTO_INCREMENT,
                                                `event_id` bigint(20) NOT NULL,
                                                `rule_id` bigint(20) NOT NULL
);
DROP TABLE IF EXISTS `t_engine_function`;
CREATE TABLE `t_engine_function` (
                                     `id` bigint(20) NOT NULL AUTO_INCREMENT,
                                     `code` varchar(255) NOT NULL,
                                     `description` varchar(255) NOT NULL,
                                     `return_type` tinyint(4) NOT NULL,
                                     `version` bigint(20) NOT NULL DEFAULT '0'
);
DROP TABLE IF EXISTS `t_engine_function_extend`;
CREATE TABLE `t_engine_function_extend` (
                                            `id` bigint(20) NOT NULL AUTO_INCREMENT,
                                            `code` varchar(255) NOT NULL,
                                            `description` varchar(255) NOT NULL,
                                            `logic` tinyint(4) NOT NULL COMMENT '0: or 1: and',
                                            `version` bigint(20) NOT NULL DEFAULT '0',
                                            `function_code` varchar(255) NOT NULL COMMENT '内置函数code',
                                            `params` varchar(255) NOT NULL COMMENT '函数自定义入参',
                                            `create_by` varchar(50) NOT NULL,
                                            `update_by` varchar(50) NOT NULL,
                                            `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
                                            `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);
DROP TABLE IF EXISTS `t_engine_indicator`;
CREATE TABLE `t_engine_indicator` (
                                      `id` bigint(20) NOT NULL AUTO_INCREMENT,
                                      `code` varchar(255) NOT NULL,
                                      `description` varchar(255) NOT NULL,
                                      `create_by` varchar(255) NOT NULL,
                                      `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
                                      `update_by` varchar(50) NOT NULL,
                                      `update_Time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                                      `version` bigint(20) NOT NULL DEFAULT '0',
                                      `logic` tinyint(4) NOT NULL COMMENT '0: or 1: and',
                                      `period` varchar(50) NOT NULL,
                                      `factor` varchar(50) DEFAULT NULL,
                                      `type` tinyint(4) NOT NULL COMMENT '0: 指标KEY拼接值
1: 唯一累积值
2: 累积时间范围
3: 单值累积值
',
                                      `dimension` varchar(255) DEFAULT NULL
);
DROP TABLE IF EXISTS `t_engine_punish`;
CREATE TABLE `t_engine_punish` (
                                   `id` bigint(20) NOT NULL AUTO_INCREMENT,
                                   `punish_code` varchar(50) NOT NULL,
                                   `punish_field` varchar(255) NOT NULL,
                                   `rule_id` bigint(20) NOT NULL,
                                   `punish_period` varchar(50) DEFAULT NULL,
                                   `roaster_id` bigint(20) DEFAULT NULL
);
DROP TABLE IF EXISTS `t_engine_return_message`;
CREATE TABLE `t_engine_return_message` (
                                           `id` bigint(20) NOT NULL AUTO_INCREMENT,
                                           `code` int(11) NOT NULL,
                                           `description` varchar(255) NOT NULL,
                                           `return_message` varchar(255) NOT NULL,
                                           `create_by` varchar(255) NOT NULL DEFAULT '',
                                           `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
                                           `update_by` varchar(50) NOT NULL DEFAULT '',
                                           `update_Time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                                           `version` bigint(20) NOT NULL DEFAULT '0'
);
DROP TABLE IF EXISTS `t_engine_roaster`;
CREATE TABLE `t_engine_roaster` (
                                    `id` int(12) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT 'id',
                                    `code` varchar(32) NOT NULL,
                                    `name` varchar(64) NOT NULL,
                                    `create_time` datetime NOT NULL,
                                    `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                                    `update_user` varchar(64) DEFAULT NULL,
                                    `expired_at` datetime DEFAULT NULL,
                                    `remark` varchar(255) DEFAULT NULL
);
DROP TABLE IF EXISTS `t_engine_roaster_list`;
CREATE TABLE `t_engine_roaster_list` (
                                         `id` int(12) UNSIGNED NOT NULL AUTO_INCREMENT,
                                         `value` varchar(64) NOT NULL,
                                         `roaster_id` int(12) UNSIGNED NOT NULL,
                                         `expired_at` datetime DEFAULT NULL,
                                         `create_time` datetime NOT NULL,
                                         `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                                         `remark` varchar(255) DEFAULT NULL,
                                         `update_user` varchar(64) DEFAULT NULL
);
DROP TABLE IF EXISTS `t_engine_rule`;
CREATE TABLE `t_engine_rule` (
                                 `id` bigint(20) NOT NULL AUTO_INCREMENT,
                                 `code` varchar(255) NOT NULL,
                                 `description` varchar(255) NOT NULL,
                                 `logic` tinyint(4) NOT NULL COMMENT '0: or 1: and',
                                 `status` tinyint(4) NOT NULL COMMENT '0: 下线 1: 上线',
                                 `create_by` varchar(50) NOT NULL DEFAULT '',
                                 `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
                                 `update_by` varchar(50) NOT NULL DEFAULT '',
                                 `update_Time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                                 `version` bigint(20) NOT NULL DEFAULT '0',
                                 `test` tinyint(4) NOT NULL DEFAULT '0',
                                 `return_message_id` bigint(20) DEFAULT NULL,
                                 `result` int(11) NOT NULL COMMENT '0: 通过 1001: 拒绝',
                                 `rule_group_id` bigint(20) DEFAULT NULL,
                                 `priority` int(11) NOT NULL DEFAULT '0'
);
DROP TABLE IF EXISTS `t_engine_rule_group`;
CREATE TABLE `t_engine_rule_group` (
                                       `id` bigint(20) NOT NULL AUTO_INCREMENT,
                                       `code` varchar(255) NOT NULL,
                                       `description` varchar(255) NOT NULL,
                                       `status` tinyint(4) NOT NULL,
                                       `create_by` varchar(50) NOT NULL DEFAULT '',
                                       `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
                                       `update_by` varchar(50) NOT NULL DEFAULT '',
                                       `update_Time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                                       `version` bigint(20) NOT NULL DEFAULT '0'
);
