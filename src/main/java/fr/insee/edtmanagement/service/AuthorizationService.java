package fr.insee.edtmanagement.service;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import fr.insee.edtmanagement.constants.Constants;
import fr.insee.edtmanagement.repository.SurveyAssigmentRepository;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class AuthorizationService {

	@Autowired
	private SurveyAssigmentRepository surveyAssigmentRepository;
	
	@Value("${fr.insee.edtmanagement.loose-check-habilitation:false}")
	private boolean looseCheckHabilitation;

	@Cacheable("isAuthorized")
	public Boolean isAuthorized(String interrogationId, String expectedRole, String userId) {

		log.debug("Service checking Authorization of userId : {} on interrogationId : {} and role :  {} ", userId, interrogationId,expectedRole);
		
		if (Constants.INTERVIEWER.equals(StringUtils.upperCase(expectedRole))) {
			if (checkInterviewerHabilitation(interrogationId, userId)) {
				return true; 
				// Workaround to handle checkHabilitation request from queen BO in case of 
				// some PUT request made by a reviewer 
			} else if (looseCheckHabilitation) {
				log.info("Looking for an habilitation for user {} as a reviewer",userId);
				return checkReviewerHabilitation(interrogationId, userId);
			}
		}

		if (Constants.REVIEWER.equals(StringUtils.upperCase(expectedRole))) {
			return checkReviewerHabilitation(interrogationId, userId);
		}

		return false;

	}

	private boolean checkInterviewerHabilitation(String interrogationId, String userId) {
		return surveyAssigmentRepository
				.findByInterviewerIdIgnoreCaseAndInterrogationId(userId, interrogationId).isPresent();
	}

	private boolean checkReviewerHabilitation(String interrogationId, String userId) {
		return surveyAssigmentRepository
				.findByReviewerIdIgnoreCaseAndInterrogationId(userId, interrogationId).isPresent();          
	}
}
