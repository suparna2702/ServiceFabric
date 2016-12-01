package com.similan.portal.view.partner;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

import lombok.extern.slf4j.Slf4j;

import org.apache.commons.lang.StringUtils;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.DualListModel;
import org.primefaces.model.UploadedFile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.similan.domain.entity.partner.PartnerProgramRequiredAttributeType;
import com.similan.framework.dto.OrganizationDetailInfoDto;
import com.similan.framework.dto.member.MemberInfoDto;
import com.similan.framework.dto.partner.PartnerPreQualificationAnswerChoiceDto;
import com.similan.framework.dto.partner.PartnerPreQualificationQuestionDto;
import com.similan.framework.dto.partner.PartnerProgramBenifitDto;
import com.similan.framework.dto.partner.PartnerProgramDefinitionDto;
import com.similan.framework.dto.partner.PartnerRelationshipCategoryDto;
import com.similan.framework.dto.partner.PartnerRequiredAttributeDto;
import com.similan.portal.view.BaseView;
import com.similan.portal.view.handler.ImageDeletion;
import com.similan.portal.view.handler.ImageUpload;

@Scope("view")
@Component("partnerView")
@Slf4j
public class PartnerProgramView extends BaseView {

  private static final long serialVersionUID = 1L;
  public static final String IMAGES_PARTNER_DEFAULT_LOGO = "/images/businessLogo.jpg";

  @Autowired
  private OrganizationDetailInfoDto orgInfo;

  @Autowired
  private MemberInfoDto memberInfo;

  private PartnerPreQualificationQuestionDto currentQuestion = new PartnerPreQualificationQuestionDto();

  private String questionChoice;

  private boolean showChoice = true;

  private PartnerProgramDefinitionDto newProgram = null;

  private PartnerProgramBenifitDto newBenifit = new PartnerProgramBenifitDto();

  private PartnerRelationshipCategoryDto newCategory = new PartnerRelationshipCategoryDto();

  private boolean existingProgram = false;

  @PostConstruct
  public void init() {

    String partnerProgramIdParam = this.getContextParam("pid");
    log.info("Partner program id " + partnerProgramIdParam);

    List<PartnerRequiredAttributeDto> sourceAttr = new ArrayList<PartnerRequiredAttributeDto>();
    List<PartnerRequiredAttributeDto> targetAttr = null;
    PartnerProgramRequiredAttributeType[] attrEnumList = PartnerProgramRequiredAttributeType
        .values();

    for (PartnerProgramRequiredAttributeType attrType : attrEnumList) {
      PartnerRequiredAttributeDto attr = new PartnerRequiredAttributeDto();
      attr.setNameType(attrType);
      sourceAttr.add(attr);
    }

    if (partnerProgramIdParam != null) {
      Long partnerProgramId = Long.valueOf(partnerProgramIdParam);
      newProgram = this.getOrgService().getPartnerProgramById(partnerProgramId);
      log.info("Fetched Partner program by id " + partnerProgramIdParam);
      targetAttr = newProgram.getPartnerProgramAttributes();
      existingProgram = true;
    } else {
      log.info("Creating new Partner program");
      newProgram = new PartnerProgramDefinitionDto();
      newProgram.setOwner(orgInfo);
      newProgram.setCreator(this.memberInfo);
      targetAttr = new ArrayList<PartnerRequiredAttributeDto>();
    }

    DualListModel<PartnerRequiredAttributeDto> dualAttrList = new DualListModel<PartnerRequiredAttributeDto>(
        sourceAttr, targetAttr);
    newProgram.setPartnerRequiredInfoAttributes(dualAttrList);
  }

  public boolean getExistingProgram() {
    return existingProgram;
  }

  public void setExistingProgram(boolean existingProgram) {
    this.existingProgram = existingProgram;
  }

  public void addCategory() {
    log.info("Adding new category " + this.newCategory);
    this.newProgram.getPartnerRelationshipCategory().add(newCategory);
    this.newCategory = new PartnerRelationshipCategoryDto();
  }

  public PartnerRelationshipCategoryDto getNewCategory() {
    return newCategory;
  }

  public void setNewCategory(PartnerRelationshipCategoryDto newCategory) {
    this.newCategory = newCategory;
  }

  public void addBenefit() {
    log.info("Adding new benifits " + this.newBenifit);
    this.newProgram.getPartnerProgramBenifits().add(this.newBenifit);
    this.newBenifit = new PartnerProgramBenifitDto();

  }

  public PartnerProgramBenifitDto getNewBenifit() {
    return newBenifit;
  }

  public void setNewBenifit(PartnerProgramBenifitDto newBenifit) {
    this.newBenifit = newBenifit;
  }

  public PartnerProgramDefinitionDto getNewProgram() {
    return newProgram;
  }

  public void setNewProgram(PartnerProgramDefinitionDto newProgram) {
    this.newProgram = newProgram;
  }

  public String savePartnerProgram() {

    try {

      log.info("Saving partner program " + newProgram.getName());
      this.getPartnerManagementService().savePartnerProgram(newProgram);

      FacesContext.getCurrentInstance().addMessage(
          null,
          new FacesMessage(FacesMessage.SEVERITY_INFO, "Message",
              "Successfully created the partner program"));

    } catch (Exception exp) {
      log.error("Error saving partner program ", exp);
      FacesContext.getCurrentInstance().addMessage(
          null,
          new FacesMessage(FacesMessage.SEVERITY_ERROR, "Failure",
              "Failed to save partner program"));
    }

    return "result";
  }

  public void addPreQualQuestion() {

    log.info("Adding  questions " + this.currentQuestion);
    List<String> errors = validateQuestion(currentQuestion);
    if (errors.isEmpty()) {
      currentQuestion.setQuestionIndex(newProgram
          .getPreQualificationQuestions().size());
      newProgram.getPreQualificationQuestions().add(currentQuestion);
      this.currentQuestion = new PartnerPreQualificationQuestionDto();
    } else {
      FacesContext.getCurrentInstance().addMessage(
          null,
          new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error",
              "Unable to add question: "));
      for (String error : errors) {
        FacesContext.getCurrentInstance().addMessage(null,
            new FacesMessage(FacesMessage.SEVERITY_ERROR, "*", error));
      }
    }
  }

  private List<String> validateQuestion(
      PartnerPreQualificationQuestionDto question) {
    List<String> results = new ArrayList<String>();

    // Question text cannot be empty
    if (StringUtils.isBlank(question.getQuestionText().trim()))
      results.add("The question must be specified");

    // Multi choice questions always should have more than one choice
    // and choice text cannot be empty
    if (question.getMultiChoice()
        && question.getPreQualAnswerChoice().size() < 2)
      results
          .add("Multiple choice questions must have more then one possible answer.");

    // Single choice (List and Radio) much have more than one choice and
    // choice text cannot be empty
    if ((question.getSingleChoiceList() || question.getSingleChoiceRadio())
        && question.getPreQualAnswerChoice().size() < 2)
      results
          .add("Single choice questions must have more then one possible answer.");

    return results;
  }

  public String getLogoLocation() {
    if (newProgram.getLogoLocation() != null) {
      return newProgram.getLogoLocation();
    }
    return newProgram.getOwner().getLogoLocation();
  }

  public void handleFileUpload(final FileUploadEvent event) {
    this.imageUploadHandler.handleImageUpload(new ImageUpload() {

      public void update() throws Exception {
        // Nothing to do here, as the program will be saved later.
      }

      public void setImageKey(String key) {
        newProgram.setLogoLocation(key);
      }

      public String getType() {
        return "program";
      }

      public String getId() {
        // Using the Organization id here, since that will make all
        // the images go to the same logical bucket.
        // The uniqueness is given by the file name.
        // Example, all the program images for organization 5 wil go
        // to /program/5/. If two images there have the same name,
        // it's assumed that they are the same image.
        return String.valueOf(newProgram.getOwner().getId());
      }

      public UploadedFile getFile() {
        return event.getFile();
      }

      public String currentKey() {
        // Doesn't make much sense as we don't allow editing partner programs.
        return newProgram.getLogoLocation();
      }
    });
  }

  public boolean isPartnerLogoExists() {
    return this.newProgram != null
        && StringUtils.isNotBlank(this.newProgram.getLogoLocation())
        && !StringUtils.equalsIgnoreCase(this.newProgram.getLogoLocation(),
            IMAGES_PARTNER_DEFAULT_LOGO);
  }

  public void deletePhoto() {
    this.imageUploadHandler.handleImageDeletion(new ImageDeletion() {

      public void update() throws Exception {
        newProgram.setLogoLocation(IMAGES_PARTNER_DEFAULT_LOGO);
      }

      public String getCurrentKey() {
        if (newProgram.getLogoLocation().equals(IMAGES_PARTNER_DEFAULT_LOGO))
          return null;
        return newProgram.getLogoLocation();
      }
    });
  }

  public void showHideChoice() {

    log.info("Show choice value " + this.showChoice + " question type "
        + currentQuestion.getAnswerType());

    switch (currentQuestion.getAnswerType()) {
    case MultiChoice:
    case SingleChoiceList:
    case SingleChoiceRadio:
      this.showChoice = true;
      break;
    case TextInput:
    case Rating:
      this.showChoice = false;
      break;
    default:
      break;
    }
  }

  public boolean getShowChoice() {
    return showChoice;
  }

  public void setShowChoice(boolean showChoice) {
    this.showChoice = showChoice;
  }

  public PartnerPreQualificationQuestionDto getCurrentQuestion() {
    return currentQuestion;
  }

  public void setCurrentQuestion(
      PartnerPreQualificationQuestionDto currentQuestion) {
    this.currentQuestion = currentQuestion;
  }

  public String getQuestionChoice() {
    return questionChoice;
  }

  public void setQuestionChoice(String questionChoice) {
    this.questionChoice = questionChoice;
  }

  public void deleteAnswerChoice(int index) {
    currentQuestion.getPreQualAnswerChoice().remove(index);
  }

  public void addAnswerChoice() {
    log.info("adding answer choice " + this.questionChoice);
    if (!StringUtils.isBlank(questionChoice.trim())) {
      PartnerPreQualificationAnswerChoiceDto answerChoice = new PartnerPreQualificationAnswerChoiceDto();
      answerChoice.setAnswerChoiceText(questionChoice);
      this.currentQuestion.getPreQualAnswerChoice().add(answerChoice);
      questionChoice = "";
    } else {
      FacesContext.getCurrentInstance().addMessage(
          null,
          new FacesMessage(FacesMessage.SEVERITY_ERROR,
              "Unable to add question choice",
              "The question choice cannot be empty"));
    }
  }

  public void deleteQuestion(String uuID) {
    log.info("deleting answer choice with UUID " + uuID);

    for (PartnerPreQualificationQuestionDto questionDeleted : this.newProgram
        .getPreQualificationQuestions()) {
      if (questionDeleted.getQuestionUUID().equalsIgnoreCase(uuID) == true) {
        this.newProgram.getPreQualificationQuestions().remove(questionDeleted);
        break;
      }
    }
  }
}
