package com.yeah.thinkinginjava.chapter14;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

interface ProxyInterface {
	void doSomething();
	void sthElse(String arg);
}

class RealObject implements ProxyInterface {

	@Override
	public void doSomething() {
		System.out.println("doSth");
		
	}

	@Override
	public void sthElse(String arg) {
		System.out.println("sthElse " + arg);
	}
	
}

// 代理对象和实际对象都必须继承同一个接口
class SimpleProxy implements ProxyInterface {
	private ProxyInterface proxied;
	
	public SimpleProxy(ProxyInterface proxy) {
		this.proxied = proxy;
	}
	
	@Override
	public void doSomething() {
		System.out.println("Proxy doSomething");
		proxied.doSomething();
	}
	
	@Override
	public void sthElse(String arg) {
		System.out.println("proxy do sthelse " + arg);
		proxied.sthElse(arg);
	}
}

// 动态代理,不用创建SimpleProxy类
class DynamicProxyHandler implements InvocationHandler {

	private Object proxied;
	public DynamicProxyHandler(Object proxied) {
		this.proxied = proxied;
	}
	
	@Override
	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
		System.out.println("dynamic proxy, ClassName: " + proxy.getClass() + ", method: " + method + ", args: " + args);
		return method.invoke(proxied, args);
	}
	
}

public class ProxyDemo {
	public static void consumer(ProxyInterface iface) {
		iface.doSomething();
		iface.sthElse("Proxy Test");
	}
	
	public static void main(String[] args) {
		consumer(new RealObject());
		consumer(new SimpleProxy(new RealObject()));
		
		// 动态代理
		ProxyInterface proxy = (ProxyInterface)Proxy.newProxyInstance(
				ProxyInterface.class.getClassLoader(),
				new Class[] {ProxyInterface.class},
				new DynamicProxyHandler(new RealObject()));
		
		consumer(proxy);
		System.out.println("****" + proxy);
	}
}
