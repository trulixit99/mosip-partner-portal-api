package io.mosip.pmp.partnermanagement.service.impl;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import io.mosip.pmp.partnermanagement.constant.NoPartnerApiKeyRequestsConstant;
import io.mosip.pmp.partnermanagement.constant.PartnerAPIDoesNotExistConstant;
import io.mosip.pmp.partnermanagement.constant.PartnerAPIKeyDoesNotExistConstant;
import io.mosip.pmp.partnermanagement.constant.PartnerApiKeyDoesNotBelongToThePolicyGroupOfThePartnerMangerConstant;
import io.mosip.pmp.partnermanagement.constant.PartnerDoesNotExistExceptionConstant;
import io.mosip.pmp.partnermanagement.constant.PartnerIdDoesNotExistExceptionConstant;
import io.mosip.pmp.partnermanagement.constant.PolicyNotExistConstant;
import io.mosip.pmp.partnermanagement.dto.ActivateDeactivatePartnerRequest;
import io.mosip.pmp.partnermanagement.dto.ApikeyRequests;
import io.mosip.pmp.partnermanagement.dto.PartnerAPIKeyToPolicyMappingsResponse;
import io.mosip.pmp.partnermanagement.dto.PartnersPolicyMappingRequest;
import io.mosip.pmp.partnermanagement.dto.PartnersPolicyMappingResponse;
import io.mosip.pmp.partnermanagement.dto.RetrievePartnerDetailsResponse;
import io.mosip.pmp.partnermanagement.dto.RetrievePartnersDetails;
import io.mosip.pmp.partnermanagement.entity.AuthPolicy;
import io.mosip.pmp.partnermanagement.entity.Partner;
import io.mosip.pmp.partnermanagement.entity.PartnerPolicy;
import io.mosip.pmp.partnermanagement.entity.PartnerPolicyRequest;
import io.mosip.pmp.partnermanagement.entity.PolicyGroup;
import io.mosip.pmp.partnermanagement.exception.NoPartnerApiKeyRequestsException;
import io.mosip.pmp.partnermanagement.exception.PartnerAPIDoesNotExistException;
import io.mosip.pmp.partnermanagement.exception.PartnerAPIKeyDoesNotExistException;
import io.mosip.pmp.partnermanagement.exception.PartnerApiKeyDoesNotBelongToThePolicyGroupOfThePartnerMangerException;
import io.mosip.pmp.partnermanagement.exception.PartnerDoesNotExistException;
import io.mosip.pmp.partnermanagement.exception.PartnerIdDoesNotExistException;
import io.mosip.pmp.partnermanagement.exception.PolicyNotExistException;
import io.mosip.pmp.partnermanagement.repository.AuthPolicyRepository;
import io.mosip.pmp.partnermanagement.repository.PartnerPolicyRepository;
import io.mosip.pmp.partnermanagement.repository.PartnerPolicyRequestRepository;
import io.mosip.pmp.partnermanagement.repository.PartnerRepository;
import io.mosip.pmp.partnermanagement.repository.PolicyGroupRepository;
import io.mosip.pmp.partnermanagement.service.PartnerManagementService;
import io.mosip.pmp.partnermanagement.util.PartnerUtil;

/**
 * @author sanjeev.shrivastava
 *
 */

@Service
public class PartnerManagementServiceImpl implements PartnerManagementService {

	@Autowired
	PartnerPolicyRepository partnerPolicyRepository;

	@Autowired
	PartnerRepository partnerRepository;
	
	@Autowired
	PartnerPolicyRequestRepository partnerPolicyRequestRepository;
	
	@Autowired
	PolicyGroupRepository policyGroupRepository;
	
	@Autowired
	AuthPolicyRepository authPolicyRepository;

	@Override
	public PartnersPolicyMappingResponse partnerApiKeyPolicyMappings(PartnersPolicyMappingRequest request,
			String partnerID, String PolicyAPIKey) {

		Optional<PartnerPolicy> findById = partnerPolicyRepository.findById(PolicyAPIKey);
		PartnerPolicy partnerPolicy = null;
		if (findById.isPresent() && findById != null) {
			partnerPolicy = findById.get();
			
			String auth_pilicy_id = partnerPolicy.getPolicyId();
			String part_id = partnerPolicy.getPartner().getId();
			if(auth_pilicy_id!=null && part_id.equalsIgnoreCase(partnerID)) {
				Optional<AuthPolicy> findByAuthId = authPolicyRepository.findById(auth_pilicy_id);
				if(findByAuthId.isPresent()) {
					AuthPolicy authPolicy = findByAuthId.get();
					PolicyGroup policyGroup = null; 
					String old_policy_id= authPolicy.getPolicyGroup().getId();
					if(old_policy_id.equalsIgnoreCase(request.getOldPolicyID())) {
						Optional<PolicyGroup> findByGroupId = policyGroupRepository.findById(request.getNewPolicyID());
						if(findByGroupId.isPresent()) {
							policyGroup = findByGroupId.get();
						}else {
							System.out.println("throw NewPolicyIdIsNotExist");
						}
						authPolicy.setPolicyGroup(policyGroup);
						authPolicyRepository.save(authPolicy);
					}else {
						throw new PolicyNotExistException(PolicyNotExistConstant.POLICY_NOT_EXIST_EXCEPTION.getErrorCode(),
								PolicyNotExistConstant.POLICY_NOT_EXIST_EXCEPTION.getErrorMessage());
					}
				}else {
					System.out.println("Auth Policy Not Exist");
				}
			}else {
				System.out.println("Throw Partner Not Exist Exception");
			}
		}else {
			throw new PartnerAPIKeyDoesNotExistException(
					PartnerAPIKeyDoesNotExistConstant.PARTNER_API_KEY_DOES_NOT_EXIST_EXCEPTION.getErrorCode(),
					PartnerAPIKeyDoesNotExistConstant.PARTNER_API_KEY_DOES_NOT_EXIST_EXCEPTION.getErrorMessage());	
		} 
		PartnersPolicyMappingResponse partnersPolicyMappingResponse = new PartnersPolicyMappingResponse();
		partnersPolicyMappingResponse.setMessage("Partner api key to Policy Mappings updated successfully");
		return partnersPolicyMappingResponse;
	}

	@Override
	public PartnersPolicyMappingResponse activateDeactivateAuthEKYCPartner(String partnerID,
			ActivateDeactivatePartnerRequest request) {
		PartnersPolicyMappingResponse partnersPolicyMappingResponse = new PartnersPolicyMappingResponse();
		Optional<Partner> findById = partnerRepository.findById(partnerID);
		if (findById.isPresent() && findById != null) {
			Partner partner = findById.get();
			String partner_status = request.getStatus();
				if(partner_status.equalsIgnoreCase("Active")) {
					partner.setIsActive(true);
				} else if (partner_status.equalsIgnoreCase("De-Active")) {
					partner.setIsActive(false);
				}
			partnerRepository.save(partner);
		} else {
			throw new PartnerIdDoesNotExistException(
					PartnerIdDoesNotExistExceptionConstant.PARTNER_ID_DOES_NOT_EXIST_EXCEPTION.getErrorCode(),
					PartnerIdDoesNotExistExceptionConstant.PARTNER_ID_DOES_NOT_EXIST_EXCEPTION.getErrorMessage());
		}
		partnersPolicyMappingResponse.setMessage("Partner status updated successfully");
		return partnersPolicyMappingResponse;
	}
	
	@Override
	public PartnersPolicyMappingResponse activateDeactivatePartnerAPIKeyGivenPartner(String partnerID,
			ActivateDeactivatePartnerRequest request, String partnerAPIKey) {
		Optional<PartnerPolicy> findById = partnerPolicyRepository.findById(partnerAPIKey);

		if (findById.isPresent() && findById != null) {
			PartnerPolicy partnerPolicy = findById.get();
			if (partnerPolicy.getPartner().getId().equals(partnerID)) {
				
				if(request.getStatus().equalsIgnoreCase("Active")) {
					partnerPolicy.setIsActive(true);
				}else if(request.getStatus().equalsIgnoreCase("De-Active")){
					partnerPolicy.setIsActive(false);
				}
				partnerPolicyRepository.save(partnerPolicy);
			} else {
				throw new PartnerDoesNotExistException(
						PartnerIdDoesNotExistExceptionConstant.PARTNER_ID_DOES_NOT_EXIST_EXCEPTION.getErrorCode(),
						PartnerIdDoesNotExistExceptionConstant.PARTNER_ID_DOES_NOT_EXIST_EXCEPTION.getErrorMessage());
			}
		} else {
			throw new PartnerAPIKeyDoesNotExistException(
					PartnerAPIKeyDoesNotExistConstant.PARTNER_API_KEY_DOES_NOT_EXIST_EXCEPTION.getErrorCode(),
					PartnerAPIKeyDoesNotExistConstant.PARTNER_API_KEY_DOES_NOT_EXIST_EXCEPTION.getErrorMessage());
		}
		PartnersPolicyMappingResponse partnersPolicyMappingResponse = new PartnersPolicyMappingResponse();
		partnersPolicyMappingResponse.setMessage("Partner API Key status updated successfully");
		return partnersPolicyMappingResponse;
	}

	@Override
	public RetrievePartnerDetailsResponse getAllAuthEKYCPartnersForThePolicyGroup() {
		RetrievePartnerDetailsResponse partnersResponse = new RetrievePartnerDetailsResponse();
		List<RetrievePartnersDetails> partners = new ArrayList<RetrievePartnersDetails>();

		List<Partner> list_part = null;
		list_part = partnerRepository.findAll();
		Partner partner = null;
		Iterator<Partner> partner_iterat = list_part.iterator();

		while (partner_iterat.hasNext()) {
			RetrievePartnersDetails retrievePartnersDetails = new RetrievePartnersDetails();
			partner = partner_iterat.next();

			retrievePartnersDetails.setPartnerID(partner.getId());
			if(partner.getIsActive()) {
				retrievePartnersDetails.setStatus("Active");
			}else {
				retrievePartnersDetails.setStatus("De-Active");
			}
			//retrievePartnersDetails.setStatus(partner.getIsActive()== true ? "Active" : "De-Active");
			retrievePartnersDetails.setOrganizationName(partner.getName());
			retrievePartnersDetails.setContactNumber(partner.getContactNo());
			retrievePartnersDetails.setEmailId(partner.getEmailId());
			retrievePartnersDetails.setAddress(partner.getAddress());

			partners.add(retrievePartnersDetails);
		}
		partnersResponse.setPartners(partners);
		return partnersResponse;
	}

	@Override
	public RetrievePartnersDetails getparticularAuthEKYCPartnerDetailsForGivenPartnerId(String partnerID) {
		Optional<Partner> findById = partnerRepository.findById(partnerID);
		RetrievePartnersDetails retrievePartnersDetails = new RetrievePartnersDetails();
		if (findById.isPresent() && findById != null) {
			Partner partner = findById.get();

			retrievePartnersDetails.setPartnerID(partner.getId());
			if(partner.getIsActive()) {
				retrievePartnersDetails.setStatus("Active");
			}else {
				retrievePartnersDetails.setStatus("De-Active");
			}
			retrievePartnersDetails.setOrganizationName(partner.getName());
			retrievePartnersDetails.setContactNumber(partner.getContactNo());
			retrievePartnersDetails.setEmailId(partner.getEmailId());
			retrievePartnersDetails.setAddress(partner.getAddress());

		} else {
			throw new PartnerDoesNotExistException(
					PartnerDoesNotExistExceptionConstant.PARTNER_DOES_NOT_EXIST_EXCEPTION.getErrorCode(),
					PartnerDoesNotExistExceptionConstant.PARTNER_DOES_NOT_EXIST_EXCEPTION.getErrorMessage());
		}
		return retrievePartnersDetails;
	}

	@Override
	public PartnerAPIKeyToPolicyMappingsResponse getPartnerAPIKeyToPolicyMapping(String partnerID,
			String PartnerAPIKey) {
		AuthPolicy authPolicy = null;
		Optional<PartnerPolicy> findById = partnerPolicyRepository.findById(PartnerAPIKey);
		PartnerAPIKeyToPolicyMappingsResponse response = new PartnerAPIKeyToPolicyMappingsResponse();
		if (findById.isPresent() && findById != null) {
			PartnerPolicy partnerPolicy = findById.get();
			if (partnerPolicy.getPartner().getId().equals(partnerID)) {
				response.setPartnerID(partnerID);
				String auth_id = partnerPolicy.getPolicyId();
				Optional<AuthPolicy> findByAuthId = authPolicyRepository.findById(auth_id);
				if(findByAuthId.isPresent()) {
					authPolicy =findByAuthId.get();
					response.setPolicyId(authPolicy.getPolicyGroup().getId());
				}else {
					throw new PartnerApiKeyDoesNotBelongToThePolicyGroupOfThePartnerMangerException(
							PartnerApiKeyDoesNotBelongToThePolicyGroupOfThePartnerMangerConstant.PARTNER_API_DOES_NOT_BELONGS_TO_THE_POLICYGROUP_OF_PARTNERMANAGER_EXCEPTION
									.getErrorCode(),
							PartnerApiKeyDoesNotBelongToThePolicyGroupOfThePartnerMangerConstant.PARTNER_API_DOES_NOT_BELONGS_TO_THE_POLICYGROUP_OF_PARTNERMANAGER_EXCEPTION
									.getErrorMessage());
				}
			} else {
				System.err.println("Partner Not Exist Exception");
			}
		} else {
			throw new PartnerAPIDoesNotExistException(
					PartnerAPIDoesNotExistConstant.PARTNER_API_DOES_NOT_EXIST_EXCEPTION.getErrorCode(),
					PartnerAPIDoesNotExistConstant.PARTNER_API_DOES_NOT_EXIST_EXCEPTION.getErrorMessage());
		}
		return response;
	}

	@Override
	public List<ApikeyRequests> getAllPartnerAPIKeyRequestsAsReceivedByPartnerManagers() {
		
		List<ApikeyRequests> requests = new ArrayList<ApikeyRequests>();
		
		List<PartnerPolicyRequest> findAll = partnerPolicyRequestRepository.findAll();
		
		PartnerPolicyRequest partnerPolicyRequest = new PartnerPolicyRequest();
		
		String parnerId = null;
		if(!findAll.isEmpty() && findAll!=null) {
			
			Iterator<PartnerPolicyRequest> partnerPolicyRequest_iterat = findAll.iterator();
			while (partnerPolicyRequest_iterat.hasNext()) {
				ApikeyRequests ApikeyRequests = new ApikeyRequests();
				partnerPolicyRequest = partnerPolicyRequest_iterat.next();
				parnerId = partnerPolicyRequest.getPartner().getId();
				ApikeyRequests.setPartnerID(parnerId);
				ApikeyRequests.setStatus(partnerPolicyRequest.getStatusCode());
				
				Optional<Partner> findBy_PartnerId = partnerRepository.findById(parnerId);
				
				if(!findBy_PartnerId.isPresent()) {
					throw new PartnerDoesNotExistException(
							PartnerDoesNotExistExceptionConstant.PARTNER_DOES_NOT_EXIST_EXCEPTION.getErrorCode(),
							PartnerDoesNotExistExceptionConstant.PARTNER_DOES_NOT_EXIST_EXCEPTION.getErrorMessage());
				}
				
				Partner partner = findBy_PartnerId.get();
				
				ApikeyRequests.setOrganizationName(partner.getName());
				
				String policy_group_id = partner.getPolicyGroupId();
				Optional<PolicyGroup> findById = policyGroupRepository.findById(policy_group_id);
				PolicyGroup policyGroup = findById.get();
				
				ApikeyRequests.setPolicyName(policyGroup.getName());
				ApikeyRequests.setPolicyDesc(policyGroup.getDescr());
				ApikeyRequests.setApiKeyReqNo(partnerPolicyRequest.getId());

				requests.add(ApikeyRequests);
			}
		}else {
			throw new NoPartnerApiKeyRequestsException(
					NoPartnerApiKeyRequestsConstant.NO_PARTNER_API_KEY_REQUEST_EXCEPTION.getErrorCode(),
					NoPartnerApiKeyRequestsConstant.NO_PARTNER_API_KEY_REQUEST_EXCEPTION.getErrorMessage());
		}
		
		return requests;
	}

	@Override
	public ApikeyRequests getTheRequestForPartnerAPIKeyToPolicyMappingsForGivenRequestId(String APIKeyReqID) {
		ApikeyRequests apikeyRequests = new ApikeyRequests();
		Optional<PartnerPolicyRequest> findById = partnerPolicyRequestRepository.findById(APIKeyReqID);
		if(findById.isPresent() && findById!=null) {
			PartnerPolicyRequest partnerPolicyRequest = findById.get();
			
			String partnerId = partnerPolicyRequest.getPartner().getId();
			String policy_Id = partnerPolicyRequest.getPolicyId();
			
			Optional<Partner> findByPartnerId = partnerRepository.findById(partnerId);
			Partner partner = findByPartnerId.get();
			
			Optional<PolicyGroup> findByPolicyId = policyGroupRepository.findById(policy_Id);			
			PolicyGroup policyGroup = findByPolicyId.get();
			
			apikeyRequests.setPartnerID(partner.getId());
			apikeyRequests.setStatus(partnerPolicyRequest.getStatusCode());
			apikeyRequests.setOrganizationName(partner.getName());
			apikeyRequests.setPolicyName(policyGroup.getName());
			apikeyRequests.setPolicyDesc(policyGroup.getDescr());
			apikeyRequests.setApiKeyReqNo(partnerPolicyRequest.getId());
		}else {
			throw new NoPartnerApiKeyRequestsException(
					NoPartnerApiKeyRequestsConstant.NO_PARTNER_API_KEY_REQUEST_EXCEPTION.getErrorCode(),
					NoPartnerApiKeyRequestsConstant.NO_PARTNER_API_KEY_REQUEST_EXCEPTION.getErrorMessage());
		}
		
		return apikeyRequests;
	}

	@Override
	public PartnersPolicyMappingResponse approveRejectPartnerAPIKeyRequestsBasedOnAPIKeyRequestId(
			ActivateDeactivatePartnerRequest request, String partnerKeyReqId) {
		PartnerPolicyRequest partnerPolicyRequest=null;
		String partner_id = null;
		PartnersPolicyMappingResponse partnersPolicyMappingResponse = new PartnersPolicyMappingResponse();
		Optional<PartnerPolicyRequest> findById = partnerPolicyRequestRepository.findById(partnerKeyReqId);
		
		if(!findById.isPresent()) {
			throw new PartnerAPIDoesNotExistException(
					PartnerAPIDoesNotExistConstant.PARTNER_API_DOES_NOT_EXIST_EXCEPTION.getErrorCode(),
					PartnerAPIDoesNotExistConstant.PARTNER_API_DOES_NOT_EXIST_EXCEPTION.getErrorMessage());
		}
		
		partnerPolicyRequest = findById.get();
		partnerPolicyRequest.setStatusCode(request.getStatus());
		partnerPolicyRequestRepository.save(partnerPolicyRequest);
		// Creating Partner_API_Key and Inserting Policy_Id and Partner_Id in the Same 
		
		//Partner_API_Key should be unique for same partner.
		PartnerPolicy partnerPolicy = new PartnerPolicy();
		partner_id = partnerPolicyRequest.getPartner().getId();
		PartnerPolicy findByPartnerId = partnerPolicyRepository.findByPartnerId(partner_id);
		
		if(findByPartnerId!=null) {
			//TODO Log info
			// partnerKeyReqId already have Partner_API_Key.
			// duplicate Partner_API_Key
		}else {
			String Partner_API_Key = PartnerUtil.createPartnerApiKey(); 
			Optional<PartnerPolicy> detail_Partner_API_Key = partnerPolicyRepository.findById(Partner_API_Key);
			if(detail_Partner_API_Key.isPresent()) {
				//TODO Log info
				// duplicate Partner_API_Key
			}else {
				partnerPolicy.setPolicyApiKey(Partner_API_Key);
				partnerPolicy.setPartner(partnerPolicyRequest.getPartner());
				
				// need to update auth_policy_id
				String policy_id = partnerPolicyRequest.getPolicyId();
				
				AuthPolicy findByPolicyId = authPolicyRepository.findByPolicyId(policy_id);
				if(findByPolicyId == null) {
					System.out.println("throw the error AuthPolicyNotFoundException");
				}
				
				partnerPolicy.setPolicyId(findByPolicyId.getId());
				partnerPolicy.setIsActive(true);
				
				LocalDateTime now = LocalDateTime.now();		
				partnerPolicy.setValidFromDatetime(Timestamp.valueOf(now));
				partnerPolicy.setValidToDatetime(Timestamp.valueOf(now));
				partnerPolicy.setCrBy(partnerPolicyRequest.getCrBy());
				partnerPolicy.setCrDtimes(partnerPolicyRequest.getCrDtimes());
				
				partnerPolicyRepository.save(partnerPolicy);
			}
		}
		partnersPolicyMappingResponse.setMessage("PartnerAPIKey approved successfully");
		return partnersPolicyMappingResponse;
	}
}
