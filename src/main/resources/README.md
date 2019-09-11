##Spring bean的作用域
    Singleton -- 整个应用中只有一个
    Prototype -- 每次注入或通过上下文获取时，都会创建一个bean
    Session -- 为每个会话创建一个bean
    Request -- 为每个请求创建一个bean
    通过@Scop(ConfigurableBeanFactory.SCOPE_PROTOTYPE)设置

##Scope的ProxyMode
    如果注入的bean不是Singleton的，那么实际会注入哪个对象呢？
    答案：在注入的时候是注入代理对象，而不是真实的对象。
    如果这个对象时具体的类，那么通过CGLIB生成继承这个类的子类代理对象(ScopedProxyMode.TARGET_CLASS)。
    如果这个对象时接口，那么通过JDK的代理方式生成实现这个接口的对象(ScopedProxyMode.INTERFACES)。
    最终代理对象把请求转给具体的对象实例来完成调用。