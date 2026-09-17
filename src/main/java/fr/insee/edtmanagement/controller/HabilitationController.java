package fr.insee.edtmanagement.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import fr.insee.edtmanagement.constants.Constants;
import fr.insee.edtmanagement.dto.HabilitationDto;
import fr.insee.edtmanagement.service.AuthorizationService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
public class HabilitationController {

	@Autowired
	private AuthorizationService authorizationService;

	@Operation(summary = "Check the habilitation for an interrogation")
	@GetMapping(path = Constants.API_HABILITATION_URL)
	public ResponseEntity<HabilitationDto> checkHabilitation(
			@RequestParam(value = "id", required = true) String interrogationId,
			@RequestParam(value = "role", required = false) String expectedRole,
			@RequestParam(value = "campaign", required = false) String campaignId,
			@RequestParam(value = "idep", required = true) String userId) {
		
		//Queen Back Office doesnt send the role when it concerns an INTERVIEWER
		if (!StringUtils.hasLength(expectedRole)) {
			expectedRole = Constants.INTERVIEWER;
		}
		
		log.info("Checking Habilitation for interrogation {} in campaign  {} with role {} as {} ",interrogationId,expectedRole,userId);

		HabilitationDto habilitation = new HabilitationDto();
		
		boolean kAutorisation= authorizationService.isAuthorized(interrogationId, expectedRole, userId);	
		habilitation.setHabilitated(kAutorisation);
		
		log.info("Access " + (kAutorisation ? "granted" : "denied") + " for {} on interrogation {} ", userId, interrogationId);

		return new ResponseEntity<>(habilitation, HttpStatus.OK);

	}

}