INSERT INTO `t_engine_function` (`id`, `code`, `description`, `return_type`, `version`) VALUES (599, 'getValueByJsonPath', '通过JSONPATH获取上下文中的值', 5, 0);
INSERT INTO `t_engine_function` (`id`, `code`, `description`, `return_type`, `version`) VALUES (600, 'roastFunction', '名单函数', 3, 0);

INSERT INTO `t_engine_function_extend` (`id`, `code`, `description`, `logic`, `version`, `function_code`, `params`, `create_by`, `update_by`, `create_time`, `update_time`) VALUES (16, 'test2', 'tt', 1, 3, 'getValueByJsonPath', '$.userId', 'admin', 'admin', '2022-11-15 18:09:12', '2022-11-15 18:09:12');
INSERT INTO `t_engine_function_extend` (`id`, `code`, `description`, `logic`, `version`, `function_code`, `params`, `create_by`, `update_by`, `create_time`, `update_time`) VALUES (17, 'mobileBlackList', 'mobileBlackList', 1, 2, 'roastFunction', '2', 'admin', 'admin', '2022-11-21 19:45:57', '2022-11-21 19:45:57');

INSERT INTO `t_engine_return_message` (`id`, `code`, `description`, `return_message`, `create_by`, `create_time`, `update_by`, `update_Time`, `version`) VALUES (34, -1001, '1', '1', 'admin', '2022-11-04 13:37:39', '', '2022-11-04 15:54:16', 0);

