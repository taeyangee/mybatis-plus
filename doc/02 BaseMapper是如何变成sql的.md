# 问题
- mbp是如何做到去xml实现自动注入
- 如何集成service crud & mapper crud、crud的接口都覆盖了什么情况

# BaseMapper的接口内容
- CURD及其重载：比较有意思的是 Wrapper、Map到查询条件的转换
- Q：各种注解、在干嘛 @Param(Constants.WRAPPER) ？？？

# BaseMapper中的方法和MapperStatment的绑定
- 回顾：mybatis中的sql初始化流程：每个sql语句会初始化为MapperStatment
- 回顾：mybatis中的mapper绑定机制：mapper接口中的方法，最终和一个MapperStatment绑定
- 所以：MapperStatment的生成是关键
- 方向：BaseMapper中有默认定义方法、也支持自定义方法， 都是如何和一个MapperStatment绑定
## BaseMapper接口中的自定义方法
- 使用： BaseMapper接口中的自定义方法通过@Select、@Update，即可执行SQL操作
- 功能实现：MybatisMapperAnnotationBuilder根据注解完成了BaseMapper中自定义方法到MapperStatment的绑定
- 代码路径：MybatisMapperAnnotationBuilder#parse -> #parseStatement
## BaseMapper接口中的默认方法
- 功能实现：MybatisMapperAnnotationBuilder-> SqlInjector-> 各种AbstractMethod子类，完成了默认方法到MapperStatment的绑定
- 相关类
    - [AbstractMethod体系](pic/02AbstractMethod.uml)： 每个AbstractMethod子类 对应 BaseMapper中的一个方法对应BaseMapper中的一个方法
    - [ISqlInjector体系](pic/02ISqlInjector.uml)： 像mb注入MappedStatement
