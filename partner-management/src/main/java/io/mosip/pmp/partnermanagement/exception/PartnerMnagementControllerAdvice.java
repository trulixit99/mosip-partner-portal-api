package io.mosip.pmp.partnermanagement.exception;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import org.springframework.web.util.ContentCachingRequestWrapper;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.mosip.pmp.partnermanagement.constant.PartnerManagementInputExceptionConstant;
import io.mosip.pmp.partnermanagement.core.ResponseWrapper;
/**
 * @author sanjeev.shrivastava
 *
 */

@RestControllerAdvice
public class PartnerMnagementControllerAdvice extends ResponseEntityExceptionHandler{
	
	@Autowired
	private ObjectMapper objectMapper;
	
	/**
	 * @param httpServletRequest
	 * @param exception
	 * @return
	 * @throws IOException
	 */
	
	@ExceptionHandler(PartnerAPIKeyDoesNotExistException.class)
	public ResponseEntity<ResponseWrapper<ErrorResponse>> getPartnerAPIKeyDoesNotExistExceptionMassage(
			final HttpServletRequest httpServletRequest, final PartnerAPIKeyDoesNotExistException exception)
			throws IOException {
		ResponseWrapper<ErrorResponse> response = setErrors(httpServletRequest);
		ErrorResponse errorResponse = new ErrorResponse();
		errorResponse.setErrorCode(exception.getErrorCode());
		errorResponse.setMessage(exception.getErrorText());
		
		List<ErrorResponse> errorlist = new ArrayList<ErrorResponse>();
		errorlist.add(errorResponse);
		
		response.setErrors(errorlist);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}
	
	/**
	 * @param httpServletRequest
	 * @param exception
	 * @return
	 * @throws IOException
	 */
	
	@ExceptionHandler(PolicyNotExistException.class)
	public ResponseEntity<ResponseWrapper<ErrorResponse>> getPolicyNotExistExceptionMassage(
			final HttpServletRequest httpServletRequest, final PolicyNotExistException exception)
			throws IOException {
		ResponseWrapper<ErrorResponse> response = setErrors(httpServletRequest);
		ErrorResponse errorResponse = new ErrorResponse();
		errorResponse.setErrorCode(exception.getErrorCode());
		errorResponse.setMessage(exception.getErrorText());
		
		List<ErrorResponse> errorlist = new ArrayList<ErrorResponse>();
		errorlist.add(errorResponse);
		
		response.setErrors(errorlist);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}
	
	/**
	 * @param httpServletRequest
	 * @param exception
	 * @return
	 * @throws IOException
	 */
	
	@ExceptionHandler(PartnerDoesNotExistException.class)
	public ResponseEntity<ResponseWrapper<ErrorResponse>> getPartnerDoesNotExistExceptionMassage(
			final HttpServletRequest httpServletRequest, final PartnerDoesNotExistException exception)
			throws IOException {
		ResponseWrapper<ErrorResponse> response = new ResponseWrapper<ErrorResponse>();
		ErrorResponse errorResponse = new ErrorResponse();
		errorResponse.setErrorCode(exception.getErrorCode());
		errorResponse.setMessage(exception.getErrorText());
		
		List<ErrorResponse> errorlist = new ArrayList<ErrorResponse>();
		errorlist.add(errorResponse);
		
		response.setId("mosip.partnermanagement.partners.retrieve");
		response.setVersion("1.0");
		response.setErrors(errorlist);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}
	
	
	/**
	 * @param httpServletRequest
	 * @param exception
	 * @return
	 * @throws IOException
	 */
	
	@ExceptionHandler(PartnerIdDoesNotExistException.class)
	public ResponseEntity<ResponseWrapper<ErrorResponse>> getPartnerIdDoesNotExistExceptionMassage(
			final HttpServletRequest httpServletRequest, final PartnerIdDoesNotExistException exception)
			throws IOException {
		ResponseWrapper<ErrorResponse> response = new ResponseWrapper<ErrorResponse>();
		ErrorResponse errorResponse = new ErrorResponse();
		errorResponse.setErrorCode(exception.getErrorCode());
		errorResponse.setMessage(exception.getErrorText());
		
		List<ErrorResponse> errorlist = new ArrayList<ErrorResponse>();
		errorlist.add(errorResponse);
		
		response.setId("mosip.partnermanagement.partners.retrieve");
		response.setVersion("1.0");
		response.setErrors(errorlist);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}
	
	/**
	 * @param httpServletRequest
	 * @param exception
	 * @return
	 * @throws IOException
	 */
	
	@ExceptionHandler(NoPartnerApiKeyRequestsException.class)
	public ResponseEntity<ResponseWrapper<ErrorResponse>> getNoPartnerApiKeyRequestExceptionMassage(
			final HttpServletRequest httpServletRequest, final NoPartnerApiKeyRequestsException exception)
			throws IOException {
		ResponseWrapper<ErrorResponse> response = new ResponseWrapper<ErrorResponse>();
		ErrorResponse errorResponse = new ErrorResponse();
		errorResponse.setErrorCode(exception.getErrorCode());
		errorResponse.setMessage(exception.getErrorText());
		
		List<ErrorResponse> errorlist = new ArrayList<ErrorResponse>();
		errorlist.add(errorResponse);
		
		response.setId("mosip.partnermanagement.partners.apikey.request.retrieve");
		response.setVersion("1.0");
		response.setErrors(errorlist);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}
	
	/**
	 * @param httpServletRequest
	 * @param exception
	 * @return
	 * @throws IOException
	 */
	
	@ExceptionHandler(PartnerApiKeyDoesNotBelongToThePolicyGroupOfThePartnerMangerException.class)
	public ResponseEntity<ResponseWrapper<ErrorResponse>> getPartnerApiKeyDoesNotBelongToThePolicyGroupExceptionMassage(
			final HttpServletRequest httpServletRequest, final PartnerApiKeyDoesNotBelongToThePolicyGroupOfThePartnerMangerException exception)
			throws IOException {
		ResponseWrapper<ErrorResponse> response = new ResponseWrapper<ErrorResponse>();
		ErrorResponse errorResponse = new ErrorResponse();
		errorResponse.setErrorCode(exception.getErrorCode());
		errorResponse.setMessage(exception.getErrorText());
		
		List<ErrorResponse> errorlist = new ArrayList<ErrorResponse>();
		errorlist.add(errorResponse);
		
		response.setId("mosip.partnermanagement.partners.retrieve.policy");
		response.setVersion("1.0");
		response.setErrors(errorlist);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}
	
	@ExceptionHandler(PartnerAPIDoesNotExistException.class)
	public ResponseEntity<ResponseWrapper<ErrorResponse>> getPartnerApiDoesNotExistExceptionMassage(
			final HttpServletRequest httpServletRequest, final PartnerAPIDoesNotExistException exception)
			throws IOException {
		ResponseWrapper<ErrorResponse> response = new ResponseWrapper<ErrorResponse>();
		ErrorResponse errorResponse = new ErrorResponse();
		errorResponse.setErrorCode(exception.getErrorCode());
		errorResponse.setMessage(exception.getErrorText());
		
		List<ErrorResponse> errorlist = new ArrayList<ErrorResponse>();
		errorlist.add(errorResponse);
		
		response.setId("mosip.partnermanagement.partners.retrieve.policy");
		response.setVersion("1.0");
		response.setErrors(errorlist);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}
	
	/**
	 * @param httpServletRequest
	 * @return
	 * @throws IOException
	 */
	
	private ResponseWrapper<ErrorResponse> setErrors(HttpServletRequest httpServletRequest) throws IOException {
		ResponseWrapper<ErrorResponse> responseWrapper = new ResponseWrapper<>();
		String requestBody = null;
		if (httpServletRequest instanceof ContentCachingRequestWrapper) {
			requestBody = new String(((ContentCachingRequestWrapper) httpServletRequest).getContentAsByteArray());
		}
		
		JsonNode reqNode = objectMapper.readTree(requestBody);
		responseWrapper.setId(reqNode.path("id").asText());
		responseWrapper.setVersion(reqNode.path("version").asText());
		return responseWrapper;
	}
	
	/**
	 * @param httpServletRequest
	 * @param exception
	 * @return
	 * @throws IOException
	 */
	
	@ExceptionHandler({Exception.class , RuntimeException.class})
	public ResponseEntity<ResponseWrapper<ErrorResponse>> getExceptionMassage(
			final HttpServletRequest httpServletRequest, final RuntimeException exception)
			throws IOException {
		ResponseWrapper<ErrorResponse> response = setErrors(httpServletRequest);
		ErrorResponse errorResponse = new ErrorResponse();
		errorResponse.setMessage(exception.getMessage());
		
		List<ErrorResponse> errorlist = new ArrayList<ErrorResponse>();
		errorlist.add(errorResponse);
		
		response.setErrors(errorlist);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}
	
	@Override
	protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {
		 Map<String, Object> body = new LinkedHashMap<>(); 
		 body.put("timestamp", new Date()); 
		 body.put("status", status.value());
		 ErrorResponse errorResponse =new ErrorResponse();
		 errorResponse.setErrorCode(PartnerManagementInputExceptionConstant.MISSING_PARTNER_MANAGEMENT_INPUT_PARAMETER.getErrorCode());
		 errorResponse.setMessage(PartnerManagementInputExceptionConstant.MISSING_PARTNER_MANAGEMENT_INPUT_PARAMETER.getErrorMessage());
		 List<ErrorResponse> errors = new ArrayList<ErrorResponse>();
		 errors.add(errorResponse);
		 
		body.put("errors", errors);
		return new ResponseEntity<>(body, headers, status);
	}

}
