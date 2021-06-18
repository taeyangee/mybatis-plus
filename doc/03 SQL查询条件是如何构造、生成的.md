# Q
- mbp的wrapper很强大了，都有哪些分类、用于什么常见: update/query, lambda形式的update/query
- 复杂条件之间是如何组织、翻译成SQL的：围绕ISqlSegment元素，利用组合模式与解释器模式，完成条件拼接与解析输出
- wrapper翻译出的SQL是如何mb的mappedStatment使用的： TODO,可能要对照[02#AbstractMethod](02%20BaseMapper是如何变成sql的.md)看看

# wrapper的分类与使用
- 按功能分类：update/query
- 形式分类：普通、lambda， lambda有编译检查的优势，
- 官方文档
    - [crud-interface](https://mybatis.plus/guide/crud-interface.html)
    - [abstractwrapper](https://mybatis.plus/guide/wrapper.html#abstractwrapper)
- 相关测试用例：QueryWrapperTest.java UpdateWrapperTest.java

# 源码 - 包主体结构
条件构造的功能由`package com.baomidou.mybatisplus.core.conditions`提供支持， 由如下几个部分构成
- Wrapper体系：用户层代码的入口，通过实例化Wrapper完成SQL查询条件构造，Wrapper.java/AbstractLambdaWrapper.java/AbstractWrapper.java
- interfaces包：是对SQL查询条件的语法抽象，由Wrapper负责实现，包括以下几种抽象
    - Func： sql中关于`函数`的抽象
    - Compare：sql中关于`比较语法`的抽象，比如`>,<,!=,==`之类的
    - Nested：sql中关于`嵌套语法`的抽象,比如`or/and`之类的
    - Join：sql中关于`简单拼接`的抽象,比如`or/and`之类的
- ISqlSegment接口：SQL片段，是整个conditions包的核心概念。一个sql拆解若干SQL片组成， Wrapper内部通过维护一堆ISqlSegment，完成SQL查询条件的构造、解析、sql语句输出
- segments包：是对ISqlSegment的各种实现，mbp将SQL片段主要分为以下几种, 同时给Wrapper提供MergeSegments作为ISqlSegment的管理入口
    - GroupBySegmentList： 负责管理SQL的group语句，解决多个group子句的拼接、语法冲突解决
    - HavingSegmentList： 负责管理SQL的having语句，效果类似
    - OrderBySegmentList： 负责管理SQL的order语句，效果类似
    - NormalSegmentList： 负责管理SQL的其他常规的查询条件语句，效果类似
- query包： 继承Wrapper，实现QueryWrapper、LambdaQueryWrapper
- update包： 继承Wrapper，实现LambdaUpdateWrapper、UpdateWrapper

# 源码 - ISqlSegment
功能：ISqlSegment整个conditions包的核心抽象，一个sql可以拆解为若干SQL片段，Wrapper体系能实现查询条件构造，其实现都是围绕若干ISqlSegment做添加、解析、拼接，
概述
- [ISqlSegment体系图](pic/03-ISqlSegment.uml)
- ISqlSegment是一个函数式接口， 只定义了getSqlSegment方法
- AbstractWrapper、AbstractISegmentList、MergeSegments、SqlKeyword都实现了该接口，使得组件之间可以通过组合模式、解释器模式实现了复杂语句的组织

# 源码 - Wrapper体系
Wrapper:用户代码构造查询条件的入口，[AbstractWrapper体系图](pic/03-AbstractWrapper.uml)
## Wrapper<T>
- 功能：条件构造抽象类
- 概述
    - 定义对MergeSegments的判定方法，要求子类要提供MergeSegments
    - 定义getSqlXXX，更像是给测试用的
    - 实现了ISqlSegment接口： 复杂条件的构造是通过组合模式、解释器模式实现的， Wrapper实现ISqlSegment是组合模式实现的基础
## AbstractWrapper<T, R, Children extends AbstractWrapper<T, R, Children>>
- 功能：查询条件抽象类
- 概述
    - 继承了 Wrapper
    - 实现了 conditions.interfaces中的各种查询接口
- 关键函数：
    - addCondition + appendSqlSegments: 常规查询条件SQL拼接
    - addNestedCondition + appendSqlSegments： 嵌套查询条件SQL拼接
    - likeValue + appendSqlSegments ：相比addNestedCondition，只是多了SqlUtils.concatLike(val, sqlLike)
    - appendSqlSegments:调用`MergeSegments#add`完成ISqlSegment插入
    - getSqlSegment: 调用`MergeSegments#getSqlSegment`完成ISqlSegment的逐个、逐级(如果有嵌套的话)解析
    - instance：由子类实现，用于创建同父类同类型的wrapper，这个instance处理完作为ISqlSegment加入到父类的ISqlSegment数组中，实现组合模式的拼接效果
## interfaces包
interfaces包：是对SQL查询条件的语法抽象，由Wrapper负责实现，包括以下几种抽象
- Func： sql中关于`函数`的抽象
- Compare：sql中关于`比较语法`的抽象，比如`>,<,!=,==`之类的
- Nested：sql中关于`嵌套语法`的抽象,比如`or/and`之类的
- Join：sql中关于`简单拼接`的抽象,比如`or/and`之类的
## QueryWrapper
- 功能：基于plain text定位column的wrapper
- 概述：
    - 继承AbstractWrapper，里头实现了sql查询条件的构造与解析
    - 实现Query接口，里头定义了sql set的语法抽象
## UpdateWrapper
- 功能：基于plain text定位column的update wrapper
- 概述：
    - 继承AbstractWrapper，里头实现了sql查询条件的构造与解析
    - 实现Update接口，里头定义了sql set的语法抽象

# 源码 - segments体系
- [AbstractISegmentList体系](pic/03-ISqlSegment.uml)
## MergeSegments
- 功能： 一个桥接类，对AbstractWrapper提供Segment管理功能
- 关键实现：
    - add(ISqlSegment... iSqlSegments)：将Wrapper#add传入的iSqlSegments，按分类委托给不同的AbstractISegmentList子类进行管理
## AbstractISegmentList
- 功能：SQL片段的抽象类
- 概述
    - 实现了ISqlSegment接口，同时也是ISqlSegment容器（为组合模型奠定基础）
    - mbp将SQL片段主要分为以下几种（）
        - GroupBySegmentList： 负责管理SQL的group语句，解决多个group子句的拼接、语法冲突解决
        - HavingSegmentList： 负责管理SQL的having语句，效果类似
        - OrderBySegmentList： 负责管理SQL的order语句，效果类似
        - NormalSegmentList： 负责管理SQL的其他常规的查询条件语句，效果类似
- 关键实现
    - addAll：对外，向容器添加ISqlSegment
    - transformList：被addAll调用，由子类实现，对待添加的ISqlSegment做判断、处理
    - getSqlSegment：对外，将所有ISqlSegment解析成SQL
    - childrenSqlSegment：被getSqlSegment调用， 由子类实现，对不同类型的ISqlSegment数组，完成SQL语句解析
# GroupBySegmentList
- 功能：管理所有Group SQL片段
- 概述： 继承AbstractISegmentList
- 关键实现：
    - transformList：每次List<ISqlSegment>要添加时，把打头代表`group by`的SQL ISqlSegment砍掉
    - childrenSqlSegment：取出ISqlSegment列表，在头部添加了`GROUP_BY` Seg
# HavingSegmentList/MergeSegments/OrderBySegmentList/NormalSegmentList
- 都差不多，只不过根据SQL语法，对Segment做了自动添加、删除的动作（比如补齐and、or，删除多余or）

# 源码 - LambdaWrapper体系
整体和Wrapper体系差不多，只是在Column的获取形式从plain text改为根据lambda表达式获取
## AbstractLambdaWrapper
- 继承AbstractWrapper，主要改动是override了columnsToString方法
- 关键实现：
 - jdk lambda表达式到ColumnCache的转换：columnToString -> getColumnCache -> LambdaUtils.extract (至此就是jdk lambda相关的知识域了)
## LambdaQueryWrapper
- 继承AbstractLambdaWrapper
## LambdaUpdateWrapper
- 继承AbstractLambdaWrapper
