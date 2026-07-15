package controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import util.TestUtil;

class EmpListServletTest {

	private EmpListServlet servlet;

	@BeforeEach
	void setUp() throws Exception {
		TestUtil.initDB();
		TestUtil.setDS101ToDB();
		TestUtil.setDS001ToDB();
		servlet = new EmpListServlet();
	}

	@AfterEach
	void tearDown() throws Exception {
		TestUtil.initDB();
	}

	@Test
	@DisplayName("社員一覧取得成功時に社員一覧JSPに正しいパスでフォワードする")
	void doGetForwardsToEmployeeListJsp() throws Exception {
		Map<String, Object> attributes = new HashMap<>();
		AtomicReference<String> dispatchedPath = new AtomicReference<>();
		HttpServletRequest request = createRequest(attributes, dispatchedPath);
		HttpServletResponse response = createResponse();

		servlet.doGet(request, response);

		assertEquals("/WEB-INF/jsp/employee/select/employeeselectlist.jsp", dispatchedPath.get());
		assertNotNull(attributes.get("empAllList"));
	}

	private HttpServletRequest createRequest(Map<String, Object> attributes, AtomicReference<String> dispatchedPath) {
		return (HttpServletRequest) Proxy.newProxyInstance(
				HttpServletRequest.class.getClassLoader(),
				new Class<?>[] { HttpServletRequest.class },
				new InvocationHandler() {
					@Override
					public Object invoke(Object proxy, Method method, Object[] args) {
						if ("setAttribute".equals(method.getName())) {
							attributes.put((String) args[0], args[1]);
							return null;
						}
						if ("getAttribute".equals(method.getName())) {
							return attributes.get(args[0]);
						}
						if ("getRequestDispatcher".equals(method.getName())) {
							dispatchedPath.set((String) args[0]);
							return createDispatcher();
						}
						if ("getContextPath".equals(method.getName())) {
							return "";
						}
						return defaultValue(method.getReturnType());
					}
				});
	}

	private RequestDispatcher createDispatcher() {
		return (RequestDispatcher) Proxy.newProxyInstance(
				RequestDispatcher.class.getClassLoader(),
				new Class<?>[] { RequestDispatcher.class },
				new InvocationHandler() {
					@Override
					public Object invoke(Object proxy, Method method, Object[] args) {
						return null;
					}
				});
	}

	private HttpServletResponse createResponse() {
		return (HttpServletResponse) Proxy.newProxyInstance(
				HttpServletResponse.class.getClassLoader(),
				new Class<?>[] { HttpServletResponse.class },
				new InvocationHandler() {
					@Override
					public Object invoke(Object proxy, Method method, Object[] args) {
						return defaultValue(method.getReturnType());
					}
				});
	}

	private Object defaultValue(Class<?> returnType) {
		if (returnType == boolean.class) {
			return false;
		}
		if (returnType == int.class) {
			return 0;
		}
		if (returnType == long.class) {
			return 0L;
		}
		if (returnType == double.class) {
			return 0D;
		}
		if (returnType == float.class) {
			return 0F;
		}
		if (returnType == short.class) {
			return (short) 0;
		}
		if (returnType == byte.class) {
			return (byte) 0;
		}
		if (returnType == char.class) {
			return (char) 0;
		}
		return null;
	}
}
