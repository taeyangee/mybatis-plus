# Q
- mbp的wrapper很强大了，都有哪些分类、用于什么常见
- 复杂条件之间是如何组织的
- 如何结合泛型做实现

# wrapper的分类与使用
- 按功能分类：update/query
- 形式分类：普通、lambda
[crud-interface](https://mybatis.plus/guide/crud-interface.html)
[crud-interface](https://mybatis.plus/guide/wrapper.html#abstractwrapper)
- 相关测试用例：com.baomidou.mybatisplus.core.conditions


# 源码分析
package com.baomidou.mybatisplus.core.conditions.interfaces;
- Wrapper 接口：定义了各种查询条件接口
- Func： sql中关于 函数 的抽象
- Compare：sql中关于 比较 的抽象
- Nested：sql中关于 嵌套 的抽象


