package com.oaosis.samak.global.security.jwt.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.oaosis.samak.global.response.ApiResponse;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Slf4j
@Component
public class JsonResponseWriter {

	private static final ObjectMapper objectMapper = new ObjectMapper();

	private static void setDefaultResponse(HttpServletResponse response, HttpStatus httpStatus) {
		response.setContentType(MediaType.APPLICATION_JSON_VALUE);
		response.setStatus(httpStatus.value());
		response.setCharacterEncoding("UTF-8");
	}

	public static void sendSuccessResponse(HttpServletResponse response, HttpStatus httpStatus, Object body) throws
			IOException {
		setDefaultResponse(response, httpStatus);
		String responseBody = objectMapper.writeValueAsString(ApiResponse.success(body));
		response.getWriter().write(responseBody);
	}

	public static void sendErrorResponse(HttpServletResponse response, HttpStatus httpStatus, Object body)
		throws IOException {
		setDefaultResponse(response, httpStatus);
		String responseBody = objectMapper.writeValueAsString(body);
		response.getWriter().write(responseBody);
	}
}
