# 校园选课系统（单体版）项目文档

## 一、项目说明

### 1. 项目背景与目标

校园选课系统（单体版）是学期贯穿项目的初始阶段，旨在构建一个功能完整的单体架构课程管理与选课平台。通过该项目，开发者可掌握 Spring Boot 开发流程、RESTful API 设计原则及完整 CRUD 操作实现，为后续扩展为微服务架构奠定基础。

### 2. 核心功能模块

项目包含三大核心功能模块，覆盖课程管理、学生管理及选课业务全流程：

- **课程管理模块**：提供课程的查询（所有 / 单个）、创建、更新、删除功能，支持课程基础信息（编码、名称、教师、上课时间、容量）的维护。
- **学生管理模块**：实现学生信息的全生命周期管理，包括自动生成 UUID 作为唯一标识、学号唯一性校验、邮箱格式验证，以及基于选课记录的删除限制。
- **选课管理模块**：处理学生选课 / 退课操作，内置课程容量限制、重复选课检查、课程 / 学生存在性验证等业务规则，确保选课数据一致性。

### 3. 技术栈与架构

- **核心框架**：Spring Boot 3.4.x（提供快速开发支持与自动配置）
- **Web 组件**：Spring Boot Starter Web（实现 HTTP 接口与 RESTful 服务）
- **数据校验**：Spring Boot Starter Validation（验证请求参数格式与完整性）
- **数据存储**：内存存储（ConcurrentHashMap），无需数据库，简化开发与测试流程
- **架构模式**：分层架构（Controller→Service→Repository），职责分明，便于维护

### 4. 统一规范

- **响应格式**：所有 API 返回 JSON 格式数据，包含`code`（状态码）、`message`（提示信息）、`data`（业务数据）三部分，成功与错误场景格式统一。
- **HTTP 状态码**：遵循标准语义，如 201（创建成功）、400（参数错误）、404（资源不存在）、204（删除成功）。
- **包结构**：严格按照`com.zjsu.djy.course`命名空间组织，分为`model`（实体类）、`repository`（数据访问）、`service`（业务逻辑）、`controller`（接口层）四层。

## 二、如何运行项目

### 1. 环境准备

- **JDK 版本**：JDK 17 
- **构建工具**：Maven 3.6
- **开发工具**：IntelliJ IDEA、Eclipse 等（支持 Spring Boot 项目的 IDE）
- **测试工具**：Apifox

### 2. 项目获取与导入

1. 从 Git 仓库拉取：克隆项目仓库到本地，命令如下：

   ```bash
   git clone https://github.com/NIIIIIIIka/microService
   ```

   

2. IDE 导入：

   - 打开 IDE，选择 “Import Project”，选中项目根目录下的`pom.xml`文件。
   - 选择 Maven 自动导入，等待依赖下载完成（确保网络通畅，依赖无缺失）。

### 4. 启动项目

#### 方式 1：IDE 启动

1. 找到项目启动类`CourseApplication.java`（位于`com.zjsu.<姓名缩写>.course`包下）。
2. 右键点击该类，选择 “Run CourseApplication” 或 “Debug CourseApplication”。
3. 启动成功验证：终端输出 “Started CourseApplication in XX seconds”，无报错信息。

## 三、API 接口列表

### 1. 课程管理 API

| 接口功能     | HTTP 方法 | 接口路径          | 请求体 / 参数                         | 响应说明                                   |
| ------------ | --------- | ----------------- | ------------------------------------- | ------------------------------------------ |
| 查询所有课程 | GET       | /api/courses      | 无参数                                | 200 OK，`data`为课程列表（空列表或多课程） |
| 查询单个课程 | GET       | /api/courses/{id} | 路径参数`id`（课程 UUID）             | 200 OK（存在）/404 Not Found（不存在）     |
| 创建课程     | POST      | /api/courses      | JSON（含 code、title、instructor 等） | 201 Created，`data`为创建的课程信息        |
| 更新课程     | PUT       | /api/courses/{id} | 路径参数`id` + JSON（待更新字段）     | 200 OK（成功）/404 Not Found（不存在）     |
| 删除课程     | DELETE    | /api/courses/{id} | 路径参数`id`（课程 UUID）             | 204 No Content（成功）/404 Not Found       |

**创建课程请求体示例**：

```json
{
  "code": "CS101",
  "title": "计算机科学导论",
  "instructor": {
    "id": "T001",
    "name": "张教授",
    "email": "zhang@example.edu.cn"
  },
  "schedule": {
    "dayOfWeek": "MONDAY",
    "startTime": "08:00",
    "endTime": "10:00",
    "expectedAttendance": 50
  },
  "capacity": 60
}
```

### 2. 学生管理 API

| 接口功能     | HTTP 方法 | 接口路径           | 请求体 / 参数                        | 响应说明                                                   |
| ------------ | --------- | ------------------ | ------------------------------------ | ---------------------------------------------------------- |
| 创建学生     | POST      | /api/students      | JSON（含 studentId、name、major 等） | 201 Created（成功）/400 Bad Request（参数错误）            |
| 查询所有学生 | GET       | /api/students      | 无参数                               | 200 OK，`data`为学生列表（空列表或多学生）                 |
| 查询单个学生 | GET       | /api/students/{id} | 路径参数`id`（学生 UUID）            | 200 OK（存在）/404 Not Found（不存在）                     |
| 更新学生信息 | PUT       | /api/students/{id} | 路径参数`id` + JSON（待更新字段）    | 200 OK（成功）/404 Not Found（不存在）                     |
| 删除学生     | DELETE    | /api/students/{id} | 路径参数`id`（学生 UUID）            | 204 No Content（无选课记录）/400 Bad Request（有选课记录） |

**创建学生请求体示例**：

```json
{
  "studentId": "2024001",
  "name": "李小明",
  "major": "计算机科学与技术",
  "grade": 2024,
  "email": "liming@example.edu.cn"
}
```

### 3. 选课管理 API

| 接口功能         | HTTP 方法 | 接口路径                             | 请求体 / 参数                   | 响应说明                                                 |
| ---------------- | --------- | ------------------------------------ | ------------------------------- | -------------------------------------------------------- |
| 学生选课         | POST      | /api/enrollments                     | JSON（含 courseId、studentId）  | 201 Created（成功）/400 Bad Request（容量满 / 重复选课） |
| 学生退课         | DELETE    | /api/enrollments/{id}                | 路径参数`id`（选课记录 UUID）   | 204 No Content（成功）/404 Not Found                     |
| 查询所有选课记录 | GET       | /api/enrollments                     | 无参数                          | 200 OK，`data`为选课记录列表                             |
| 按课程查询选课   | GET       | /api/enrollments/course/{courseId}   | 路径参数`courseId`（课程 UUID） | 200 OK，`data`为该课程的选课记录                         |
| 按学生查询选课   | GET       | /api/enrollments/student/{studentId} | 路径参数`studentId`（学生学号） | 200 OK，`data`为该学生的选课记录                         |

**学生选课请求体示例**：

```json
{
  "courseId": "课程UUID",
  "studentId": "2024001"
}
```

## 四、测试

- 见另一份文档“默认模块.md”
