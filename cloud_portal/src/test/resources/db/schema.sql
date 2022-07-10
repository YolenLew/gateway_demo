SET NAMES utf8;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for t_teacher
-- ----------------------------
DROP TABLE IF EXISTS `t_teacher`;
CREATE TABLE `t_teacher`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '用户ID ',
  `name` varchar(32)  NOT NULL DEFAULT '' COMMENT '姓名',
  `work_year` varchar(32)  NOT NULL DEFAULT '' COMMENT '工作年限',
  `daily_salary` decimal(10, 0) NOT NULL DEFAULT 0 COMMENT '日薪',
  `created_time` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_time` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ;

-- ----------------------------
-- Table structure for t_student
-- ----------------------------
DROP TABLE IF EXISTS `t_student`;
CREATE TABLE `t_student`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '用户ID ',
  `name` varchar(32)  NOT NULL DEFAULT '' COMMENT '姓名',
  `avg_score` decimal(2, 1) NOT NULL DEFAULT 0.0 COMMENT '能力平均分数？星',
  `created_time` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_time` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '更新时间',
  `role_type` tinyint(4) NULL DEFAULT NULL COMMENT '角色类型',
  `role_range` varchar(255)  NULL DEFAULT NULL COMMENT '角色技能',
  PRIMARY KEY (`id`)
);

-- ----------------------------
-- Table structure for t_student_teacher_relation
-- ----------------------------
DROP TABLE IF EXISTS `t_student_teacher_relation`;
CREATE TABLE `t_student_teacher_relation`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `stu_id` bigint(20) NOT NULL,
  `tea_id` bigint(20) NOT NULL,
  PRIMARY KEY (`id`)
);

SET FOREIGN_KEY_CHECKS = 1;
