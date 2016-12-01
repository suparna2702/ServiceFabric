package com.similan.service.marshallers;

import java.util.ArrayList;
import java.util.List;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;

import com.similan.domain.entity.community.SocialActor;
import com.similan.domain.entity.community.SocialOrganization;
import com.similan.domain.entity.community.SocialPerson;
import com.similan.domain.entity.partner.PartnerPreQualificationAnswer;
import com.similan.domain.entity.partner.PartnerPreQualificationAnswerChoice;
import com.similan.domain.entity.partner.PartnerPreQualificationQuestion;
import com.similan.domain.entity.partner.PartnerPreQualificationQuestionResponse;
import com.similan.domain.entity.partner.PartnerProgramBenifit;
import com.similan.domain.entity.partner.PartnerProgramDefinition;
import com.similan.domain.entity.partner.PartnerProgramRequiredAttribute;
import com.similan.domain.entity.partner.PartnerProgramResponse;
import com.similan.domain.entity.partner.PartnerRelationshipCategory;
import com.similan.domain.entity.partner.Partnership;
import com.similan.domain.repository.partner.PartnerProgramBenifitRepository;
import com.similan.domain.repository.partner.PartnerProgramCommunicationAttributeRepository;
import com.similan.domain.repository.partner.PartnerProgramDefinitionRepository;
import com.similan.domain.repository.partner.PartnerProgramRequiredAttributeRepository;
import com.similan.domain.repository.partner.PartnerProgramResponseRepository;
import com.similan.domain.repository.partner.PartnerProgramTypeInfoRepository;
import com.similan.domain.repository.partner.PartnerRelationshipCategoryRepository;
import com.similan.domain.repository.partner.PartnershipRepository;
import com.similan.framework.dto.OrganizationBasicInfoDto;
import com.similan.framework.dto.OrganizationDetailInfoDto;
import com.similan.framework.dto.member.MemberInfoDto;
import com.similan.framework.dto.partner.PartnerPreQualificationAnswerChoiceDto;
import com.similan.framework.dto.partner.PartnerPreQualificationAnswerDto;
import com.similan.framework.dto.partner.PartnerPreQualificationQuestionDto;
import com.similan.framework.dto.partner.PartnerPreQualificationQuestionResponseDto;
import com.similan.framework.dto.partner.PartnerProgramBenifitDto;
import com.similan.framework.dto.partner.PartnerProgramDefinitionDto;
import com.similan.framework.dto.partner.PartnerProgramResponseDto;
import com.similan.framework.dto.partner.PartnerRelationshipCategoryDto;
import com.similan.framework.dto.partner.PartnerRequiredAttributeDto;
import com.similan.framework.dto.partner.PartnershipDto;

@Slf4j
public class PartnerProgramMarshaller {


  @Autowired
  private PartnershipRepository partnershipRepository;

  @Autowired
  private PartnerProgramDefinitionRepository partnerProgramRepository;

  @Autowired
  private PartnerProgramResponseRepository partnerProgramResponseRepository;

  @Autowired
  private PartnerProgramRequiredAttributeRepository partnerProgramRequiredAttributeRepository;

  @Autowired
  private PartnerProgramTypeInfoRepository partnerProgramTypeInfoRepository;

  @Autowired
  private PartnerProgramBenifitRepository partnerProgramBenifitRepository;

  @Autowired
  private PartnerProgramCommunicationAttributeRepository partnerProgramCommunicationAttributeRepository;

  @Autowired
  private PartnerRelationshipCategoryRepository partnerRelationshipCategoryRepository;

  @Autowired
  private MemberMarshaller memberMarshaller;

  public List<PartnerProgramDefinitionDto> marshallPartnerProgramDefinitions(
      List<PartnerProgramDefinition> partnerProgramDefList) {
    List<PartnerProgramDefinitionDto> partnerProgramList = new ArrayList<PartnerProgramDefinitionDto>();

    for (PartnerProgramDefinition partnerDef : partnerProgramDefList) {
      PartnerProgramDefinitionDto program = this
          .marshallPartnerProgramDefinition(partnerDef);
      partnerProgramList.add(program);
    }

    return partnerProgramList;
  }

  public PartnerProgramDefinitionDto marshallPartnerProgramDefinition(
      PartnerProgramDefinition partnerDef) {

    /* get count */
    Long partnershipCount = this.partnershipRepository
        .findPartnershipCountByPartnerProgram(partnerDef.getId());
    log.info("Partnership count " + partnershipCount);
    Integer intPartnershipCount = 0;

    if (partnershipCount != null) {
      intPartnershipCount = Integer.valueOf(String.valueOf(partnershipCount));
    }

    PartnerProgramDefinitionDto partnerProgram = new PartnerProgramDefinitionDto();
    partnerProgram.setId(partnerDef.getId());
    partnerProgram.setDescription(partnerDef.getDescription());
    partnerProgram.setProgramDetails(partnerDef.getProgramDetails());
    partnerProgram.setLogoLocation(partnerDef.getLogoLocation());
    partnerProgram.setOutStandingInvites(partnerDef.getOutStandingInvites());
    partnerProgram.setName(partnerDef.getName());
    partnerProgram.setTotalActiveMembers(intPartnershipCount);

    SocialOrganization partnerOwner = partnerDef.getProgramOwner();
    OrganizationDetailInfoDto orgInfo = new OrganizationDetailInfoDto();
    orgInfo.setId(partnerOwner.getId());
    orgInfo.setBusinessName(partnerOwner.getBusinessName());
    orgInfo.setLogoLocation(partnerOwner.getLogoUrl());
    partnerProgram.setOwner(orgInfo);

    SocialActor creator = partnerDef.getCreator();
    if (creator != null) {
      MemberInfoDto creatorDto = this.memberMarshaller
          .marshallMember((SocialPerson) creator);
      partnerProgram.setCreator(creatorDto);

    }

    log.info("Found partner program " + partnerDef.getId()
        + " for business " + partnerOwner.getId());

    /* categories */
    List<PartnerRelationshipCategoryDto> categories = this
        .marshallPartnerRelationshipCategorys(partnerDef
            .getRelationshipCategory());
    partnerProgram.setPartnerRelationshipCategory(categories);

    /* benifits */
    List<PartnerProgramBenifitDto> benifits = this
        .marshallPartnerBenifits(partnerDef.getProgramBenifit());
    partnerProgram.setPartnerProgramBenifits(benifits);

    /* pre qual questions */
    List<PartnerPreQualificationQuestion> preQualQuestions = partnerDef
        .getPartnerPreQualificationQuestions();
    partnerProgram.setPreQualificationQuestions(this
        .marshallPreQualicationQuestions(preQualQuestions));

    /* partner program attributes */
    List<PartnerProgramRequiredAttribute> attrList = partnerDef
        .getPartnerProgramAttributes();
    partnerProgram.setPartnerProgramAttributes(this
        .marshallPartnerProgramRequiredAttributes(attrList));

    return partnerProgram;
  }

  public List<PartnershipDto> marshallPartnerships(
      List<Partnership> partnerShipList) {

    List<PartnershipDto> retPartnershipList = new ArrayList<PartnershipDto>();

    for (Partnership partnership : partnerShipList) {
      PartnershipDto partnershipDto = this.marshallPartnership(partnership);
      retPartnershipList.add(partnershipDto);
    }

    return retPartnershipList;
  }

  public PartnershipDto marshallPartnership(Partnership partnership) {
    PartnershipDto retPartnershipDto = null;

    if (partnership != null) {

      log.info("Partnership info " + partnership.getId());
      retPartnershipDto = new PartnershipDto();
      retPartnershipDto.setId(partnership.getId());
      retPartnershipDto
          .setPartnershipStatus(partnership.getPartnershipStatus());
      retPartnershipDto.setCreated(partnership.getCreated());

      PartnerProgramDefinition partnerProgram = partnership.getPartnerProgram();
      PartnerProgramDefinitionDto partnerProgramDto = this
          .marshallPartnerProgramDefinition(partnerProgram);
      retPartnershipDto.setProgram(partnerProgramDto);

      SocialOrganization partnerOrg = partnership.getPartner();
      if (partnerOrg != null) {
        OrganizationBasicInfoDto orgTag = new OrganizationBasicInfoDto();
        orgTag.setId(partnerOrg.getId());
        orgTag.setLogoLocation(partnerOrg.getLogoUrl());
        orgTag.setDescription(partnerOrg.getBusinessDescription());
        orgTag.setName(partnerOrg.getBusinessName());
        orgTag.setIndustry(partnerOrg.getIndustry());
        retPartnershipDto.setOrgPartner(orgTag);
        orgTag.setPhone(orgTag.getPhone());
        orgTag.setEmail(orgTag.getEmail());
      }

      /* now get the response */
      PartnerProgramResponse response = partnership.getResponse();
      if (response != null) {
        PartnerProgramResponseDto responseDto = new PartnerProgramResponseDto();
        retPartnershipDto.setResponse(responseDto);

        if (response.getPartnerPreQualificationAnswer() != null) {
          List<PartnerPreQualificationAnswer> responseAnswerList = response
              .getPartnerPreQualificationAnswer();
          log.info("Numbe of answers " + responseAnswerList.size());

          /* get the answers and associated choices */
          for (PartnerPreQualificationAnswer answer : responseAnswerList) {

            /* create a dto */
            PartnerPreQualificationAnswerDto questionDto = new PartnerPreQualificationAnswerDto();
            questionDto.setId(answer.getId());
            questionDto.setAnswerType(answer.getAnswerType());
            questionDto.setQuestionId(answer.getQuestionId());
            questionDto.setQuestionText(answer.getQuestionText());
            log.info("Response text " + answer.getQuestionText());

            /* add choices */
            List<PartnerPreQualificationQuestionResponse> answerChoice = answer
                .getAnswerChoice();
            if (answerChoice != null) {
              log.info("Number of choice " + answerChoice.size());
              for (PartnerPreQualificationQuestionResponse choice : answerChoice) {

                PartnerPreQualificationQuestionResponseDto choiceDto = new PartnerPreQualificationQuestionResponseDto();
                choiceDto.setAnswerRating(choice.getAnswerRating());
                choiceDto.setAnswerText(choice.getAnswerText());
                choiceDto.setId(choice.getChoiceId());
                choiceDto.setChoiceText(choice.getChoiceText());
                log.info("Choice text " + choice.getChoiceText());
                questionDto.getAnswerChoices().add(choiceDto);
              }
            }

            responseDto.getPreQualQuestionResponses().add(questionDto);
          }
        }
      }

    }

    return retPartnershipDto;
  }

  public List<PartnerRequiredAttributeDto> marshallPartnerProgramRequiredAttributes(
      List<PartnerProgramRequiredAttribute> partnerAttrs) {
    List<PartnerRequiredAttributeDto> attrList = new ArrayList<PartnerRequiredAttributeDto>();

    for (PartnerProgramRequiredAttribute attr : partnerAttrs) {
      PartnerRequiredAttributeDto attrDto = this
          .marshallPartnerProgramRequiredAttribute(attr);
      attrList.add(attrDto);
    }

    return attrList;
  }

  public PartnerRequiredAttributeDto marshallPartnerProgramRequiredAttribute(
      PartnerProgramRequiredAttribute partnerAttr) {
    PartnerRequiredAttributeDto partnerAttrDto = new PartnerRequiredAttributeDto();
    partnerAttrDto.setId(partnerAttr.getId());
    partnerAttrDto.setNameType(partnerAttr.getAttributeType());

    log.info("A partner program attr  " + partnerAttr.getId());
    return partnerAttrDto;
  }

  public List<PartnerPreQualificationQuestionDto> marshallPreQualicationQuestions(
      List<PartnerPreQualificationQuestion> preQualQuestions) {
    List<PartnerPreQualificationQuestionDto> questionDtoList = new ArrayList<PartnerPreQualificationQuestionDto>();

    for (PartnerPreQualificationQuestion question : preQualQuestions) {
      PartnerPreQualificationQuestionDto questionDto = this
          .marshallPreQualicationQuestion(question);
      questionDtoList.add(questionDto);
    }
    return questionDtoList;
  }

  public PartnerPreQualificationQuestionDto marshallPreQualicationQuestion(
      PartnerPreQualificationQuestion preQualQuestionDef) {

    /* create a dto */
    PartnerPreQualificationQuestionDto questionDto = new PartnerPreQualificationQuestionDto();
    questionDto.setId(preQualQuestionDef.getId());
    questionDto.setAnswerType(preQualQuestionDef.getAnswerType());
    questionDto.setQuestionIndex(preQualQuestionDef.getQuestionIndex());
    questionDto.setQuestionText(preQualQuestionDef.getQuestionText());

    /* add choices */
    for (PartnerPreQualificationAnswerChoice choice : preQualQuestionDef
        .getAnswerChoice()) {

      PartnerPreQualificationAnswerChoiceDto choiceDto = new PartnerPreQualificationAnswerChoiceDto();
      choiceDto.setId(choice.getId());
      choiceDto.setAnswerChoiceText(choice.getAnswerChoiceText());
      choiceDto.setAnswerIndex(choice.getAnswerIndex());

      questionDto.getPreQualAnswerChoice().add(choiceDto);
    }

    return questionDto;
  }

  public List<PartnerRelationshipCategoryDto> marshallPartnerRelationshipCategorys(
      List<PartnerRelationshipCategory> categorys) {
    List<PartnerRelationshipCategoryDto> categoryList = new ArrayList<PartnerRelationshipCategoryDto>();
    for (PartnerRelationshipCategory category : categorys) {
      PartnerRelationshipCategoryDto categoryDto = this
          .marshallPartnerRelationshipCategory(category);
      categoryList.add(categoryDto);
    }

    return categoryList;
  }

  public PartnerRelationshipCategoryDto marshallPartnerRelationshipCategory(
      PartnerRelationshipCategory category) {
    PartnerRelationshipCategoryDto categoryDto = new PartnerRelationshipCategoryDto();
    categoryDto.setCustomName(category.getCustomName());
    categoryDto.setId(category.getId());
    categoryDto.setPartnerRelationshipCategoryType(category
        .getRelationshipCategoryType());
    categoryDto.setRelationshipDetails(category.getRelationshipDetails());

    return categoryDto;
  }

  public PartnerProgramBenifitDto marshallPartnerBenifit(
      PartnerProgramBenifit benifit) {
    PartnerProgramBenifitDto benifitDto = new PartnerProgramBenifitDto();
    benifitDto.setBenifitDetails(benifit.getBenifitDetails());
    benifitDto.setBenifitName(benifit.getBenifitName());
    benifitDto.setId(benifit.getId());
    return benifitDto;

  }

  public List<PartnerProgramBenifitDto> marshallPartnerBenifits(
      List<PartnerProgramBenifit> benifits) {
    List<PartnerProgramBenifitDto> benifitList = new ArrayList<PartnerProgramBenifitDto>();
    for (PartnerProgramBenifit benifit : benifits) {
      PartnerProgramBenifitDto benifitDto = this
          .marshallPartnerBenifit(benifit);
      benifitList.add(benifitDto);
    }

    return benifitList;
  }

}
