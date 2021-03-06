package io.mosip.pmp.partner.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import io.mosip.pmp.partner.core.RequestWrapper;
import io.mosip.pmp.partner.core.ResponseWrapper;
import io.mosip.pmp.partner.dto.APIkeyRequests;
import io.mosip.pmp.partner.dto.DownloadPartnerAPIkeyResponse;
import io.mosip.pmp.partner.dto.PartnerAPIKeyRequest;
import io.mosip.pmp.partner.dto.PartnerAPIKeyResponse;
import io.mosip.pmp.partner.dto.PartnerRequest;
import io.mosip.pmp.partner.dto.PartnerResponse;
import io.mosip.pmp.partner.dto.PartnerUpdateRequest;
import io.mosip.pmp.partner.dto.PartnersRetrieveApiKeyRequests;
import io.mosip.pmp.partner.dto.RetrievePartnerDetailsResponse;
import io.mosip.pmp.partner.logger.Logger;
import io.mosip.pmp.partner.logger.PartnerServiceLogger;
import io.mosip.pmp.partner.service.PartnerService;


/**
 * <p>This is the MOSIP Partner Service controller. This defines all the necessary operations</p>
 * <p>required for Partner</p>.
 * Partner Service Controller is having following operations.<br/>
 * 1. Partner SelfRegistration {{@link #partnerSelfRegistration(RequestWrapper)}.<br/>
 * 2. Retrieve PartnerDetails {{@link #retrievePartnerDetails(String)}.<br/>
 * 3. Update PartnerDetails {{@link #updatePartnerDetails(RequestWrapper, String)}.<br/>
 * 4. Submitting Partner API Key Request{{@link #submitPartnerApiKeyRequest(String, RequestWrapper)}.<br/>
 * 5. Downloading Partner API Key {{@link #downloadPartnerAPIkey(String, String)}.<br/>
 * 6. Retrieve All ApiKeyRequest Submitted By Partner Till Date {{@link #retrieveAllApiKeyRequestsSubmittedByPartnerTillDate(String)}.<br/>
 * 7. View ApiKeyRequest Status And ApiKey {{@link #viewApiKeyRequestStatusAndApiKey(String, String)} .<br/>
  *
  * @author sanjeev.shrivastava
  * @version 1.0
 */


@RestController
@RequestMapping(value = "/partners")
public class PartnerServiceController {
	
	private static final Logger LOGGER = PartnerServiceLogger.getLogger(PartnerServiceController.class);
	
	@Autowired
	PartnerService partnerService;
	
	/**
	 * This API would be used for self registration by partner to create Auth/E-KYC
	 * Partners. Partner Management module would be integrating with Kernel IAM
	 * module for generation of userid and password for partners.
	 * @param partnerRequest.
	 * @return partnerResponse.
	 */

	@RequestMapping(value = "partnerReg", method = RequestMethod.POST)
	public ResponseEntity<ResponseWrapper<PartnerResponse>> partnerSelfRegistration(
			@RequestBody @Valid RequestWrapper<PartnerRequest> request) {
		LOGGER.info("sessionId", "idType", "id", "description");
		ResponseWrapper<PartnerResponse> response = new ResponseWrapper<PartnerResponse>();
		PartnerResponse partnerResponse = null;
		PartnerRequest partnerRequest = null;
		partnerRequest = request.getRequest();
		partnerResponse = partnerService.savePartner(partnerRequest);
		response.setId("mosip.partnermanagement.partners.create");
		response.setVersion("1.0");
		response.setResponse(partnerResponse);
		LOGGER.info("request", "idType", "id", "request");
		return new ResponseEntity<ResponseWrapper<PartnerResponse>>(response, HttpStatus.CREATED);
	}
	
	/**
	 * This API would be used to retrieve Auth/E-KYC Partner details
	 * @param partnerID
	 * @return retrievePartnerDetailsResponse.
	 */
	
	@RequestMapping(value = "/{partnerID}", method = RequestMethod.GET)
	public ResponseEntity<ResponseWrapper<RetrievePartnerDetailsResponse>> retrievePartnerDetails(
			@PathVariable String partnerID) {

		ResponseWrapper<RetrievePartnerDetailsResponse> response = new ResponseWrapper<RetrievePartnerDetailsResponse>();
		RetrievePartnerDetailsResponse retrievePartnerDetailsResponse = null;
		retrievePartnerDetailsResponse = partnerService.getPartnerDetails(partnerID);
		response.setId("mosip.partnermanagement.partners.retrieve");
		response.setVersion("1.0");
		response.setResponse(retrievePartnerDetailsResponse);
		return new ResponseEntity<ResponseWrapper<RetrievePartnerDetailsResponse>>(response, HttpStatus.OK);
	}

	
	/**
	 * This API would be used to update Auth/E-KYC Partner's details.
	 * @param request.
	 * @param partnerID.
	 * @return partnerResponse.
	 */
	
	@RequestMapping(value = "/{partnerID}", method = RequestMethod.PUT)
	public ResponseEntity<ResponseWrapper<PartnerResponse>> updatePartnerDetails(
			@RequestBody @Valid RequestWrapper<PartnerUpdateRequest> request, @PathVariable String partnerID) {
		ResponseWrapper<PartnerResponse> response = new ResponseWrapper<PartnerResponse>();
		PartnerResponse partnerResponse = null;
		PartnerUpdateRequest partnerRequest = request.getRequest();
		partnerResponse = partnerService.updatePartnerDetail(partnerRequest, partnerID);
		response.setId("mosip.partnermanagement.partners.update");
		response.setVersion("1.0");
		response.setResponse(partnerResponse);
		return new ResponseEntity<ResponseWrapper<PartnerResponse>>(response, HttpStatus.OK);
	}

	/**
	 * This API would be used to submit Partner api key request.
	 * @param partnerID
	 * @param request
	 * @return partnerAPIKeyResponse.
	 */
	
	@RequestMapping(value = "/{partnerID}/partnerAPIKeyRequests", method = RequestMethod.POST)
	public ResponseEntity<ResponseWrapper<PartnerAPIKeyResponse>> submitPartnerApiKeyRequest(
			@PathVariable String partnerID, 
			@RequestBody RequestWrapper<PartnerAPIKeyRequest> request) {
		ResponseWrapper<PartnerAPIKeyResponse> response = new ResponseWrapper<PartnerAPIKeyResponse>();
		PartnerAPIKeyResponse partnerAPIKeyResponse = null;
		PartnerAPIKeyRequest partnerAPIKeyRequest = request.getRequest();
		partnerAPIKeyResponse = partnerService.submitPartnerApiKeyReq(partnerAPIKeyRequest, partnerID);
		response.setId("mosip.partnermanagement.partnerAPIKeyRequest.create");
		response.setVersion("1.0");
		response.setResponse(partnerAPIKeyResponse);
		return new ResponseEntity<ResponseWrapper<PartnerAPIKeyResponse>>(response, HttpStatus.CREATED);
	}

	/**
	 *  This API would be used to download Partner API key for the given APIKeyReqID.
	 * @param partnerID.
	 * @param aPIKeyReqID.
	 * @return downloadPartnerAPIkeyResponse.
	 */
	
	@RequestMapping(value = "/{partnerID}/partnerAPIKeyRequests/{aPIKeyReqID}", method = RequestMethod.POST)
	public ResponseEntity<ResponseWrapper<DownloadPartnerAPIkeyResponse>> downloadPartnerAPIkey(
			@PathVariable String partnerID, @PathVariable String aPIKeyReqID) {
		ResponseWrapper<DownloadPartnerAPIkeyResponse> response = new ResponseWrapper<DownloadPartnerAPIkeyResponse>();
		DownloadPartnerAPIkeyResponse resp = null;
		resp = partnerService.downloadPartnerAPIkey(partnerID, aPIKeyReqID);
		response.setId("mosip.partnermanagement.partnerAPIKey.download");
		response.setVersion("1.0");
		response.setResponse(resp);
		return new ResponseEntity<ResponseWrapper<DownloadPartnerAPIkeyResponse>>(response, HttpStatus.OK);
	}

	/**
	 * This API would be used to retrieve all API key requests submitted by partner till date.
	 * @param partnerID.
	 * @return partnersRetrieveApiKeyRequests.
	 */
	
	@RequestMapping(value = "/{partnerID}/partnerAPIKeyRequests", method = RequestMethod.GET)
	public ResponseEntity<ResponseWrapper<PartnersRetrieveApiKeyRequests>> retrieveAllApiKeyRequestsSubmittedByPartnerTillDate(
			@PathVariable String partnerID) {
		ResponseWrapper<PartnersRetrieveApiKeyRequests> response = new ResponseWrapper<PartnersRetrieveApiKeyRequests>();
		PartnersRetrieveApiKeyRequests partnersRetrieveApiKeyRequests = null;
		partnersRetrieveApiKeyRequests = partnerService.retrieveAllApiKeyRequestsSubmittedByPartner(partnerID);
		response.setId("mosip.partnermanagement.partners.retrieve.apiKeyRequests");
		response.setVersion("1.0");
		response.setResponse(partnersRetrieveApiKeyRequests);
		return new ResponseEntity<ResponseWrapper<PartnersRetrieveApiKeyRequests>>(response, HttpStatus.OK);
	}

	/**
	 *  This API would be used to view API key request status and API key (in case request is approved).
	 * @param partnerID.
	 * @param aPIKeyReqID.
	 * @return aPIkeyRequests.
	 */
	
	@RequestMapping(value = "/{partnerID}/partnerAPIKeyRequests/{aPIKeyReqID}", method = RequestMethod.GET)
	public ResponseEntity<ResponseWrapper<APIkeyRequests>> viewApiKeyRequestStatusAndApiKey(
			@PathVariable String partnerID, @PathVariable String aPIKeyReqID) {
		ResponseWrapper<APIkeyRequests> response = new ResponseWrapper<APIkeyRequests>();
		APIkeyRequests aPIkeyRequests = null;
		aPIkeyRequests = partnerService.viewApiKeyRequestStatusApiKey(partnerID, aPIKeyReqID);
		response.setId("mosip.partnermanagement.partners.apikey.status");
		response.setVersion("1.0");
		response.setResponse(aPIkeyRequests);
		return new ResponseEntity<ResponseWrapper<APIkeyRequests>>(response, HttpStatus.OK);
	}
}
