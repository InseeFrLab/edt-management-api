package fr.insee.edtmanagement.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import fr.insee.edtmanagement.domain.SurveyAssigment;

public interface SurveyAssigmentRepository extends CrudRepository<SurveyAssigment, Long> {

	Optional<List<SurveyAssigment>> findByInterviewerId(String interviewerId);
	Optional<List<SurveyAssigment>> findByReviewerId(String reviewerId);
	Optional<SurveyAssigment> findByInterrogationId(String interrogationId);

	Optional<SurveyAssigment> findByInterviewerIdIgnoreCaseAndInterrogationIdAndCampaignId(String interviewerId, String surveyUnitId,
			String campaignId);

	Optional<SurveyAssigment> findByReviewerIdIgnoreCaseAndInterrogationIdAndCampaignId(String reviewerId, String surveyUnitId,
			String campaignId);

	Optional<SurveyAssigment> findByInterviewerIdIgnoreCaseAndInterrogationId(String interviewerId, String surveyUnitId);

	Optional<SurveyAssigment> findByReviewerIdIgnoreCaseAndInterrogationId(String reviewerId, String surveyUnitId);
	
	Optional<List<SurveyAssigment>> findByReviewerIdIgnoreCase(String userId);
	Optional<List<SurveyAssigment>> findByInterviewerIdIgnoreCase(String userId);
	
	@Transactional
	@Modifying
	@Query("DELETE FROM SurveyAssigment s WHERE s.campaignId = :campaignId")
	int deleteByCampaignId(@Param("campaignId") String campaignId);

}
