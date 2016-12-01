package com.similan.service.api;

public class CoreServiceErrorCode {
	
	public static String MEMBER_EMAIL_NOT_FOUND = "service.error.member.emailnotfound";
	public static String MEMBER_INFO_CREATION_FAILED = "service.error.member.membercreationfailed";
	public static String MEMBER_INCORRECT_CREDENTIAL = "service.error.member.incorrectcredential";
	public static String MEMBER_UUID_NOT_FOUND = "service.error.member.uuidnotfound";
	public static String DUPLICATE_PRIMARY_EMAIL_FOUND = "service.error.member.duplicatePrimaryEmailFound";
	public static final String ORGANIZATION_ID_NOT_FOUND = "service.error.crm.organizationNotFound";

	public static final String CRM_LEAD_SYNC_SETTINGS_NOT_FOUND = "service.error.crm.leadSyncSettingsNotFound";
	public static final String CRM_CONNECT_NOT_FOUND= "service.error.crm.crmConnectNotFound";
	public static final String CRM_SYNC_ALREADY_IN_PROCESS = "service.error.crm.already.in.progress";

	public static final String CRM_ZOHO_API_ERROR= "service.error.crm.zoho.error";
	public static final String CRM_ZOHO_API_ERROR_INVALID_DATA= "service.error.crm.zoho.error.invalid.data";
	public static final String CRM_ZOHO_API_ERROR_UNSUPPORTED_MODULE = "service.error.crm.zoho.error.unsupported.module";
}
