# LitterSortSystem

Since the implementation of waste sorting policies in 2019, starting from the 46 pilot cities, waste sorting has become an important requirement in urban life. Waste sorting contributes to the cleanliness and convenience of our daily lives, as well as the efficient utilization of recycled materials. However, for many people, identifying and correctly disposing of different types of waste can be challenging. People living in large cities often have their time and energy squeezed by daily life, leaving them with little time or inclination to systematically learn the specialized knowledge of waste sorting.

Traditional manual waste sorting techniques suffer from low efficiency, long processing times, heavy reliance on manpower, and poor working conditions. Therefore, finding effective ways to automate waste sorting has become an urgent issue.

## 项目背景

随着全球城市化进程的不断加速，垃圾处理成为了一个日益严峻的问题。传统的垃圾处理方法已经不能满足人们对环境保护和资源回收利用的需求。因此，开发一套智能化、高效率的垃圾分类系统变得尤为重要。

## 项目目标

该项目旨在实现以下目标：

- 开发一套智能化的垃圾分类系统，能够自动识别垃圾类别。
- 结合树莓派硬件，实现垃圾分类的实时监测和数据采集。
- 利用 Spring Cloud 构建系统的微服务架构，实现系统的高可扩展性和高可用性。
- 运用机器学习技术，提高垃圾分类的准确性和效率。
- 将系统部署到实际场景中，为城市管理部门和居民提供垃圾分类解决方案。

## 技术栈

### 前端

- **Vue.js**: 前端框架，负责用户界面的展示和交互。
- **Element UI**: 前端 UI 组件库，提供丰富的组件和样式。

### 后端

- **Spring Boot**: 后端框架，负责系统的业务逻辑和数据处理。
- **Spring Cloud**: 微服务框架，用于构建分布式系统和服务治理。
- **Mybatis Plus**: 数据访问框架，简化对数据库的操作。

### 数据库

- **MySQL**: 关系型数据库，用于存储用户信息、垃圾分类数据等。

### 机器学习

- **Pytorch**: 机器学习框架，用于训练垃圾分类模型。
- **Resnet18**: 基于卷积神经网络实现，提取图像特征
- **随机森林**: 分类器，实现图像分类

### 树莓派

- **Raspberry Pi OS**: 基于 Linux 的操作系统，用于运行垃圾分类系统。
- **Python**: 编程语言，用于编写树莓派端的数据采集和识别程序。

## 如何使用

1. 克隆项目到本地。
2. 部署树莓派端程序，并确保树莓派与后端服务的通信正常。
3. 训练垃圾分类模型，并将模型集成到系统中。
4. 测试系统的功能和性能，并根据需要进行调优和优化。
