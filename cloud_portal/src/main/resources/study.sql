SET NAMES utf8mb4;

-- ----------------------------
-- Table structure for t_student
-- ----------------------------
DROP TABLE IF EXISTS `t_student`;
CREATE TABLE `t_student`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '用户ID ',
  `name` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '姓名',
  `avg_score` decimal(2, 1) NOT NULL DEFAULT 0.0 COMMENT '能力平均分数？星',
  `created_time` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_time` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 4 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '学生信息表 ：学生的详细信息' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of t_student
-- ----------------------------
INSERT INTO `t_student` VALUES (1, 'zhangsan', 8.8, '2022-05-18 18:04:34', '2022-05-18 18:04:34');
INSERT INTO `t_student` VALUES (2, 'lisi', 6.6, '2022-05-18 18:04:48', '2022-05-18 18:04:48');
INSERT INTO `t_student` VALUES (3, 'wangwu', 7.7, '2022-05-18 18:04:57', '2022-05-18 18:04:57');

-- ----------------------------
-- Table structure for t_student_teacher_relation
-- ----------------------------
DROP TABLE IF EXISTS `t_student_teacher_relation`;
CREATE TABLE `t_student_teacher_relation`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `stu_id` bigint(20) NOT NULL,
  `tea_id` bigint(20) NOT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 4 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '关系表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of t_student_teacher_relation
-- ----------------------------
INSERT INTO `t_student_teacher_relation` VALUES (1, 1, 1);
INSERT INTO `t_student_teacher_relation` VALUES (2, 1, 2);
INSERT INTO `t_student_teacher_relation` VALUES (3, 2, 2);

-- ----------------------------
-- Table structure for t_teacher
-- ----------------------------
DROP TABLE IF EXISTS `t_teacher`;
CREATE TABLE `t_teacher`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '用户ID ',
  `name` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '姓名',
  `work_year` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '工作年限',
  `daily_salary` decimal(10, 0) NOT NULL DEFAULT 0 COMMENT '日薪',
  `created_time` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_time` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 3 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '老师信息表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of t_teacher
-- ----------------------------
INSERT INTO `t_teacher` VALUES (1, 'tom', '3', 1888, '2022-05-18 18:05:15', '2022-05-18 18:05:15');
INSERT INTO `t_teacher` VALUES (2, 'erica', '6', 2689, '2022-05-18 18:05:35', '2022-05-18 18:05:35');
